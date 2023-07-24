package shgo.innowise.trainee.songmicroservice.fileapi.client;

import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import shgo.innowise.trainee.songmicroservice.fileapi.security.OAuth2Provider;

/**
 * Authentication configuration for File API client.
 */
@Configuration
@RequiredArgsConstructor
public class SongApiAuthConfiguration {
    private final String AUTH_SERVER_NAME = "keycloak";
    private final OAuth2Provider oAuth2Provider;

    @Bean
    public RequestInterceptor fileApiAuthInterceptor(){
        return requestTemplate -> requestTemplate
                .header(HttpHeaders.AUTHORIZATION, oAuth2Provider.getAuthenticationToken(AUTH_SERVER_NAME));
    }
}
