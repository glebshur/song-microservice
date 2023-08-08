package shgo.innowise.trainee.songmicroservice.enricherservice.route.consumer;

import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.springframework.stereotype.Component;
import shgo.innowise.trainee.songmicroservice.enricherservice.route.RouteNames;

/**
 * Camel route, that gets file id from file api.
 */
@Component("rabbitmq-consumer")
public class RabbitmqConsumerRoute extends EndpointRouteBuilder {

    @Override
    public void configure() {
        from(springRabbitmq("{{message-brokers.rabbitmq.consumer.exchange}}")
                .queues("{{message-brokers.rabbitmq.consumer.queue}}")
                .routingKey("{{message-brokers.rabbitmq.consumer.queue}}"))
                .to(direct(RouteNames.METADATA_PROVIDER.getRouteName()));
    }
}
