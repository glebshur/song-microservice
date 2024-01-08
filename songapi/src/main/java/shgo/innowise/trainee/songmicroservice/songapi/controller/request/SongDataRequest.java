package shgo.innowise.trainee.songmicroservice.songapi.controller.request;

import jakarta.validation.constraints.Min;
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

    private String name = "";
    private String artist = "";
    private String album = "";
    private String userId = "";
}
