package shgo.innowise.trainee.songmicroservice.songapi.response;

/**
 * Contains data about validation errors.
 *
 * @param field field name
 * @param message error message
 */
public record Violation(String field, String message) {
}
