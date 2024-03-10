package shgo.innowise.trainee.songmicroservice.songapi.mapper;

import org.mapstruct.Mapper;
import shgo.innowise.trainee.songmicroservice.songapi.dto.PlaylistDto;
import shgo.innowise.trainee.songmicroservice.songapi.entity.Playlist;

/**
 * Mapstruct mapper interface for playlist and playlist dto mapping.
 */
@Mapper(componentModel = "spring")
public interface PlaylistDtoMapper {
    Playlist playlistDtoToPlaylist(PlaylistDto playlistDto);

    PlaylistDto playlistToPlaylistDto(Playlist playlist);
}
