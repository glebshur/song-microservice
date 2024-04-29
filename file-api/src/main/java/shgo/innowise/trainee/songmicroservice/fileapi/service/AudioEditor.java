package shgo.innowise.trainee.songmicroservice.fileapi.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

/**
 * Editor for audio files.
 */
@Service
@Slf4j
public class AudioEditor {

    @Value("${local-base-path:temp-files/songs}")
    private String localBasePath;

    /**
     * Cuts part from audio file.
     *
     * @param audioFile audio file to cut
     * @param filename  name of the audio file
     * @param startTime start time of part to cut in seconds
     * @param endTime   end time of part to cut in seconds
     * @return resource with cuted part
     */
    public Resource cutAudioFile(final Resource audioFile,
                                 final String filename,
                                 final float startTime,
                                 final float endTime) {
        final float offset = Float.min(startTime, endTime);
        final float duration = Math.abs(endTime - startTime);
        final String extension = FilenameUtils.getExtension(filename);

        try {
            AudioAttributes audioAttributes = new AudioAttributes();
            audioAttributes.setBitRate(128000);
            audioAttributes.setChannels(2);
            audioAttributes.setSamplingRate(44100);

            EncodingAttributes encodingAttributes = new EncodingAttributes();
            encodingAttributes.setInputFormat(extension);
            encodingAttributes.setOutputFormat(extension);
            encodingAttributes.setAudioAttributes(audioAttributes);
            encodingAttributes.setOffset(offset);
            encodingAttributes.setDuration(duration);

            File tempFile = File.createTempFile("temp", "." + extension);
            try (OutputStream output = new FileOutputStream(tempFile)) {
                IOUtils.copy(audioFile.getInputStream(), output);
            }

            MultimediaObject source = new MultimediaObject(tempFile);
            File outputFile = new File(localBasePath + "/output." + extension);

            Encoder encoder = new Encoder();
            encoder.encode(source, outputFile, encodingAttributes);

            Resource resource = new ByteArrayResource(Files.readAllBytes(outputFile.toPath()));
            outputFile.delete();
            tempFile.delete();
            return resource;
        } catch (IOException | EncoderException e) {
            log.error("Error during audio cutting: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
