package shgo.innowise.trainee.songmicroservice.fileapi.service;

import io.awspring.cloud.sqs.operations.MessagingOperationFailedException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.exception.TikaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.xml.sax.SAXException;
import shgo.innowise.trainee.songmicroservice.fileapi.client.SongApiClient;
import shgo.innowise.trainee.songmicroservice.fileapi.entity.Metadata;
import shgo.innowise.trainee.songmicroservice.fileapi.entity.SongData;
import shgo.innowise.trainee.songmicroservice.fileapi.entity.SongResponse;
import shgo.innowise.trainee.songmicroservice.fileapi.exception.SenderException;
import shgo.innowise.trainee.songmicroservice.fileapi.exception.StorageException;
import shgo.innowise.trainee.songmicroservice.fileapi.repository.MetadataRepository;
import shgo.innowise.trainee.songmicroservice.fileapi.repository.SongDataRepository;
import shgo.innowise.trainee.songmicroservice.fileapi.service.messagesender.strategy.SenderStrategy;
import shgo.innowise.trainee.songmicroservice.fileapi.service.messagesender.strategy.SqsSender;
import shgo.innowise.trainee.songmicroservice.fileapi.service.storage.strategy.LocalStorageStrategy;
import shgo.innowise.trainee.songmicroservice.fileapi.service.storage.strategy.StorageStrategy;
import shgo.innowise.trainee.songmicroservice.fileapi.service.storage.StorageStrategyRegistry;
import software.amazon.awssdk.core.exception.SdkClientException;

import java.io.IOException;

/**
 * Contains main business logic of song processing.
 */
@Service
@Slf4j
public class SongService {
    private final StorageStrategy mainStorage;
    private final LocalStorageStrategy localStorageStrategy;
    private final SongDataRepository songDataRepository;
    private final MetadataRepository metadataRepository;
    private final MetadataProvider metadataProvider;
    private final SenderStrategy mainSender;
    private final SongApiClient songApiClient;
    private final TransactionTemplate transactionTemplate;
    private final StorageStrategyRegistry storageStrategyRegistry;

    @Autowired
    public SongService(@Qualifier("mainStorage") StorageStrategy mainStorage,
                       LocalStorageStrategy localStorageStrategy,
                       SongDataRepository songDataRepository,
                       MetadataRepository metadataRepository,
                       MetadataProvider metadataProvider,
                       @Qualifier("mainSender") SenderStrategy mainSender,
                       SongApiClient songApiClient,
                       PlatformTransactionManager platformTransactionManager,
                       StorageStrategyRegistry storageStrategyRegistry) {
        this.mainStorage = mainStorage;
        this.localStorageStrategy = localStorageStrategy;
        this.songDataRepository = songDataRepository;
        this.metadataRepository = metadataRepository;
        this.metadataProvider = metadataProvider;
        this.mainSender = mainSender;
        this.songApiClient = songApiClient;
        this.transactionTemplate = new TransactionTemplate(platformTransactionManager);
        this.storageStrategyRegistry = storageStrategyRegistry;
    }

    /**
     * Uploads song to storage.
     *
     * @param song audio file to upload
     * @return song data
     * @throws IOException   error by file uploading
     * @throws TikaException exception by parsing file
     * @throws SAXException  exception by parsing metadata
     */
    @Transactional
    public SongData uploadSong(final MultipartFile song) throws IOException, TikaException, SAXException {
        if (!isAudioFile(song)) {
            String message = song.getOriginalFilename() + " isn't an audio file";
            log.info(message);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }

        if (songDataRepository.existsByOriginalName(song.getOriginalFilename())) {
            String message = "Song with name " + song.getOriginalFilename() + " already exists";
            log.info(message);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }

        SongData songData;
        try {
            songData = mainStorage.saveSong(song);
        } catch (SdkClientException | CallNotPermittedException | StorageException ex) {
            log.error("Error in main storage: " + ex.getMessage());

            songData = localStorageStrategy.saveSong(song);
        }

        songData = songDataRepository.save(songData);
        log.debug("Data for song {} was saved to db", songData.getOriginalName());

        Metadata metadata = metadataProvider.getMetadata(songData.getId(), song.getResource());
        metadata = metadataRepository.save(metadata);
        log.debug("Metadata for song with id {} was saved to db", metadata.getFileId());

        try {
            mainSender.sendSongData(songData);
        } catch (SenderException ex) {
            log.error("Message sending failed: " + ex.getMessage());
        }
        return songData;
    }

    /**
     * Downloads file from storage.
     *
     * @param id audio file id
     * @return audio file with filename
     */
    public SongResponse downloadSong(final Long id) {
        SongData songData = songDataRepository.findById(id)
                .orElseThrow(() -> {
                    String message = "Song with id " + id + " cannot be found";
                    log.error(message);
                    return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
                });

        Resource resource = null;

        try {
            resource = storageStrategyRegistry.getStorageStrategy(songData.getStorageType())
                    .getSong(songData);
            if (!resource.exists()) {
                log.error("File with id {} doesn't exists by path {}", songData.getId(), songData.getPath());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File with id "
                        + songData.getId() + " doesn't exists");
            }
        } catch (SdkClientException | StorageException ex) {
            log.error("File accessing error: " + ex.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Cannot access file with id " + id);
        }
        return new SongResponse(songData.getOriginalName(), resource);
    }

    /**
     * Deletes all song data and song from the storage.
     *
     * @param id song id
     */
    public void deleteSong(final Long id) {
        SongData songData = songDataRepository.findById(id)
                .orElse(null);

        if (songData == null) { // prevents looping on deleting
            log.info("Song with id {} cannot be found", id);
            return;
        }

        transactionTemplate.execute(status -> {
            metadataRepository.deleteById(id);
            songDataRepository.deleteById(id);
            return null;
        });

        storageStrategyRegistry.getStorageStrategy(songData.getStorageType())
                .deleteSong(songData);
        songApiClient.deleteSongData(id);
    }

    /**
     * Checks if file is audio file.
     *
     * @param file multipart file to check
     * @return true, if it's audio file; false, if it isn't an audio file
     */
    private boolean isAudioFile(final MultipartFile file) {
        String contentType = file.getContentType();
        String mimeType = "audio";
        return contentType != null && contentType.startsWith(mimeType);
    }
}
