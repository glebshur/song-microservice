package shgo.innowise.trainee.songmicroservice.fileapi.service;

import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.server.ResponseStatusException;
import shgo.innowise.trainee.songmicroservice.fileapi.client.SongApiClient;
import shgo.innowise.trainee.songmicroservice.fileapi.entity.Metadata;
import shgo.innowise.trainee.songmicroservice.fileapi.entity.SongData;
import shgo.innowise.trainee.songmicroservice.fileapi.entity.SongResponse;
import shgo.innowise.trainee.songmicroservice.fileapi.entity.StorageType;
import shgo.innowise.trainee.songmicroservice.fileapi.repository.MetadataRepository;
import shgo.innowise.trainee.songmicroservice.fileapi.repository.SongDataRepository;
import shgo.innowise.trainee.songmicroservice.fileapi.service.messagesender.strategy.SenderStrategy;
import shgo.innowise.trainee.songmicroservice.fileapi.service.storage.StorageStrategyRegistry;
import shgo.innowise.trainee.songmicroservice.fileapi.service.storage.strategy.LocalStorageStrategy;
import shgo.innowise.trainee.songmicroservice.fileapi.service.storage.strategy.StorageStrategy;
import software.amazon.awssdk.core.exception.SdkClientException;

import java.security.Principal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SongServiceTest {

    @Mock
    private StorageStrategy mainStorage;
    @Mock
    private LocalStorageStrategy localStorageStrategy;
    @Mock
    private SongDataRepository songDataRepository;
    @Mock
    private MetadataRepository metadataRepository;
    @Mock
    private MetadataProvider metadataProvider;
    @Mock
    private SenderStrategy mainSender;
    @Mock
    private SongApiClient songApiClient;
    @Mock
    private StorageStrategyRegistry storageStrategyRegistry;
    @Mock
    private Principal principal;

    @InjectMocks
    SongService songService;

    @Test
    public void givenTextFile_whenUploadSong_thenThrowException() throws Exception {
        final String fileName = "test.txt";
        MockMultipartFile multipartFile = new MockMultipartFile("file",
                fileName,
                MediaType.TEXT_PLAIN,
                getClass().getResourceAsStream("/" + fileName));

        Assertions.assertThrows(ResponseStatusException.class,
                () -> songService.uploadSong(multipartFile, null));
    }

    @Test
    public void givenExistedAudioFile_whenUploadSong_thenThrowException() throws Exception {
        final String fileName = "testAudio.mp3";
        MockMultipartFile multipartFile = new MockMultipartFile("file.mp3",
                fileName,
                "audio",
                getClass().getResourceAsStream("/" + fileName));
        when(songDataRepository.existsByHash(anyString())).thenReturn(true);

        Assertions.assertThrows(ResponseStatusException.class,
                () -> songService.uploadSong(multipartFile, null));
    }

    @Test
    public void givenAudioFile_whenUploadSong_thenReturnSongData() throws Exception {
        final String fileName = "testAudio.mp3";
        MockMultipartFile multipartFile = new MockMultipartFile("file.mp3",
                fileName,
                "audio",
                getClass().getResourceAsStream("/" + fileName));
        when(songDataRepository.existsByHash(anyString())).thenReturn(false);
        SongData songData = new SongData("test", StorageType.LOCAL, "/testAudio.mp3", null);
        songData.setId(1L);
        when(mainStorage.saveSong(any())).thenReturn(songData);
        when(songDataRepository.save(any())).thenReturn(songData);
        Metadata metadata = new Metadata();
        metadata.setFileId(1L);
        when(metadataProvider.getMetadata(any(), any())).thenReturn(metadata);
        when(principal.getName()).thenReturn("test");
        when(metadataRepository.save(any())).thenReturn(metadata);

        SongData result = songService.uploadSong(multipartFile, principal);

        Assertions.assertNotNull(result);
    }

    @Test
    public void givenIdNotExists_whenDownloadSong_thenThrowException() {
        when(songDataRepository.findById(any())).thenReturn(Optional.empty());

        Assertions.assertThrows(ResponseStatusException.class,
                () -> songService.downloadSong(1L));
    }

    @Test
    public void givenResourceNotExists_whenDownloadSong_thenThrowException() {
        SongData songData = new SongData("test", StorageType.LOCAL, "/testAudio.mp3", null);
        songData.setId(1L);
        when(songDataRepository.findById(any())).thenReturn(Optional.of(songData));
        when(storageStrategyRegistry.getStorageStrategy(any())).thenReturn(localStorageStrategy);
        Resource resource = mock(Resource.class);
        when(localStorageStrategy.getSong(any())).thenReturn(resource);
        when(resource.exists()).thenReturn(false);

        Assertions.assertThrows(ResponseStatusException.class,
                () -> songService.downloadSong(1L));
    }

    @Test
    public void givenStorageThrowsException_whenDownloadSong_thenThrowException() {
        SongData songData = new SongData("test", StorageType.LOCAL, "/testAudio.mp3", null);
        songData.setId(1L);
        when(songDataRepository.findById(any())).thenReturn(Optional.of(songData));
        when(storageStrategyRegistry.getStorageStrategy(any())).thenReturn(localStorageStrategy);
        when(localStorageStrategy.getSong(any())).thenThrow(SdkClientException.class);

        Assertions.assertThrows(ResponseStatusException.class,
                () -> songService.downloadSong(1L));
    }

    @Test
    public void givenSongId_whenDownloadSong_thenSongResponse() {
        SongData songData = new SongData("test", StorageType.LOCAL, "/testAudio.mp3", null);
        songData.setId(1L);
        when(songDataRepository.findById(any())).thenReturn(Optional.of(songData));
        when(storageStrategyRegistry.getStorageStrategy(any())).thenReturn(localStorageStrategy);
        Resource resource = mock(Resource.class);
        when(localStorageStrategy.getSong(any())).thenReturn(resource);
        when(resource.exists()).thenReturn(true);

        SongResponse songResponse = songService.downloadSong(1L);

        Assertions.assertNotNull(songResponse);
    }
}
