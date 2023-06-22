package shgo.innowise.trainee.songmicroservice.enricherservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import shgo.innowise.trainee.songmicroservice.enricherservice.exception.TokenRequestException;
import shgo.innowise.trainee.songmicroservice.enricherservice.response.TokenResponse;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Provides tokens for Spotify API.
 */
@Service
@Slf4j
public class SpotifyTokenProvider {

    @Value("${spotify.client-id}")
    private String clientId;
    @Value("${spotify.client-secret}")
    private String clientSecret;
    @Value("${spotify.token-endpoint}")
    private String url;

    private final RestTemplate restTemplate = new RestTemplate();
    private String currentToken = null;
    private Instant expirationTime = null;

    private final long tokenExpiration = 3600;
    public String getToken() {
        if (currentToken == null || expirationTime.isBefore(Instant.now())) {
            currentToken = null;
            expirationTime = Instant.now().plus(tokenExpiration, ChronoUnit.SECONDS);
            currentToken = requestToken();
        }
        return currentToken;
    }

    private String requestToken() {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        var params = new LinkedMultiValueMap<String, String>();
        params.add("grant_type", "client_credentials");
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
        ResponseEntity<TokenResponse> response = restTemplate.exchange(url, HttpMethod.POST,
                entity, TokenResponse.class);

        if(response.getStatusCode().is2xxSuccessful() && response.hasBody()) {
            log.debug("Got new token");
            return response.getBody().getAccessToken();
        } else {
            log.error("Cannot get new token");
            throw new TokenRequestException("New spotify token cannot be obtained");
        }
    }
}
