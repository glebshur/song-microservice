package shgo.innowise.trainee.songmicroservice.fileapi.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import shgo.innowise.trainee.songmicroservice.fileapi.entity.Metadata;
import shgo.innowise.trainee.songmicroservice.fileapi.repository.MetadataRepository;

/**
 * Contains business logic of songs metadata.
 */
@Service
@Slf4j
public class MetadataService {

    private final MetadataRepository metadataRepository;

    @Autowired
    public MetadataService(MetadataRepository metadataRepository) {
        this.metadataRepository = metadataRepository;
    }

    /**
     * Gets metadata of specified song.
     *
     * @param id song file id
     * @return metadata
     */
    public Metadata getMetadata(Long id){
        return metadataRepository.findById(id)
                .orElseThrow(() -> {
                    String message = "Song metadata with id " + id + " cannot be found";
                    log.error(message);
                    return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
                });
    }
}
