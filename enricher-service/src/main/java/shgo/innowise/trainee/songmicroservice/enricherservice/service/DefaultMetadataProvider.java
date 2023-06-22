package shgo.innowise.trainee.songmicroservice.enricherservice.service;

import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import shgo.innowise.trainee.songmicroservice.enricherservice.client.FileApiClient;
import shgo.innowise.trainee.songmicroservice.enricherservice.entity.AudioMetadata;
import shgo.innowise.trainee.songmicroservice.enricherservice.entity.AudioMetadataFields;
import shgo.innowise.trainee.songmicroservice.enricherservice.exception.UnsupportedFileException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Retrieves metadata from audio files.
 */
@Service
public class DefaultMetadataProvider {

    private FileApiClient client;
    private AutoDetectParser parser;
    private DefaultDetector detector;

    private final String audioType = "audio";

    @Autowired
    public DefaultMetadataProvider(FileApiClient client) {
        this.client = client;
        parser = new AutoDetectParser();
        detector = new DefaultDetector();
    }

    /**
     * Gets metadata of file.
     *
     * @param fileId file id
     * @return file metadata
     * @throws IOException exception by working with file
     * @throws TikaException exception by parsing file
     * @throws SAXException exception by parsing metadata
     */
    public AudioMetadata getMetadata(final Long fileId) throws IOException, TikaException, SAXException {
        final Resource resource = client.downloadFile(fileId);

        if (!isAudioFile(resource.getInputStream())) {
            throw new UnsupportedFileException("File should be audio file");
        }

        var metadata = new Metadata();
        parser.parse(resource.getInputStream(), new BodyContentHandler(), metadata);

        var audioMetadata = new AudioMetadata();
        audioMetadata.setFileId(fileId);
        audioMetadata.setName(metadata.get(AudioMetadataFields.NAME.getCode()));
        audioMetadata.setAlbum(metadata.get(AudioMetadataFields.ALBUM.getCode()));
        audioMetadata.setArtist(metadata.get(AudioMetadataFields.ARTIST.getCode()));
        audioMetadata.setReleaseDate(metadata.get(AudioMetadataFields.RELEASE_DATE.getCode()));
        return audioMetadata;
    }

    private boolean isAudioFile(final InputStream inputStream) throws IOException {
        var metadata = new Metadata();
        MediaType mediaType = detector.detect(inputStream, metadata);
        return audioType.equals(mediaType.getType());
    }
}
