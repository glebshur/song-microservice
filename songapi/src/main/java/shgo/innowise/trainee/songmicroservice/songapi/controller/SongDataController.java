package shgo.innowise.trainee.songmicroservice.songapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shgo.innowise.trainee.songmicroservice.songapi.dto.SongDataDto;
import shgo.innowise.trainee.songmicroservice.songapi.entity.SongData;
import shgo.innowise.trainee.songmicroservice.songapi.mapper.SongDataDtoMapper;
import shgo.innowise.trainee.songmicroservice.songapi.service.SongDataService;

import java.util.List;

@RestController
@RequestMapping("/songs-data")
public class SongDataController {
    private SongDataService songDataService;
    private SongDataDtoMapper songDataDtoMapper;

    @Autowired
    public SongDataController(SongDataService songDataService, SongDataDtoMapper songDataDtoMapper) {
        this.songDataService = songDataService;
        this.songDataDtoMapper = songDataDtoMapper;
    }

    @GetMapping("/{id}")
    public SongDataDto getSongData(@PathVariable Long id) {
        return songDataDtoMapper.songDataToSongDataDto(songDataService.getSongData(id));
    }

    @GetMapping("/")
    public List<SongDataDto> getSongsData(@RequestParam(required = false) String namePattern) {
        List<SongData> songDataList = namePattern == null ? songDataService.getAllSongData() :
                songDataService.getAllSongData(namePattern);
        return songDataList.stream().map(songDataDtoMapper::songDataToSongDataDto).toList();
    }

    @PutMapping("/{id}/update")
    public SongDataDto updateSongData(@PathVariable Long id,
                                      @RequestBody SongDataDto songDataDto) {
        return songDataDtoMapper.songDataToSongDataDto(
                songDataService.updateSongData(id, songDataDto));
    }

    @DeleteMapping("/{id}/delete")
    public void deleteSongData(@PathVariable Long id) {
        songDataService.deleteSongData(id);
    }
}
