package shgo.innowise.trainee.songmicroservice.fileapi.service.strategy;

import io.awspring.cloud.s3.S3Template;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import shgo.innowise.trainee.songmicroservice.fileapi.entity.SongData;
import shgo.innowise.trainee.songmicroservice.fileapi.entity.StorageType;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

import java.io.IOException;

/**
 * Provides file operations in AWS S3 storage.
 */
@Service
@Slf4j
public class S3StorageStrategy implements StorageStrategy {

    @Value("${bucket-name:file-bucket}")
    private String bucketName;
    private S3Template s3Template;
    private S3Client s3Client;

    @Autowired
    public S3StorageStrategy(S3Template s3Template, S3Client s3Client) {
        this.s3Template = s3Template;
        this.s3Client = s3Client;
    }

    /**
     * Saves song file in AWS S3 storage.
     *
     * @param song song file
     * @return song data
     * @throws IOException error by file reading
     */
    @Override
    @CircuitBreaker(name = "s3StorageBreaker")
    public SongData saveSong(final MultipartFile song) throws IOException {
        String key = generateKey(FilenameUtils.getExtension(song.getOriginalFilename()));
        s3Template.upload(bucketName, key, song.getInputStream());

        log.debug(song.getOriginalFilename() + " was saved like " + key);
        return new SongData(song.getOriginalFilename(), StorageType.S3, key, bucketName);
    }

    /**
     * Downloads song from AWS S3 storage.
     *
     * @param songData song data
     * @return song file
     */
    @Override
    public Resource getSong(final SongData songData) {
        return s3Template.download(songData.getBucketName(), songData.getPath());
    }

    /**
     * Deletes song from AWS S3 storage.
     *
     * @param songData song to delete
     */
    @Override
    public void deleteSong(SongData songData) {
        s3Template.deleteObject(songData.getBucketName(), songData.getPath());
    }

    /**
     * Generates key for S3 object.
     *
     * @param extension file extension
     * @return generated key
     */
    private String generateKey(String extension) {
        final int fileNameLength = 8;
        String name = RandomStringUtils.randomAlphanumeric(fileNameLength) + "." + extension;

        while (exists(name)) {
            name = RandomStringUtils.random(fileNameLength) + "." + extension;
        }
        return name;
    }

    /**
     * Checks existence of an object.
     *
     * @param objectName object name
     * @return true, if exists; false, if doesn't exits
     */
    private boolean exists(final String objectName) {
        HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                .bucket(bucketName)
                .key(objectName)
                .build();

        try {
            s3Client.headObject(headObjectRequest);
            return true;
        } catch (NoSuchKeyException ex) {
            log.debug(ex.getMessage());
            return false;
        }
    }
}
