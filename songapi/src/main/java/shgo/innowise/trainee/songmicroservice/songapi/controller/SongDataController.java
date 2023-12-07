package shgo.innowise.trainee.songmicroservice.songapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shgo.innowise.trainee.songmicroservice.songapi.controller.request.SongDataRequest;
import shgo.innowise.trainee.songmicroservice.songapi.dto.SongDataDto;
import shgo.innowise.trainee.songmicroservice.songapi.entity.SongData;
import shgo.innowise.trainee.songmicroservice.songapi.mapper.SongDataDtoMapper;
import shgo.innowise.trainee.songmicroservice.songapi.service.SongDataService;

import java.util.List;

/**
 * Rest controller for song data.
 */
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

    /**
     * Get song data by id.
     *
     * @param id song data's id
     * @return song data dto
     */
    @GetMapping("/{id}")
    public SongDataDto getSongData(@PathVariable final Long id) {
        return songDataDtoMapper.songDataToSongDataDto(songDataService.getSongData(id));
    }

    /**
     * Get page with songs data by name, album and artist patterns.
     *
     * @param songDataRequest request object for retrieving data
     * @return list of song data dto
     */
    @PostMapping("/")
    public List<SongDataDto> getSongsData(@RequestBody SongDataRequest songDataRequest) {
        List<SongData> songDataList = songDataService.getAllSongData(songDataRequest);
        return songDataList.stream().map(songDataDtoMapper::songDataToSongDataDto).toList();
    }

    /**
     * Updates song data.
     *
     * @param id song data id
     * @param songDataDto data to update
     * @return updated song data dto
     */
    @PutMapping("/{id}/update")
    @PreAuthorize("hasRole('ADMIN')")
    public SongDataDto updateSongData(@PathVariable final Long id,
                                      @RequestBody final SongDataDto songDataDto) {
        return songDataDtoMapper.songDataToSongDataDto(
                songDataService.updateSongData(id, songDataDto));
    }

    /**
     * Deletes song data by id.
     *
     * @param id song data's id
     */
    @DeleteMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteSongData(@PathVariable final Long id) {
        songDataService.deleteSongData(id);
    }

    /**
     * Deletes song data by file id (for calling from file-api).
     *
     * @param id file id
     */
    @DeleteMapping("/file/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteSongDataByFile(@PathVariable final Long id) {
        songDataService.deleteSongDataByFileId(id);
    }


}
