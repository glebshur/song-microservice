package shgo.innowise.trainee.songmicroservice.fileapi.service.storage.strategy;

import com.mongodb.MongoException;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import shgo.innowise.trainee.songmicroservice.fileapi.entity.SongData;
import shgo.innowise.trainee.songmicroservice.fileapi.entity.SongFile;
import shgo.innowise.trainee.songmicroservice.fileapi.entity.StorageType;
import shgo.innowise.trainee.songmicroservice.fileapi.exception.StorageException;
import shgo.innowise.trainee.songmicroservice.fileapi.service.storage.StorageStrategyRegistry;

import java.io.IOException;

/**
 * Provides operations in MongoDB.
 */
@Service("mongo-storage")
@Slf4j
public class MongoStorageStrategy implements StorageStrategy {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public MongoStorageStrategy(MongoTemplate mongoTemplate,
                                StorageStrategyRegistry registry) {
        this.mongoTemplate = mongoTemplate;
        registry.register(StorageType.MONGO, this);
    }

    /**
     * Saves song in mongo database.
     *
     * @param song audio file
     * @return song data
     */
    @Override
    public SongData saveSong(final MultipartFile song) {
        try {
            var songFile = new SongFile(new Binary(BsonBinarySubType.BINARY, song.getBytes()));
            mongoTemplate.insert(songFile);
            return new SongData(song.getOriginalFilename(), StorageType.MONGO, songFile.getId(), null);
        } catch (IOException | MongoException ex) {
            log.error(ex.getMessage());
            throw new StorageException(ex.getMessage());
        }
    }

    /**
     * Retrieves song from mongo database.
     *
     * @param songData song to download.
     * @return audio file as resource
     */
    @Override
    public Resource getSong(final SongData songData) {
        SongFile songFile = mongoTemplate.findById(songData.getPath(), SongFile.class);
        if (songFile != null) {
            return new ByteArrayResource(songFile.getSong().getData());
        }
        else {
            throw new StorageException("File with id " + songData.getPath() + " cannot be found");
        }
    }

    /**
     * Deletes audio file from mongo database.
     *
     * @param songData song to delete
     */
    @Override
    public void deleteSong(final SongData songData) {
        mongoTemplate.findAndRemove(new Query(Criteria.where("id").is(songData.getPath())),
                SongFile.class);
    }
}
