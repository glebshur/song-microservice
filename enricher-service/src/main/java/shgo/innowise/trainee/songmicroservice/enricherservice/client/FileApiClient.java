package shgo.innowise.trainee.songmicroservice.enricherservice.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Feign client of File API.
 */
@FeignClient(value = "${file-api-name:file-api}", configuration = FileApiAuthConfiguration.class)
public interface FileApiClient {

    /**
     * Retrieves song metadata from File API.
     *
     * @param id song id
     * @return song metadata
     */
    @GetMapping(value = "/metadata/{id}")
    @CircuitBreaker(name = "fileApiClientBreaker")
    FileApiMetadata downloadMetadata(final @PathVariable("id") Long id);
}
