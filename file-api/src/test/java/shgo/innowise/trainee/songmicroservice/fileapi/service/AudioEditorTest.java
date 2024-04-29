package shgo.innowise.trainee.songmicroservice.fileapi.service;

import org.bson.assertions.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

import java.io.IOException;

public class AudioEditorTest {

    private final AudioEditor audioEditor = new AudioEditor();

    @Test
    public void givenAudioResource_whenCutAudio_thenReturnCutAudioResource() throws IOException {
        final String filename = "testAudio.mp3";

        InputStreamResource audioFile = new InputStreamResource(getClass().getResourceAsStream("/" + filename));
        Resource cutAudio = audioEditor.cutAudioFile(audioFile, filename, 10, 10);

        Assertions.assertNotNull(cutAudio);
        Assertions.assertNotNull(cutAudio.getContentAsByteArray());
    }

}
