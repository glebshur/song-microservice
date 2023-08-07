package shgo.innowise.trainee.songmicroservice.fileapi.entity;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.Binary;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * File to save in Mongo DB.
 */
@Document(collection = "song")
@Getter
@Setter
public class SongFile {
    private String id;
    private Binary song;

    public SongFile(Binary song) {
        this.song = song;
    }
}
