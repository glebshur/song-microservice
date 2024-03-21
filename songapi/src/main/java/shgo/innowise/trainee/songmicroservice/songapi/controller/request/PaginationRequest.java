package shgo.innowise.trainee.songmicroservice.songapi.controller.request;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

/**
 * Contains pagination data.
 */
@Getter
@Setter
public abstract class PaginationRequest {
    @Min(value = 0, message = "Offset must be greater or equal 0")
    private long offset;
    @Min(value = 1, message = "Offset must be greater or equal 1")
    private int limit;
}
