package shgo.innowise.trainee.songmicroservice.songapi.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO of song data.
 */
@NoArgsConstructor
@Getter
@Setter
public class SongDataDto {

    private Long id;
    private Long fileId;
    private String name;
    private String artist;
    private String artistLink;
    private String album;
    private String albumLink;
    private String releaseDate;
    private Long duration;
}
