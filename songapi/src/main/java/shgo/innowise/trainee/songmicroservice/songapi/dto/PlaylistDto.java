package shgo.innowise.trainee.songmicroservice.songapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * DTO of playlist.
 */
@NoArgsConstructor
@Getter
@Setter
public class PlaylistDto {

    private Long id;
    @NotBlank(message = "Name cannot be blank")
    private String name;
    private String userId;
    private List<SongDataDto> songs;
    private Boolean personal;
}
