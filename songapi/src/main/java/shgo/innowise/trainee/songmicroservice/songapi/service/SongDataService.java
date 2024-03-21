package shgo.innowise.trainee.songmicroservice.songapi.service;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;
import shgo.innowise.trainee.songmicroservice.songapi.client.FileApiClient;
import shgo.innowise.trainee.songmicroservice.songapi.controller.request.SongDataRequest;
import shgo.innowise.trainee.songmicroservice.songapi.dto.SongDataDto;
import shgo.innowise.trainee.songmicroservice.songapi.entity.SongData;
import shgo.innowise.trainee.songmicroservice.songapi.repository.OffsetLimitPageRequest;
import shgo.innowise.trainee.songmicroservice.songapi.repository.SongDataRepository;
import shgo.innowise.trainee.songmicroservice.songapi.repository.specification.SongDataSpecificationProvider;

/**
 * Business logic of song data.
 */
@Service
@Slf4j
@Validated
public class SongDataService {

    private SongDataRepository songDataRepository;
    private FileApiClient fileApiClient;

    @Autowired
    public SongDataService(SongDataRepository songDataRepository,
                           FileApiClient fileApiClient) {
        this.songDataRepository = songDataRepository;
        this.fileApiClient = fileApiClient;
    }

    /**
     * Retrieves song data from database.
     *
     * @param id song data's id
     * @return song data
     */
    public SongData getSongData(final Long id) {
        return songDataRepository.findById(id)
                .orElseThrow(() -> {
                    String message = "Song data with id " + id + " cannot be found";
                    log.error(message);
                    return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
                });
    }

    /**
     * Retrieves songs data by song data request.
     *
     * @param songDataRequest song data retrieval request
     * @return page of song data
     */
    public Page<SongData> getAllSongData(@Valid final SongDataRequest songDataRequest) {
        return songDataRepository.findAll(SongDataSpecificationProvider.searchByRequest(songDataRequest),
                new OffsetLimitPageRequest(songDataRequest.getLimit(), songDataRequest.getOffset()));
    }

    /**
     * Updates song data.
     *
     * @param id          song data's id
     * @param songDataDto dto with data to update
     * @return update song data
     */
    public SongData updateSongData(final Long id, @Valid final SongDataDto songDataDto) {
        SongData songData = songDataRepository.findById(id)
                .orElseThrow(() -> {
                    String message = "Song data with id " + id + " cannot be found";
                    log.error(message);
                    return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
                });

        songData.setName(songDataDto.getName());
        songData.setArtist(songDataDto.getArtist());
        songData.setAlbum(songDataDto.getAlbum());
        songData.setReleaseDate(songDataDto.getReleaseDate());

        log.info("Updating song data with id {}", songData.getId());
        return songDataRepository.save(songData);
    }

    /**
     * Deletes song data.
     *
     * @param id song data's id
     */
    public void deleteSongData(final Long id) {
        SongData songData = songDataRepository.findById(id)
                .orElse(null);

        if (songData == null) { // prevents looping on deletion
            log.info("Song data with id {} cannot be found", id);
            return;
        }

        log.info("Deleting song data with id {}", id);
        songDataRepository.deleteById(id);
        fileApiClient.deleteFile(songData.getFileId());
    }

    /**
     * Creates song data.
     *
     * @param songData song data to create
     * @return created song data
     */
    public SongData createSongData(final SongData songData) {
        log.info("Saving song data with name {}", songData.getName());
        return songDataRepository.save(songData);
    }

    /**
     * Deletes song data by file id.
     *
     * @param fileId file id
     */
    public void deleteSongDataByFileId(Long fileId) {
        SongData songData = songDataRepository.findByFileId(fileId)
                .orElse(null);

        if (songData == null) { // prevents looping on deletion
            log.info("Song data with file id {} cannot be found", fileId);
            return;
        }

        log.info("Deleting song data with file id {}", fileId);
        songDataRepository.deleteById(songData.getId());
        fileApiClient.deleteFile(songData.getFileId());
    }
}
