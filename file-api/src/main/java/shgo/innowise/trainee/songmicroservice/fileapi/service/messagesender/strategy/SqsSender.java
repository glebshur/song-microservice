package shgo.innowise.trainee.songmicroservice.fileapi.service.messagesender.strategy;

import io.awspring.cloud.sqs.operations.MessagingOperationFailedException;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import shgo.innowise.trainee.songmicroservice.fileapi.entity.SongData;
import shgo.innowise.trainee.songmicroservice.fileapi.exception.SenderException;

/**
 * Provides message sending to AWS SQS.
 */
@Service("sqs-sender")
@Slf4j
public class SqsSender implements SenderStrategy {

    private final SqsTemplate sqsTemplate;
    @Value("${aws.sqs.queue-name:file-enricher-queue}")
    private String queueName;

    @Autowired
    public SqsSender(SqsTemplate sqsTemplate) {
        this.sqsTemplate = sqsTemplate;
    }

    /**
     * Sends song id to SQS.
     *
     * @param songData song data to send
     * @return send result
     */
    @Override
    public void sendSongData(final SongData songData) {
        try {
            sqsTemplate.send(queueName, songData.getId().toString());
        } catch (MessagingOperationFailedException ex) {
            log.error(ex.getMessage());
            throw new SenderException(ex.getMessage());
        }
    }
}
