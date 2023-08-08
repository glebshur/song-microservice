package shgo.innowise.trainee.songmicroservice.enricherservice.route;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum with names of routes.
 */
@Getter
@AllArgsConstructor
public enum RouteNames {
    SPOTIFY_ENRICHER("spotifyEnricher"),
    MESSAGE_PRODUCER("messageProducer"),
    METADATA_PROVIDER("metadataProvider");

    private final String routeName;
}
