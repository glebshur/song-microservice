package shgo.innowise.trainee.songmicroservice.songapi.controller.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request for retrieving song data.
 */
@Getter
@Setter
@NoArgsConstructor
public class SongDataRequest extends PaginationRequest {
    private String name = "";
    private String artist = "";
    private String album = "";
    private String userId = "";
}
