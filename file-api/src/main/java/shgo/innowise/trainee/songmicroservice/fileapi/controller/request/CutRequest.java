package shgo.innowise.trainee.songmicroservice.fileapi.controller.request;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request for audio cutting.
 */
@Getter
@Setter
@NoArgsConstructor
public class CutRequest {
    private Long songId;
    @Min(value = 0, message = "Start time must be greater or equal 0")
    private Float startTime;
    @Min(value = 0, message = "End time must be greater or equal 0")
    private Float endTime;
}
