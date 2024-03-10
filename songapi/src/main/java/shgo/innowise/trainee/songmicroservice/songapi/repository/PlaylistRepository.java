package shgo.innowise.trainee.songmicroservice.songapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import shgo.innowise.trainee.songmicroservice.songapi.entity.Playlist;

/**
 * Playlist jpa repository interface.
 */
@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long>,
        JpaSpecificationExecutor<Playlist> {
}
