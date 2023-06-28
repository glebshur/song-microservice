package shgo.innowise.trainee.songmicroservice.songapi.mapper;

import org.mapstruct.Mapper;
import shgo.innowise.trainee.songmicroservice.songapi.dto.SongDataDto;
import shgo.innowise.trainee.songmicroservice.songapi.entity.SongData;

@Mapper(componentModel = "spring")
public interface SongDataDtoMapper {
    SongDataDto songDataToSongDataDto(SongData songData);
    SongData songDataDtoToSongData(SongDataDto songDataDto);
}
