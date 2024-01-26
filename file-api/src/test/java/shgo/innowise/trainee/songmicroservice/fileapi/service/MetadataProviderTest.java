package shgo.innowise.trainee.songmicroservice.fileapi.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import shgo.innowise.trainee.songmicroservice.fileapi.entity.Metadata;

import java.time.LocalDate;

public class MetadataProviderTest {

    MetadataProvider metadataProvider = new MetadataProvider();

    @Test
    public void givenAudioFile_whenGetMetadata_thenReturnNotEmptyMetadata() throws Exception {
        final String filePath = "/testAudio.mp3";
        Resource resource = new ClassPathResource(filePath);

        Metadata metadata = metadataProvider.getMetadata(1L, resource);

        Assertions.assertEquals(metadata.getName(), "Ich Will");
        Assertions.assertEquals(metadata.getArtist(), "Rammstein");
        Assertions.assertEquals(metadata.getAlbum(), "Mutter");
        Assertions.assertEquals(metadata.getDuration(), 216202L);
        Assertions.assertEquals(metadata.getReleaseDate(), LocalDate.of(2001, 1, 1));
    }
}
