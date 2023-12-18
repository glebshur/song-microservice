package shgo.innowise.trainee.songmicroservice.songapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * DTO of song data.
 */
@NoArgsConstructor
@Getter
@Setter
public class SongDataDto {

    private Long id;
    private Long fileId;
    @NotBlank(message = "Name cannot be blank")
    private String name;
    @NotBlank(message = "Artist cannot be blank")
    private String artist;
    private String artistLink;
    private String album;
    private String albumLink;
    @NotNull(message = "Release date cannot be null")
    private LocalDate releaseDate;
    private Long duration;
    private String userId;
}
