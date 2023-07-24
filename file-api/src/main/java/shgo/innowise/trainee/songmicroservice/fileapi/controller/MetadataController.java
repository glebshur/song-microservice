package shgo.innowise.trainee.songmicroservice.fileapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import shgo.innowise.trainee.songmicroservice.fileapi.entity.Metadata;
import shgo.innowise.trainee.songmicroservice.fileapi.service.MetadataService;

/**
 * Metadata controller.
 */
@Controller
@RequestMapping("/metadata")
public class MetadataController {

    private final MetadataService metadataService;

    @Autowired
    public MetadataController(MetadataService metadataService) {
        this.metadataService = metadataService;
    }

    /**
     * Endpoint that returns metadata with specified id.
     *
     * @param id song id
     * @return song metadata
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Metadata> getMetadata(final @PathVariable("id") Long id){
        return ResponseEntity.ok(metadataService.getMetadata(id));
    }
}