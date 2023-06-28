package shgo.innowise.trainee.songmicroservice.songapi.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import shgo.innowise.trainee.songmicroservice.songapi.dto.SongDataDto;
import shgo.innowise.trainee.songmicroservice.songapi.entity.SongData;
import shgo.innowise.trainee.songmicroservice.songapi.repository.SongDataRepository;

import java.util.List;

@Service
@Slf4j
public class SongDataService {

    private SongDataRepository songDataRepository;

    @Autowired
    public SongDataService(SongDataRepository songDataRepository) {
        this.songDataRepository = songDataRepository;
    }

    public SongData getSongData(Long id) {
        return songDataRepository.findById(id)
                .orElseThrow(() -> {
                    String message = "Song data with id " + id + " cannot be found";
                    log.error(message);
                    return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
                });
    }

    public List<SongData> getAllSongData() {
        return Streamable.of(songDataRepository.findAll()).toList();
    }

    public List<SongData> getAllSongData(String namePattern) {
        return songDataRepository.findAllByNameContains(namePattern);
    }

    public SongData updateSongData(Long id, SongDataDto songDataDto) {
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

    public void deleteSongData(Long id) {
        log.info("Deleting song data with id {}", id);
        songDataRepository.deleteById(id);
    }

    public SongData createSongData(SongData songData){
        log.info("Saving song data with name {}", songData.getName());
        return songDataRepository.save(songData);
    }
}
