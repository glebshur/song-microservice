package shgo.innowise.trainee.songmicroservice.songapi.service;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;
import shgo.innowise.trainee.songmicroservice.songapi.controller.request.PlaylistRequest;
import shgo.innowise.trainee.songmicroservice.songapi.dto.PlaylistDto;
import shgo.innowise.trainee.songmicroservice.songapi.entity.Playlist;
import shgo.innowise.trainee.songmicroservice.songapi.mapper.SongDataDtoMapper;
import shgo.innowise.trainee.songmicroservice.songapi.repository.OffsetLimitPageRequest;
import shgo.innowise.trainee.songmicroservice.songapi.repository.PlaylistRepository;
import shgo.innowise.trainee.songmicroservice.songapi.repository.specification.PlaylistSpecificationProvider;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Business logic of playlists.
 */
@Service
@Slf4j
@Validated
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final SongDataDtoMapper songDataDtoMapper;

    private static final String ADMIN_ROLE = "ROLE_ADMIN";

    @Autowired
    public PlaylistService(PlaylistRepository playlistRepository, SongDataDtoMapper songDataDtoMapper) {
        this.playlistRepository = playlistRepository;
        this.songDataDtoMapper = songDataDtoMapper;
    }

    /**
     * Retrieves playlist from database.
     *
     * @param id playlist's id
     * @return playlist
     */
    public Playlist getPlaylist(final Long id) {
        return playlistRepository.findById(id)
                .orElseThrow(() -> {
                    String message = "Playlist with id " + id + " cannot be found";
                    log.error(message);
                    return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
                });
    }

    /**
     * Retrieves playlists according to the playlist request.
     *
     * @param playlistRequest playlist retrieval request
     * @return page of playlist
     */
    public Page<Playlist> getAllPlaylists(@Valid final PlaylistRequest playlistRequest) {
        return playlistRepository.findAll(PlaylistSpecificationProvider.searchByRequest(playlistRequest),
                new OffsetLimitPageRequest(playlistRequest.getLimit(), playlistRequest.getOffset()));
    }

    /**
     * Creates new playlist.
     *
     * @param playlistDto playlist data to create
     * @param principal   principal with user id
     * @return created playlist
     */
    public Playlist createPlaylist(@Valid final PlaylistDto playlistDto,
                                   Principal principal) {
        Playlist playlist = new Playlist();
        playlist.setName(playlistDto.getName());
        playlist.setUserId(principal.getName());

        log.info("Creating playlist with name {}", playlist.getName());
        return playlistRepository.save(playlist);
    }

    /**
     * Deletes playlist.
     *
     * @param id             playlist id
     * @param authentication user authentication
     */
    public void deletePlaylist(final Long id, final Authentication authentication) {
        Playlist playlist = playlistRepository.findById(id).orElse(null);

        if (playlist == null) {
            log.info("Playlist with id {} cannot be found", id);
            return;
        }

        if (!isOwnerOrAdmin(playlist, authentication)) {
            String message = "Playlist cannot be deleted, user doesn't have enough permission";
            log.error(message);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, message);
        }

        log.info("Deleting playlist with id {}", id);
        playlistRepository.deleteById(id);
    }

    /**
     * Updates specified playlist.
     *
     * @param id             playlist id
     * @param playlistDto    data to update
     * @param authentication authentication user authentication
     * @return updated playlist
     */
    public Playlist updatePlaylist(final Long id, final PlaylistDto playlistDto, final Authentication authentication) {
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> {
                    String message = "Playlist with id " + id + " cannot be found";
                    log.error(message);
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
                });

        if (!isOwnerOrAdmin(playlist, authentication)) {
            String message = "Playlist cannot be updated, user doesn't have enough permission";
            log.error(message);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, message);
        }

        playlist.setName(playlistDto.getName());
        playlist.setSongs(new ArrayList<>(playlistDto.getSongs().stream()
                .map(songDataDtoMapper::songDataDtoToSongData)
                .toList()));

        log.info("Updating playlist with id {}", id);
        return playlistRepository.save(playlist);
    }

    private boolean isOwnerOrAdmin(final Playlist playlist, final Authentication authentication) {
        Collection<? extends GrantedAuthority> roles = authentication.getAuthorities();
        return playlist.getUserId().equals(authentication.getName()) || roles.stream()
                .anyMatch((role) -> role.getAuthority().equals(ADMIN_ROLE));
    }
}
