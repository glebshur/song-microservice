package shgo.innowise.trainee.songmicroservice.fileapi.service;

import io.awspring.cloud.sqs.operations.SendResult;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import shgo.innowise.trainee.songmicroservice.fileapi.entity.SongData;

/**
 * Provides message sending to AWS SQS.
 */
@Service
public class SqsProvider {

    private SqsTemplate sqsTemplate;
    @Value("${queue-name:file-enricher-queue}")
    private String queueName;

    @Autowired
    public SqsProvider(SqsTemplate sqsTemplate) {
        this.sqsTemplate = sqsTemplate;
    }

    /**
     * Sends song data to SQS.
     *
     * @param songData song data to send
     * @return send result
     */
    public SendResult<String> sendSongData(final SongData songData) {
        return sqsTemplate.send(queueName, songData.getId().toString());
    }
}
