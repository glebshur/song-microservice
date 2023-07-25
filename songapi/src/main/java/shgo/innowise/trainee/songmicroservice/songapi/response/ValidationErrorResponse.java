package shgo.innowise.trainee.songmicroservice.songapi.response;

import java.util.List;

/**
 * Response with list of violations.
 *
 * @param violations list of violations
 */
public record ValidationErrorResponse(List<Violation> violations) {
}
