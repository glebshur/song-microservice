package shgo.innowise.trainee.songmicroservice.enricherservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum with code of file's metadata.
 */
@AllArgsConstructor
@Getter
public enum AudioMetadataFields {
    NAME("dc:title"),
    ALBUM("xmpDM:album"),
    ARTIST("xmpDM:artist"),
    RELEASE_DATE("xmpDM:releaseDate");
    private final String code;
}
