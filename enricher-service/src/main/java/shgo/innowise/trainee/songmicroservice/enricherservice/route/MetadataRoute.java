package shgo.innowise.trainee.songmicroservice.enricherservice.route;

import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import shgo.innowise.trainee.songmicroservice.enricherservice.service.DefaultMetadataProvider;

/**
 * Camel route, that retrieves file metadata by id from file api.
 */
@Component
public class MetadataRoute extends EndpointRouteBuilder {
    private final DefaultMetadataProvider defaultMetadataProvider;

    @Autowired
    public MetadataRoute(DefaultMetadataProvider defaultMetadataProvider) {
        this.defaultMetadataProvider = defaultMetadataProvider;
    }

    @Override
    public void configure() {
        from(direct(RouteNames.METADATA_PROVIDER.getRouteName()))
                .process(exchange -> exchange.getMessage().setBody(defaultMetadataProvider
                        .getMetadata(exchange.getMessage().getBody(Long.class))))
                .to(direct(RouteNames.SPOTIFY_ENRICHER.getRouteName()));
    }
}
