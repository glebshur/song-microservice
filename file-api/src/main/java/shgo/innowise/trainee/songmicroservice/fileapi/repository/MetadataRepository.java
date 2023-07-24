package shgo.innowise.trainee.songmicroservice.fileapi.repository;

import org.springframework.data.repository.CrudRepository;
import shgo.innowise.trainee.songmicroservice.fileapi.entity.Metadata;

/**
 * Interface for song metadata repository.
 */
public interface MetadataRepository extends CrudRepository<Metadata, Long> {
}
