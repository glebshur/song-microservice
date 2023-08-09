package shgo.innowise.trainee.songmicroservice.enricherservice.route.consumer;

import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.springframework.stereotype.Component;
import shgo.innowise.trainee.songmicroservice.enricherservice.route.RouteNames;

/**
 * Camel route, that gets file id from file api.
 */
@Component("sqs-consumer")
public class SqsConsumerRoute extends EndpointRouteBuilder {

    @Override
    public void configure() {
        from(aws2Sqs("{{message-brokers.sqs.consumer-queue}}").deleteAfterRead(true)
                .delay("{{message-brokers.sqs.delay:1000}}"))
                .to(direct(RouteNames.METADATA_PROVIDER.getRouteName()));
    }
}
