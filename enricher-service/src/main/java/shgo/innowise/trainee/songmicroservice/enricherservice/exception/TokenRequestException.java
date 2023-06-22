package shgo.innowise.trainee.songmicroservice.enricherservice.exception;

/**
 * Exception shows that there is error by requesting a token.
 */
public class TokenRequestException extends RuntimeException {
    public TokenRequestException(String message) {
        super(message);
    }
}