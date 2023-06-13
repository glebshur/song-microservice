package shgo.innowise.trainee.songmicroservice.enricherservice.route;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SqsConsumerRoute extends EndpointRouteBuilder {

    @Override
    public void configure() {
        from(aws2Sqs("{{consumer-queue}}").deleteAfterRead(true).delay("{{delay:1000}}"))
                .log("${body}");
    }
}
