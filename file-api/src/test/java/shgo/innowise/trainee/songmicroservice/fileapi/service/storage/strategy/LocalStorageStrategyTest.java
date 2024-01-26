package shgo.innowise.trainee.songmicroservice.fileapi.service.storage.strategy;

import jakarta.ws.rs.core.MediaType;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import shgo.innowise.trainee.songmicroservice.fileapi.entity.SongData;
import shgo.innowise.trainee.songmicroservice.fileapi.service.storage.StorageStrategyRegistry;

import java.io.File;

@ExtendWith(MockitoExtension.class)
public class LocalStorageStrategyTest {

    @Mock
    StorageStrategyRegistry storageStrategyRegistry;
    @InjectMocks
    LocalStorageStrategy localStorageStrategy;

    @Test
    public void givenAudioFile_whenSaveSong_thenCreatesFile() throws Exception {
        final String fileName = "testAudio.mp3";
        MockMultipartFile multipartFile = new MockMultipartFile("file",
                fileName,
                MediaType.APPLICATION_OCTET_STREAM,
                getClass().getResourceAsStream("/" + fileName));

        SongData songData = localStorageStrategy.saveSong(multipartFile);

        Assertions.assertEquals(songData.getOriginalName(), fileName);
        Assertions.assertTrue(new File(songData.getPath()).exists());

        FileUtils.deleteQuietly(FileUtils.getFile(songData.getPath()));
    }

    @Test
    public void givenAudioFile_whenDeleteSong_thenDeleteFile() throws Exception {
        final String filePath = "/testAudio.mp3";
        final String testFilePath = "/testAudioFile.mp3";
        File file = new File(testFilePath);
        FileUtils.copyInputStreamToFile(getClass().getResourceAsStream(filePath), file);
        SongData songData = new SongData(null, null, testFilePath, null);

        localStorageStrategy.deleteSong(songData);

        Assertions.assertFalse(file.exists());
    }
}
