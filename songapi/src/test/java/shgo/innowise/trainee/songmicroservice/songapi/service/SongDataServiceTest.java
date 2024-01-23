package shgo.innowise.trainee.songmicroservice.songapi.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import shgo.innowise.trainee.songmicroservice.songapi.client.FileApiClient;
import shgo.innowise.trainee.songmicroservice.songapi.dto.SongDataDto;
import shgo.innowise.trainee.songmicroservice.songapi.entity.SongData;
import shgo.innowise.trainee.songmicroservice.songapi.repository.SongDataRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SongDataServiceTest {

    @Mock
    private SongDataRepository songDataRepository;
    @Mock
    private FileApiClient fileApiClient;

    @InjectMocks
    SongDataService songDataService;

    @Test
    public void givenIdNotExists_whenUpdateSongData_thenThrowsException(){
        when(songDataRepository.findById(any())).thenReturn(Optional.empty());

        Assertions.assertThrows(ResponseStatusException.class,
                () -> songDataService.updateSongData(1L, new SongDataDto()));
    }

    @Test
    public void givenIdExists_whenUpdateSongData_thenSaveNewData() {
        SongData songData = new SongData();
        when(songDataRepository.findById(any())).thenReturn(Optional.of(songData));

        songDataService.updateSongData(1L, new SongDataDto());

        verify(songDataRepository, times(1)).save(songData);
    }

    @Test
    public void givenIdNotExists_whenDeleteSongData_thenDeletionIsNotCalled() {
        when(songDataRepository.findById(any())).thenReturn(Optional.empty());

        songDataService.deleteSongData(1L);

        verify(songDataRepository, times(0)).deleteById(any());
        verify(fileApiClient, times(0)).deleteFile(any());
    }

    @Test
    public void givenIdExists_whenDeleteSongData_thenDeletionIsCalled() {
        SongData songData = new SongData();
        final long id = 1L;
        songData.setId(id);
        songData.setFileId(id);
        when(songDataRepository.findById(any())).thenReturn(Optional.of(songData));

        songDataService.deleteSongData(id);

        verify(songDataRepository, times(1)).deleteById(id);
        verify(fileApiClient, times(1)).deleteFile(id);
    }

    @Test
    public void givenFileIdNotExists_whenDeleteSongDataByFileId_thenDeletionIsNotCalled() {
        when(songDataRepository.findByFileId(any())).thenReturn(Optional.empty());

        songDataService.deleteSongDataByFileId(1L);

        verify(songDataRepository, times(0)).deleteById(any());
        verify(fileApiClient, times(0)).deleteFile(any());
    }

    @Test
    public void givenFileIdExists_whenDeleteSongDataByFileId_thenDeletionIsCalled() {
        SongData songData = new SongData();
        final long id = 1L;
        songData.setId(id);
        songData.setFileId(id);
        when(songDataRepository.findByFileId(any())).thenReturn(Optional.of(songData));

        songDataService.deleteSongDataByFileId(id);

        verify(songDataRepository, times(1)).deleteById(id);
        verify(fileApiClient, times(1)).deleteFile(id);
    }
}
