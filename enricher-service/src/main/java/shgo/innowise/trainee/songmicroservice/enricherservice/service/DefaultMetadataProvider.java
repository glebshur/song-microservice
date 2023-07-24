package shgo.innowise.trainee.songmicroservice.enricherservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shgo.innowise.trainee.songmicroservice.enricherservice.client.FileApiClient;
import shgo.innowise.trainee.songmicroservice.enricherservice.client.FileApiMetadata;
import shgo.innowise.trainee.songmicroservice.enricherservice.entity.AudioMetadata;

/**
 * Retrieves metadata from audio files.
 */
@Service
public class DefaultMetadataProvider {

    private FileApiClient client;

    @Autowired
    public DefaultMetadataProvider(FileApiClient client) {
        this.client = client;
    }

    /**
     * Gets metadata of file.
     *
     * @param fileId file id
     * @return file metadata
     */
    public AudioMetadata getMetadata(final Long fileId) {
        final FileApiMetadata metadata = client.downloadMetadata(fileId);

        var audioMetadata = new AudioMetadata();
        audioMetadata.setFileId(fileId);
        audioMetadata.setName(metadata.getName());
        audioMetadata.setAlbum(metadata.getAlbum());
        audioMetadata.setArtist(metadata.getArtist());
        audioMetadata.setReleaseDate(metadata.getReleaseDate());
        audioMetadata.setDuration(metadata.getDuration());
        return audioMetadata;
    }
}
