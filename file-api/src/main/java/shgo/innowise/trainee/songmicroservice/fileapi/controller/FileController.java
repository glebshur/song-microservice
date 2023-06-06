package shgo.innowise.trainee.songmicroservice.fileapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import shgo.innowise.trainee.songmicroservice.fileapi.entity.SongData;
import shgo.innowise.trainee.songmicroservice.fileapi.service.SongService;

import java.io.IOException;
import java.util.List;

/**
 * Main File API controller.
 */
@Controller
public class FileController {

    private SongService songService;

    @Autowired
    public FileController(SongService songService) {
        this.songService = songService;
    }

    /**
     * Audio file upload endpoint.
     *
     * @param song audio file to upload
     * @return response with success message
     * @throws IOException file saving error
     */
    @PostMapping("/files/upload")
    public ResponseEntity<Object> uploadFile(@RequestParam("song") MultipartFile song) throws IOException {
        songService.uploadSong(song);

        return new ResponseEntity<>("File saved successfully", HttpStatus.CREATED);
    }

    /**
     * Audio file download endpoint.
     * @param id id of audio file
     * @return audio file
     */
    @GetMapping(value = "/files/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody Resource downloadFile(@PathVariable("id") Long id){
        return songService.downloadSong(id);
    }
}
