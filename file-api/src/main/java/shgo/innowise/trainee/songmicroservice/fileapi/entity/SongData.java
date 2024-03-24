package shgo.innowise.trainee.songmicroservice.fileapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity contains data about song in file api.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "song_data")
public class SongData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sd_id")
    private Long id;
    @Column(name = "original_name", unique = true)
    private String originalName;
    @Column(name = "storage_type")
    @Enumerated(EnumType.ORDINAL)
    private StorageType storageType;
    @Column(name = "path")
    private String path;
    @Column(name = "bucket_name")
    private String bucketName;

    @Column(name = "hash")
    private String hash;

    public SongData(String originalName,
                    StorageType storageType,
                    String path,
                    String bucketName) {
        this.originalName = originalName;
        this.storageType = storageType;
        this.path = path;
        this.bucketName = bucketName;
    }
}
