package shgo.innowise.trainee.songmicroservice.fileapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.io.Resource;

/**
 * Data for response in controller.
 */
@Getter
@Setter
@AllArgsConstructor
public class SongResponse {
    private String filename;
    private Resource song;
}
