package shgo.innowise.trainee.songmicroservice.songapi.controller.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request for retrieving song data.
 */
@Getter
@Setter
@NoArgsConstructor
public class SongDataRequest {
    @Min(value = 0, message = "Offset must be greater or equal 0")
    private long offset;
    @Min(value = 1, message = "Offset must be greater or equal 1")
    private int limit;

    @NotNull(message = "Name mustn't be null")
    private String name = "";
    @NotNull(message = "Artist mustn't be null")
    private String artist = "";
    @NotNull(message = "Album mustn't be null")
    private String album = "";
    @NotNull(message = "User ID mustn't be null")
    private String userId = "";
}
