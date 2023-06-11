package shgo.innowise.trainee.songmicroservice.fileapi.service.strategy;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import shgo.innowise.trainee.songmicroservice.fileapi.entity.SongData;

import java.io.IOException;

/**
 * Abstraction on file operations in some storage.
 */
public interface StorageStrategy {

    /**
     * Saves song in storage.
     *
     * @param song audio file to save
     * @return song data
     * @throws IOException file saving error
     */
    SongData saveSong(final MultipartFile song) throws IOException;

    /**
     * Downloads song from storage.
     *
     * @param songData song to download.
     * @return song file
     */
    Resource getSong(final SongData songData);
}
