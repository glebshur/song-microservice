package shgo.innowise.trainee.songmicroservice.songapi.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import shgo.innowise.trainee.songmicroservice.songapi.client.FileApiClient;
import shgo.innowise.trainee.songmicroservice.songapi.dto.SongDataDto;
import shgo.innowise.trainee.songmicroservice.songapi.entity.SongData;
import shgo.innowise.trainee.songmicroservice.songapi.repository.SongDataRepository;

import java.util.List;

/**
 * Business logic of song data.
 */
@Service
@Slf4j
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
     * Retrieves all songs data from database.
     *
     * @return list of songs data
     */
    public List<SongData> getAllSongData() {
        return Streamable.of(songDataRepository.findAll()).toList();
    }

    /**
     * Retrieves songs data by name.
     *
     * @param namePattern string that contains the song name
     * @return list of song data
     */
    public List<SongData> getAllSongData(final String namePattern) {
        return songDataRepository.findAllByNameContains(namePattern);
    }

    /**
     * Updates song data.
     *
     * @param id          song data's id
     * @param songDataDto dto with data to update
     * @return update song data
     */
    public SongData updateSongData(final Long id, final SongDataDto songDataDto) {
        SongData songData = songDataRepository.findById(id)
                .orElseThrow(() -> {
                    String message = "Song data with id " + id + " cannot be found";
                    log.error(message);
                    return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
                });

        songData.setName(songDataDto.getName());
        songData.setArtist(songDataDto.getArtist());
        songData.setAlbum(songDataDto.getAlbum());

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

        if(songData == null) { // prevents looping on deletion
            log.info("Song data with id {} cannot be found", id);
            return;
        }

        log.info("Deleting song data with id {}", id);
        songDataRepository.deleteById(id);
        fileApiClient.deleteFile(id);
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
}
