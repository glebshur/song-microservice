package shgo.innowise.trainee.songmicroservice.songapi.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Feign client of File API.
 */
@FeignClient(value = "${file-api-name:file-api}", configuration = FileApiAuthConfiguration.class)
public interface FileApiClient {

    @DeleteMapping("/files/{id}/delete")
    ResponseEntity<Object> deleteFile(@PathVariable("id") final Long id);
}
