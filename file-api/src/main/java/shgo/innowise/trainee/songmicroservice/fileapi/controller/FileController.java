package shgo.innowise.trainee.songmicroservice.fileapi.controller;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;
import shgo.innowise.trainee.songmicroservice.fileapi.entity.SongResponse;
import shgo.innowise.trainee.songmicroservice.fileapi.service.SongService;

import java.io.IOException;

/**
 * Main File API controller.
 */
@Controller
@RequestMapping("/files")
public class FileController {

    private final SongService songService;
    private final Tika tika;

    @Autowired
    public FileController(SongService songService) {
        this.songService = songService;
        tika = new Tika();
    }

    /**
     * Audio file upload endpoint.
     *
     * @param song audio file to upload
     * @return response with success message
     * @throws IOException   file saving error
     * @throws TikaException exception by parsing file
     * @throws SAXException  exception by parsing metadata
     */
    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> uploadFile(final @RequestParam("song") MultipartFile song) throws IOException, TikaException, SAXException {
        songService.uploadSong(song);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Audio file download endpoint.
     *
     * @param id id of audio file
     * @return audio file
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Resource> downloadFile(final @PathVariable("id") Long id) {
        SongResponse songResponse = songService.downloadSong(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(tika.detect(songResponse.getFilename())))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""
                        + songResponse.getFilename() + "\"")
                .body(songResponse.getSong());
    }

    /**
     * Audio file delete endpoint.
     *
     * @param id audio file id
     * @return response of success
     */
    @DeleteMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deleteFile(final @PathVariable("id") Long id) {
        songService.deleteSong(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
