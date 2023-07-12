package shgo.innowise.trainee.songmicroservice.songapi.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Converts jwt token from Keycloak.
 */
@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Value("${jwt.auth.converter.client-id}")
    private String clientId;
    @Value("${jwt.auth.converter.principal-attribute}")
    private String principleAttribute;

    private JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter =
            new JwtGrantedAuthoritiesConverter();

    private static final String RESOURCE_ACCESS_CODE = "resource_access";
    private static final String ROLES_CODE = "roles";

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
        Collection<GrantedAuthority> authorities = Stream.concat(
                jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
                extractResourceRoles(jwt).stream()
        ).collect(Collectors.toSet());

        return new JwtAuthenticationToken(
                jwt,
                authorities,
                gerPrincipleClaimName(jwt)
        );

    }

    /**
     * Get principle claim name value.
     *
     * @param jwt jwt token
     * @return claim name value
     */
    private String gerPrincipleClaimName(final Jwt jwt) {
        String claimName = JwtClaimNames.SUB;
        if (principleAttribute != null) {
            claimName = principleAttribute;
        }
        return jwt.getClaim(claimName);
    }

    /**
     * Extracts roles of specified client from Keycloak token.
     *
     * @param jwt jwt token
     * @return collection of authorities
     */
    private Collection<? extends GrantedAuthority> extractResourceRoles(final Jwt jwt) {
        Map<String, Object> resourceAccess;
        Map<String, Object> resource;
        Collection<String> resourceRoles;
        if (!jwt.hasClaim(RESOURCE_ACCESS_CODE)) {
            return Set.of();
        }
        resourceAccess = jwt.getClaim(RESOURCE_ACCESS_CODE);

        if (resourceAccess.get(clientId) == null) {
            return Set.of();
        }
        resource = (Map<String, Object>) resourceAccess.get(clientId);
        resourceRoles = (Collection<String>) resource.get(ROLES_CODE);

        return resourceRoles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role)) // adds default role's prefix for spring security
                .collect(Collectors.toSet());
    }
}
