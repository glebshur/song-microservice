package shgo.innowise.trainee.songmicroservice.enricherservice.route.producer;

import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;
import shgo.innowise.trainee.songmicroservice.enricherservice.route.RouteNames;

/**
 * Camel route that sends metadata to rabbitmq.
 */
@Component("rabbitmq-producer")
public class RabbitmqProducerRoute extends EndpointRouteBuilder {
    @Override
    public void configure() {
        from(direct(RouteNames.MESSAGE_PRODUCER.getRouteName()))
                .marshal().json(JsonLibrary.Jackson)
                .removeHeaders("*", "Camel*")
                .to(springRabbitmq("{{message-brokers.rabbitmq.producer.exchange}}")
                        .routingKey("{{message-brokers.rabbitmq.producer.queue}}"));
    }
}
