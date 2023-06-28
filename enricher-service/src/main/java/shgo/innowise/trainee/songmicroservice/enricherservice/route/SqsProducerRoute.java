package shgo.innowise.trainee.songmicroservice.enricherservice.route;

import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

/**
 * Camel routes that sends metadata to aws sqs.
 */
@Component
public class SqsProducerRoute extends EndpointRouteBuilder {

    @Override
    public void configure() throws Exception {
        from(direct(RouteNames.SQS_PRODUCER.getRouteName()))
                .marshal().json(JsonLibrary.Jackson)
                .removeHeaders("*", "Camel*")
                .to(aws2Sqs("{{producer-queue}}"));
    }
}
