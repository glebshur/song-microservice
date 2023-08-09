package shgo.innowise.trainee.songmicroservice.fileapi.service.messagesender.strategy;

import shgo.innowise.trainee.songmicroservice.fileapi.entity.SongData;

/**
 * Interface for message broker senders.
 */
public interface SenderStrategy {

    /**
     * Sends specified song data to queue.
     *
     * @param songData song data to send
     */
    public void sendSongData(SongData songData);
}
