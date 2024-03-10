package shgo.innowise.trainee.songmicroservice.songapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shgo.innowise.trainee.songmicroservice.songapi.controller.request.PlaylistRequest;
import shgo.innowise.trainee.songmicroservice.songapi.dto.PlaylistDto;
import shgo.innowise.trainee.songmicroservice.songapi.entity.Playlist;
import shgo.innowise.trainee.songmicroservice.songapi.mapper.PlaylistDtoMapper;
import shgo.innowise.trainee.songmicroservice.songapi.service.PlaylistService;

import java.security.Principal;
import java.util.List;

/**
 * Rest controller for playlist.
 */
@RestController
@RequestMapping("/playlists")
public class PlaylistController {

    private final PlaylistService playlistService;
    private final PlaylistDtoMapper playlistDtoMapper;

    @Autowired
    public PlaylistController(PlaylistService playlistService, PlaylistDtoMapper playlistDtoMapper) {
        this.playlistService = playlistService;
        this.playlistDtoMapper = playlistDtoMapper;
    }

    /**
     * Get playlist by id.
     *
     * @param id playlist id
     * @return playlist dto
     */
    @GetMapping("/{id}")
    public PlaylistDto getPlaylist(@PathVariable final Long id) {
        return playlistDtoMapper.playlistToPlaylistDto(playlistService.getPlaylist(id));
    }

    /**
     * Get page with playlists by name patterns and user id.
     *
     * @param playlistRequest request object for retrieving playlists
     * @return list of playlist dto
     */
    @PostMapping("/")
    public ResponseEntity<List<PlaylistDto>> getPlaylists(@RequestBody final PlaylistRequest playlistRequest) {
        final Page<Playlist> playlistPage = playlistService.getAllPlaylists(playlistRequest);
        final List<Playlist> playlistsList = playlistPage.getContent();
        final String contentRange = "" + playlistRequest.getOffset() + "-" +
                (playlistRequest.getOffset() + playlistsList.size() - 1) + "/" + playlistPage.getTotalElements();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_RANGE, contentRange)
                .body(playlistsList.stream().map(playlistDtoMapper::playlistToPlaylistDto).toList());
    }

    /**
     * Creates new playlist.
     *
     * @param playlistDto playlist dto to create
     * @param principal   user principal
     * @return created playlist dto
     */
    @PostMapping("/create")
    @PreAuthorize("hasRole('USER')")
    public PlaylistDto createPlaylist(@RequestBody final PlaylistDto playlistDto,
                                      final Principal principal) {
        return playlistDtoMapper.playlistToPlaylistDto(
                playlistService.createPlaylist(playlistDto, principal));
    }

    /**
     * Updates playlist.
     *
     * @param id             playlist id
     * @param playlistDto    new playlist data
     * @param authentication user authentication
     * @return updated playlist
     */
    @PutMapping("/{id}/update")
    @PreAuthorize("hasRole('USER')")
    public PlaylistDto updatePlaylist(@PathVariable final Long id,
                                      @RequestBody final PlaylistDto playlistDto,
                                      final Authentication authentication) {
        return playlistDtoMapper.playlistToPlaylistDto(
                playlistService.updatePlaylist(id, playlistDto, authentication));
    }

    /**
     * Deletes playlist.
     *
     * @param id             playlist id
     * @param authentication user authentication
     */
    @DeleteMapping("/{id}/delete")
    @PreAuthorize("hasRole('USER')")
    public void deletePlaylist(@PathVariable final Long id,
                               final Authentication authentication) {
        playlistService.deletePlaylist(id, authentication);
    }
}
