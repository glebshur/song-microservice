package shgo.innowise.trainee.songmicroservice.songapi.route.consumer;

import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import shgo.innowise.trainee.songmicroservice.songapi.dto.SongDataDto;
import shgo.innowise.trainee.songmicroservice.songapi.mapper.SongDataDtoMapper;
import shgo.innowise.trainee.songmicroservice.songapi.service.SongDataService;

/**
 * Camel route that consumes song data from rabbitmq.
 */
@Component("rabbitmq-consumer")
public class RabbitmqConsumerRoute extends EndpointRouteBuilder {

    private final SongDataService songDataService;
    private final SongDataDtoMapper songDataDtoMapper;

    @Autowired
    public RabbitmqConsumerRoute(SongDataService songDataService,
                            SongDataDtoMapper songDataDtoMapper) {
        this.songDataService = songDataService;
        this.songDataDtoMapper = songDataDtoMapper;
    }
    @Override
    public void configure() {
        from(springRabbitmq("{{message-brokers.rabbitmq.exchange}}")
                .queues("{{message-brokers.rabbitmq.consumer-queue}}")
                .routingKey("{{message-brokers.rabbitmq.consumer-queue}}"))
                .unmarshal().json(JsonLibrary.Jackson, SongDataDto.class)
                .process(exchange -> {
                    SongDataDto body = exchange.getMessage().getBody(SongDataDto.class);
                    songDataService.createSongData(songDataDtoMapper.songDataDtoToSongData(body));
                });
    }
}
