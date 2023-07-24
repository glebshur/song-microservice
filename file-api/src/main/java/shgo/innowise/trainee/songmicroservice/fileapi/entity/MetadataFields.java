package shgo.innowise.trainee.songmicroservice.fileapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum with code of file's metadata.
 */
@AllArgsConstructor
@Getter
public enum MetadataFields {
    NAME("dc:title"),
    ALBUM("xmpDM:album"),
    ARTIST("xmpDM:artist"),
    RELEASE_DATE("xmpDM:releaseDate"),
    DURATION("xmpDM:duration");
    private final String code;
}
