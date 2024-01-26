package shgo.innowise.trainee.songmicroservice.enricherservice.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import shgo.innowise.trainee.songmicroservice.enricherservice.service.SpotifyTokenProvider;

public class SpotifyTokenProviderTest {

    SpotifyTokenProvider spotifyTokenProvider;

    @BeforeEach
    public void init() {
        spotifyTokenProvider = new SpotifyTokenProvider();
        ReflectionTestUtils.setField(spotifyTokenProvider, "url", "https://accounts.spotify.com/api/token");
        ReflectionTestUtils.setField(spotifyTokenProvider, "clientId", "4a67fa16eb1943e581d4853135ad4099");
        ReflectionTestUtils.setField(spotifyTokenProvider, "clientSecret", "69d6db9055c64ed88c3fbafcb84227d5");
    }

    @Test
    public void givenProvider_whenGetToken_thenReturnToken() {
        Assertions.assertNotNull(spotifyTokenProvider.getToken());
    }

    @Test
    public void givenTwoNotExpiredTokens_whenGetToken_thenTokensEqual() {
        String firstToken = spotifyTokenProvider.getToken();
        String secondToken = spotifyTokenProvider.getToken();

        Assertions.assertEquals(firstToken, secondToken);
    }
}
