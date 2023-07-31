package shgo.innowise.trainee.songmicroservice.enricherservice.client;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Represents response from File API.
 */
@Getter
@Setter
@NoArgsConstructor
public class FileApiMetadata {
    private Long fileId;
    private String name;
    private String artist;
    private String album;
    private LocalDate releaseDate;
    private Long duration;
}
