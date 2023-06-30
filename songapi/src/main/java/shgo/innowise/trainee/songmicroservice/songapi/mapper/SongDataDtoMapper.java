package shgo.innowise.trainee.songmicroservice.songapi.mapper;

import org.mapstruct.Mapper;
import shgo.innowise.trainee.songmicroservice.songapi.dto.SongDataDto;
import shgo.innowise.trainee.songmicroservice.songapi.entity.SongData;

/**
 * Mapstruct mapper interface for song data and song data dto mapping.
 */
@Mapper(componentModel = "spring")
public interface SongDataDtoMapper {
    SongDataDto songDataToSongDataDto(SongData songData);
    SongData songDataDtoToSongData(SongDataDto songDataDto);
}
