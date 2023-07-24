package shgo.innowise.trainee.songmicroservice.enricherservice.route;

import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import shgo.innowise.trainee.songmicroservice.enricherservice.service.DefaultMetadataProvider;

/**
 * Camel route, that gets file id and file from file api.
 */
@Component
public class SqsConsumerRoute extends EndpointRouteBuilder {

    private final DefaultMetadataProvider defaultMetadataProvider;

    @Autowired
    public SqsConsumerRoute(DefaultMetadataProvider defaultMetadataProvider) {
        super();
        this.defaultMetadataProvider = defaultMetadataProvider;
    }

    @Override
    public void configure() {
        from(aws2Sqs("{{consumer-queue}}").deleteAfterRead(true).delay("{{delay:1000}}"))
                .process(exchange -> exchange.getMessage().setBody(defaultMetadataProvider
                        .getMetadata(exchange.getMessage().getBody(Long.class))))
                .to(direct(RouteNames.SPOTIFY_ENRICHER.getRouteName()));
    }
}
