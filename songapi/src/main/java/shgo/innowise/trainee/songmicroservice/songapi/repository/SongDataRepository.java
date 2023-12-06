package shgo.innowise.trainee.songmicroservice.songapi.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shgo.innowise.trainee.songmicroservice.songapi.entity.SongData;

import java.util.List;

/**
 * Song data crud repository interface.
 */
@Repository
public interface SongDataRepository extends JpaRepository<SongData, Long> {

    List<SongData> findAllByNameContainsAndArtistContainsAndAlbumContainsAndUserIdContains(String namePattern,
                                                                          String artistPattern,
                                                                          String albumPattern,
                                                                          String userId,
                                                                          Pageable pageable);
}
