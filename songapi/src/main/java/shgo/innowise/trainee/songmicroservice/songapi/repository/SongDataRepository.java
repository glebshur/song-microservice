package shgo.innowise.trainee.songmicroservice.songapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import shgo.innowise.trainee.songmicroservice.songapi.entity.SongData;

import java.util.Optional;

/**
 * Song data crud repository interface.
 */
@Repository
public interface SongDataRepository extends JpaRepository<SongData, Long>,
        JpaSpecificationExecutor<SongData> {
    Optional<SongData> findByFileId(Long fileId);
}
