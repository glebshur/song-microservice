package shgo.innowise.trainee.songmicroservice.fileapi.service.messagesender.strategy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import shgo.innowise.trainee.songmicroservice.fileapi.entity.SongData;
import shgo.innowise.trainee.songmicroservice.fileapi.exception.SenderException;

/**
 * Provides message sending to RabbitMQ.
 */
@Service("rabbitmq-sender")
@Slf4j
public class RabbitmqSender implements SenderStrategy {

    @Value("${rabbitmq.queue-name:file-enricher-queue}")
    private String queueName;
    private final AmqpTemplate amqpTemplate;

    @Autowired
    public RabbitmqSender(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    /**
     * Sends song id to queue.
     *
     * @param songData song data with id to send
     */
    @Override
    public void sendSongData(SongData songData) {
        try {
            amqpTemplate.convertAndSend(queueName, songData.getId().toString());
        } catch (AmqpException ex) {
            log.error(ex.getMessage());
            throw new SenderException(ex.getMessage());
        }
    }
}
