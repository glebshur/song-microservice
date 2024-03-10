package shgo.innowise.trainee.songmicroservice.songapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Groups songs into lists.
 */
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pl_id")
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String userId;
    @ManyToMany
    @JoinTable(
            name = "playlist_song_data",
            joinColumns = @JoinColumn(name = "pl_id"),
            inverseJoinColumns = @JoinColumn(name = "sd_id")
    )
    private List<SongData> songs;
}
