package shgo.innowise.trainee.songmicroservice.fileapi.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.exception.TikaException;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import shgo.innowise.trainee.songmicroservice.fileapi.entity.Metadata;
import shgo.innowise.trainee.songmicroservice.fileapi.entity.MetadataFields;

import java.io.IOException;

/**
 * Retrieves metadata from audio files.
 */
@Service
@Slf4j
public class MetadataProvider {

    private final AutoDetectParser parser;
    private final DefaultDetector detector;

    @Autowired
    public MetadataProvider() {
        parser = new AutoDetectParser();
        detector = new DefaultDetector();
    }

    /**
     * Gets metadata of file.
     *
     * @param fileId    file id
     * @param audioFile file
     * @return file metadata
     * @throws IOException   exception by working with file
     * @throws TikaException exception by parsing file
     * @throws SAXException  exception by parsing metadata
     */
    public Metadata getMetadata(final Long fileId, final Resource audioFile) throws IOException, TikaException, SAXException {
        var metadata = new org.apache.tika.metadata.Metadata();
        parser.parse(audioFile.getInputStream(), new BodyContentHandler(), metadata);

        var audioMetadata = new Metadata();
        audioMetadata.setFileId(fileId);
        audioMetadata.setName(metadata.get(MetadataFields.NAME.getCode()));
        audioMetadata.setAlbum(metadata.get(MetadataFields.ALBUM.getCode()));
        audioMetadata.setArtist(metadata.get(MetadataFields.ARTIST.getCode()));
        audioMetadata.setReleaseDate(metadata.get(MetadataFields.RELEASE_DATE.getCode()));
        audioMetadata.setDuration(getDuration(metadata));
        return audioMetadata;
    }

    /**
     * Retrieves duration from metadata.
     *
     * @param metadata metadata
     * @return duration in ms or null, if error occurred
     */
    private Long getDuration(org.apache.tika.metadata.Metadata metadata){
        try {
            Double duration = Double.parseDouble(metadata.get(MetadataFields.DURATION.getCode())) * 1000;
            return duration.longValue();
        } catch (NullPointerException | NumberFormatException ex) {
            log.error(ex.getMessage());
            return null;
        }
    }
}
