package shgo.innowise.trainee.songmicroservice.fileapi.service;

import io.awspring.cloud.s3.S3Template;
import lombok.extern.slf4j.Slf4j;
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

import java.io.IOException;

/**
 * Provides file operations in AWS S3 storage.
 */
@Service
@Slf4j
public class S3StorageManager {

    @Value("${bucket-name}")
    private String bucketName;
    private S3Template s3Template;
    private S3Client s3Client;

    @Autowired
    public S3StorageManager(S3Template s3Template, S3Client s3Client) {
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
    public SongData saveSong(MultipartFile song) throws IOException {
        String key = generateKey();
        s3Template.upload(bucketName, key, song.getInputStream());

        log.info(song.getOriginalFilename() + " was saved like " + key);
        return new SongData(song.getOriginalFilename(), StorageType.S3, key, bucketName);
    }

    /**
     * Downloads song from AWS S3 storage.
     *
     * @param songData song data
     * @return song file
     */
    public Resource getSong(SongData songData) {
        return s3Template.download(songData.getBucketName(), songData.getPath());
    }

    /**
     * Generates key for S3 object.
     *
     * @return generated key
     */
    private String generateKey() {
        final int fileNameLength = 8;
        String name = RandomStringUtils.randomAlphanumeric(fileNameLength);

        while (!exists(name)){
            name = RandomStringUtils.random(fileNameLength);
        }
        return name;
    }

    /**
     * Checks existence of an object.
     *
     * @param objectName object name
     * @return true, if exists; false, if doesn't exits
     */
    private boolean exists(String objectName) {
        HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                .bucket(bucketName)
                .key(objectName)
                .build();

        HeadObjectResponse response = s3Client.headObject(headObjectRequest);
        return response.hasMetadata();
    }
}
