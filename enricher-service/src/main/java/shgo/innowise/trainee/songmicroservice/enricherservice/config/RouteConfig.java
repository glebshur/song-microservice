package shgo.innowise.trainee.songmicroservice.enricherservice.config;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for camel routes.
 */
@Configuration
public class RouteConfig {

    private final ApplicationContext applicationContext;
    private final CamelContext camelContext;

    @Autowired
    public RouteConfig(ApplicationContext applicationContext,
                       CamelContext camelContext) {
        this.applicationContext = applicationContext;
        this.camelContext = camelContext;
    }

    /**
     * Registers main consumer route in camel.
     *
     * @param qualifier consumer route name from properties.
     * @return consumer route
     * @throws Exception error by registering
     */
    @Bean
    public EndpointRouteBuilder mainConsumerRoute(@Value("${main-consumer-route}") String qualifier) throws Exception {
        EndpointRouteBuilder mainConsumerRoute = applicationContext.getBean(qualifier, EndpointRouteBuilder.class);
        camelContext.addRoutes(mainConsumerRoute);
        return mainConsumerRoute;
    }

    /**
     * Registers main producer route in camel.
     *
     * @param qualifier producer route name from properties.
     * @return producer route
     * @throws Exception error by registering
     */
    @Bean
    public EndpointRouteBuilder mainProducerRoute(@Value("${main-producer-route}") String qualifier) throws Exception {
        EndpointRouteBuilder mainProducerRoute = applicationContext.getBean(qualifier, EndpointRouteBuilder.class);
        camelContext.addRoutes(mainProducerRoute);
        return mainProducerRoute;
    }
}
