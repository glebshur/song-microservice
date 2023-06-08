package shgo.innowise.trainee.songmicroservice.fileapi.service;

import io.awspring.cloud.sqs.operations.MessagingOperationFailedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import shgo.innowise.trainee.songmicroservice.fileapi.entity.SongData;
import shgo.innowise.trainee.songmicroservice.fileapi.entity.StorageType;
import shgo.innowise.trainee.songmicroservice.fileapi.repository.SongDataRepository;
import shgo.innowise.trainee.songmicroservice.fileapi.service.strategy.LocalStorageStrategy;
import shgo.innowise.trainee.songmicroservice.fileapi.service.strategy.S3StorageStrategy;
import software.amazon.awssdk.core.exception.SdkClientException;

import java.io.IOException;

/**
 * Contains main business logic of song processing.
 */
@Service
@Slf4j
public class SongService {
    private S3StorageStrategy s3StorageStrategy;
    private LocalStorageStrategy localStorageStrategy;
    private SongDataRepository songDataRepository;
    private SqsProvider sqsProvider;

    @Autowired
    public SongService(S3StorageStrategy s3StorageStrategy,
                       LocalStorageStrategy localStorageStrategy,
                       SongDataRepository songDataRepository,
                       SqsProvider sqsProvider) {
        this.s3StorageStrategy = s3StorageStrategy;
        this.localStorageStrategy = localStorageStrategy;
        this.songDataRepository = songDataRepository;
        this.sqsProvider = sqsProvider;
    }

    /**
     * Uploads song to storage.
     *
     * @param song audio file to upload
     * @return song data
     * @throws IOException error by file uploading
     */
    public SongData uploadSong(final MultipartFile song) throws IOException {
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
            songData = s3StorageStrategy.saveSong(song);
        } catch (SdkClientException ex) {
            log.error("Cannot connect to S3 storage: " + ex.getMessage());

            songData = localStorageStrategy.saveSong(song);
        }

        songData = songDataRepository.save(songData);
        log.debug("Data for song {} was saved to db", songData.getOriginalName());

        try {
            sqsProvider.sendSongData(songData);
        } catch (MessagingOperationFailedException ex) {
            log.error("Message sending failed: " + ex.getMessage());
        }
        return songData;
    }

    /**
     * Downloads file from storage.
     *
     * @param id audio file id
     * @return audio file
     */
    public Resource downloadSong(final Long id) {
        SongData songData = songDataRepository.findById(id)
                .orElseThrow(() -> {
                    String message = "Song data with id " + id + " cannot be found";
                    log.info(message);
                    return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
                });

        Resource resource = null;
        if (songData.getStorageType() == StorageType.S3) {
            resource = s3StorageStrategy.getSong(songData);
        } else {
            resource = localStorageStrategy.getSong(songData);
        }

        try {
            if (!resource.exists()) {
                log.error("File with id {} doesn't exists by path {}", songData.getId(), songData.getPath());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File with id "
                        + songData.getId() + " doesn't exists");
            }
        } catch (SdkClientException ex) {
            log.error("File accessing error: " + ex.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Cannot access file with id " + id);
        }
        return resource;
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
