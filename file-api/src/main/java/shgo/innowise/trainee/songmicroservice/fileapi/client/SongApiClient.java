package shgo.innowise.trainee.songmicroservice.fileapi.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Feign client of Song API.
 */
@FeignClient(value = "${song-api-name:song-api}", configuration = SongApiAuthConfiguration.class)
public interface SongApiClient {

    @DeleteMapping("/songs-data/file/{id}/delete")
    void deleteSongData(@PathVariable final Long id);
}
