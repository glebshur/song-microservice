package shgo.innowise.trainee.songmicroservice.fileapi.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for Rabbitmq sender.
 */
@Configuration
public class RabbitmqConfig {

    @Value("${rabbitmq.queue-name:file-enricher-queue}")
    private String queueName;

    @Bean
    public Queue fileEnricherQueue() {
        return new Queue(queueName);
    }
}
