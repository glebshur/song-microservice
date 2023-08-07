package shgo.innowise.trainee.songmicroservice.fileapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import shgo.innowise.trainee.songmicroservice.fileapi.service.storage.strategy.StorageStrategy;

/**
 * Configuration of main storage using property file.
 */
@Configuration
public class StorageStrategyConfig {

    private final ApplicationContext context;

    @Autowired
    public StorageStrategyConfig(ApplicationContext context) {
        this.context = context;
    }

    @Bean
    public StorageStrategy mainStorage(@Value("${main-storage}") String qualifier) {
        return context.getBean(qualifier, StorageStrategy.class);
    }
}
