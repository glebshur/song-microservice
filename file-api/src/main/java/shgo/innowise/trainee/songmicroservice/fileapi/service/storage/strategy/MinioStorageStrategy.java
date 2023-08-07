package shgo.innowise.trainee.songmicroservice.fileapi.service.storage.strategy;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.StatObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import shgo.innowise.trainee.songmicroservice.fileapi.entity.SongData;
import shgo.innowise.trainee.songmicroservice.fileapi.entity.StorageType;
import shgo.innowise.trainee.songmicroservice.fileapi.exception.StorageException;
import shgo.innowise.trainee.songmicroservice.fileapi.service.storage.StorageStrategyRegistry;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


/**
 * Provides operations in Minio S3 storage.
 */
@Service("minio-storage")
@Slf4j
public class MinioStorageStrategy implements StorageStrategy {

    @Value("${minio.bucket-name:my-bucket}")
    private String bucketName;
    private final MinioClient minioClient;
    final int fileNameLength = 8;

    @Autowired
    public MinioStorageStrategy(MinioClient minioClient,
                                StorageStrategyRegistry registry) {
        this.minioClient = minioClient;
        registry.register(StorageType.MINIO, this);
    }

    /**
     * Saves song to minio S3 storage.
     *
     * @param song audio file to save
     * @return data of saved file
     * @throws IOException exception by saving file
     */
    @Override
    public SongData saveSong(final MultipartFile song) throws IOException {
        String key = generateKey(FilenameUtils.getExtension(song.getOriginalFilename()));

        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(key)
                    .stream(song.getInputStream(), song.getSize(), -1)
                    .contentType(song.getContentType())
                    .build());
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException e) {
            throw new StorageException(e.getMessage());
        }

        log.debug(song.getOriginalFilename() + " was saved like " + key);
        return new SongData(song.getOriginalFilename(), StorageType.MINIO, key, bucketName);
    }

    /**
     * Gets song from minio s3 storage.
     *
     * @param songData song to download.
     * @return song file.
     */
    @Override
    public Resource getSong(final SongData songData) {
        try {
            return new InputStreamResource(minioClient.getObject(GetObjectArgs.builder()
                    .bucket(songData.getBucketName())
                    .object(songData.getPath())
                    .build()));
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            throw new StorageException(e.getMessage());
        }
    }

    /**
     * Deletes song from minio s3 storage.
     *
     * @param songData song to delete
     */
    @Override
    public void deleteSong(final SongData songData) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(songData.getBucketName())
                    .object(songData.getPath())
                    .build());
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            throw new StorageException(e.getMessage());
        }
    }

    /**
     * Generates key for object.
     *
     * @param extension file extension
     * @return generated key
     */
    private String generateKey(final String extension) {
        String name = RandomStringUtils.randomAlphanumeric(fileNameLength) + "." + extension;

        while (isObjectExisting(name)) {
            name = RandomStringUtils.randomAlphanumeric(fileNameLength) + "." + extension;
        }
        return name;
    }

    /**
     * Checks existence of an object.
     *
     * @param objectName object name
     * @return true, if exists; false, if doesn't
     */
    private boolean isObjectExisting(final String objectName) {
        try {
            minioClient.statObject(StatObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName).build());
            return true;
        } catch (ErrorResponseException e) {
            return false;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new StorageException(e.getMessage());
        }
    }
}