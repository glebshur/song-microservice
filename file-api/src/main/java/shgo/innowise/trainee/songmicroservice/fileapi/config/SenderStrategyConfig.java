package shgo.innowise.trainee.songmicroservice.fileapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import shgo.innowise.trainee.songmicroservice.fileapi.service.messagesender.strategy.SenderStrategy;

/**
 * Configuration of main sender using property file.
 */
@Configuration
public class SenderStrategyConfig {

    private final ApplicationContext context;

    @Autowired
    public SenderStrategyConfig(ApplicationContext context) {
        this.context = context;
    }

    @Bean
    public SenderStrategy mainSender(@Value("${main-sender}") String qualifier) {
        return context.getBean(qualifier, SenderStrategy.class);
    }
}
