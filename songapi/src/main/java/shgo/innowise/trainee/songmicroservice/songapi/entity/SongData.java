package shgo.innowise.trainee.songmicroservice.songapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Contains data of song.
 */
@NoArgsConstructor
@Setter
@Getter
@Entity
public class SongData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sd_id")
    private Long id;
    @Column(nullable = false, unique = true)
    private Long fileId;
    private String name;
    private String artist;
    private String artistLink;
    private String album;
    private String albumLink;
    private String releaseDate;
    private Long duration;
}
