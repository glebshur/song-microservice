package shgo.innowise.trainee.songmicroservice.enricherservice.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents metadata of audio file.
 */
@NoArgsConstructor
@Getter
@Setter
public class AudioMetadata {

    private Long fileId;
    private String name;
    private String artist;
    private String artistLink;
    private String album;
    private String albumLink;
    private String releaseDate;
    private Long duration;
}
