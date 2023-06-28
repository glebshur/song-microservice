package shgo.innowise.trainee.songmicroservice.songapi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import shgo.innowise.trainee.songmicroservice.songapi.entity.SongData;

import java.util.List;

@Repository
public interface SongDataRepository extends CrudRepository<SongData, Long> {

    List<SongData> findAllByNameContains(String namePattern);
}
