package shgo.innowise.trainee.songmicroservice.enricherservice.route.producer;

import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;
import shgo.innowise.trainee.songmicroservice.enricherservice.route.RouteNames;

/**
 * Camel routes that sends metadata to aws sqs.
 */
@Component("sqs-producer")
public class SqsProducerRoute extends EndpointRouteBuilder {

    @Override
    public void configure() {
        from(direct(RouteNames.MESSAGE_PRODUCER.getRouteName()))
                .marshal().json(JsonLibrary.Jackson)
                .removeHeaders("*", "Camel*")
                .to(aws2Sqs("{{message-brokers.sqs.producer-queue}}"));
    }
}
