package shgo.innowise.trainee.songmicroservice.enricherservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Feign client of File API.
 */
@FeignClient("${file-api-name:file-api}")
public interface FileApiClient {

    /**
     * Downloads file from File API.
     *
     * @param id file id
     * @return file as resource
     */
    @GetMapping(value = "/files/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    Resource downloadFile(final @PathVariable("id") Long id);
}
