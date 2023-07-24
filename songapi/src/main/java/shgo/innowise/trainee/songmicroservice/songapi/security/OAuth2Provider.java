package shgo.innowise.trainee.songmicroservice.songapi.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.stereotype.Service;

/**
 * OAuth2 token provider.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OAuth2Provider {

    public static final Authentication ANONYMOUS_USER_AUTHENTICATION =
            new AnonymousAuthenticationToken(
                    "key", "anonymous", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
    private final OAuth2AuthorizedClientManager authorizedClientManager;

    /**
     * Get token from authentication server.
     *
     * @param authServerName name of specified server
     * @return bearer token
     */
    public String getAuthenticationToken(final String authServerName) {
        final OAuth2AuthorizeRequest request =
                OAuth2AuthorizeRequest.withClientRegistrationId(authServerName)
                        .principal(ANONYMOUS_USER_AUTHENTICATION)
                        .build();
        OAuth2AuthorizedClient client = authorizedClientManager.authorize(request);

        if (client == null) {
            String message = "Client credentials flow on " + authServerName + " failed, client is null";
            log.error(message);
            throw new IllegalStateException(message);
        }

        return "Bearer " + client.getAccessToken().getTokenValue();
    }
}
