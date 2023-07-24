package shgo.innowise.trainee.songmicroservice.fileapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents metadata of audio file.
 */
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "song_metadata")
public class Metadata {

    @Id
    @Column(name = "sd_id")
    private Long fileId;
    @Column(name = "name")
    private String name;
    @Column(name = "artist")
    private String artist;
    @Column(name = "album")
    private String album;
    @Column(name = "release_date")
    private String releaseDate;
    @Column(name = "duration")
    private Long duration;
}
