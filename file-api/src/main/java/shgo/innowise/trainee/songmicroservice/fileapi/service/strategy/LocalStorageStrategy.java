package shgo.innowise.trainee.songmicroservice.fileapi.service.strategy;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import shgo.innowise.trainee.songmicroservice.fileapi.entity.SongData;
import shgo.innowise.trainee.songmicroservice.fileapi.entity.StorageType;

import java.io.File;
import java.io.IOException;

/**
 * Provides file operations in local storage.
 */
@Service("local-storage")
@Slf4j
public class LocalStorageStrategy implements StorageStrategy {

    private ResourceLoader resourceLoader;
    @Value("${local-base-path:temp-files/songs}")
    private String localBasePath;
    @Value("${files-per-directory:3000}")
    private int filesPerDirectory;
    private File currentDirectory;

    @Autowired
    public LocalStorageStrategy(final ResourceLoader resourceLoader,
                                StorageStrategyRegistry registry) {
        this.resourceLoader = resourceLoader;
        registry.register(StorageType.LOCAL, this);
    }

    /**
     * Saves song in local storage.
     *
     * @param song audio file
     * @return song data
     * @throws IOException file saving error
     */
    @Override
    public SongData saveSong(final MultipartFile song) throws IOException {
        File songFile = createFile(createDirectoryToSave(), song);
        FileUtils.copyInputStreamToFile(song.getInputStream(), songFile);

        log.debug(song.getOriginalFilename() + " was saved as " + songFile.getName());
        return new SongData(song.getOriginalFilename(), StorageType.LOCAL, songFile.getAbsolutePath(), null);
    }

    /**
     * Downloads song from local storage.
     *
     * @param songData song to download.
     * @return song file
     */
    @Override
    public Resource getSong(final SongData songData) {
        return resourceLoader.getResource("file:///" + songData.getPath());
    }

    /**
     * Deletes song from local storage.
     *
     * @param songData song to delete
     */
    @Override
    public void deleteSong(SongData songData) {
        File songFile = FileUtils.getFile(songData.getPath());
        FileUtils.deleteQuietly(songFile);
    }

    /**
     * Creates directory for song file.
     *
     * @return create directory
     */
    private File createDirectoryToSave() {
        final int nameLength = 8;

        if (currentDirectory == null) {
            String name = RandomStringUtils.randomAlphanumeric(nameLength);
            currentDirectory = new File(localBasePath + "/" + name);
        }

        while (!currentDirectory.mkdirs()) { // true until directory exists
            String[] directoryList = currentDirectory.list();
            if (directoryList == null || directoryList.length < filesPerDirectory) {
                return currentDirectory;
            }

            String name = RandomStringUtils.randomAlphanumeric(nameLength);
            currentDirectory = new File(localBasePath + "/" + name);
        }

        return currentDirectory;
    }

    /**
     * Create song file in local storage.
     *
     * @param directory directory of song file
     * @param song      song file
     * @return created file
     * @throws IOException error by file creation
     */
    private File createFile(final File directory, final MultipartFile song) throws IOException {
        final int nameLength = 10;
        final String extension = FilenameUtils.getExtension(song.getOriginalFilename());

        String name = RandomStringUtils.randomAlphanumeric(nameLength);
        var file = new File(directory, name + "." + extension);

        while (!file.createNewFile()) { // true until file exists
            name = RandomStringUtils.randomAlphanumeric(nameLength);
            file = new File(directory, name + "." + extension);
        }

        return file;
    }


}
