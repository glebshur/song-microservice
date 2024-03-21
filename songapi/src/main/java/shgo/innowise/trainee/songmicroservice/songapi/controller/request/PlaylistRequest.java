package shgo.innowise.trainee.songmicroservice.songapi.controller.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request for retrieving playlists.
 */
@Getter
@Setter
@NoArgsConstructor
public class PlaylistRequest extends PaginationRequest {
    private String name;
    private String userId;
    private Boolean personal;
}
