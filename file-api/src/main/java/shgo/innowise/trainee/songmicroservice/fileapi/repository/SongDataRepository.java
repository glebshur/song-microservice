package shgo.innowise.trainee.songmicroservice.fileapi.repository;

import org.springframework.data.repository.CrudRepository;
import shgo.innowise.trainee.songmicroservice.fileapi.entity.SongData;

/**
 * Interface for song data repository.
 */
public interface SongDataRepository extends CrudRepository<SongData, Long> {

    boolean existsByOriginalName(String originalName);
}
