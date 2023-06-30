package shgo.innowise.trainee.songmicroservice.songapi.route;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import shgo.innowise.trainee.songmicroservice.songapi.dto.SongDataDto;
import shgo.innowise.trainee.songmicroservice.songapi.mapper.SongDataDtoMapper;
import shgo.innowise.trainee.songmicroservice.songapi.service.SongDataService;

/**
 * Camel route that consumes song data from aws sqs.
 */
@Component
@Slf4j
public class SqsConsumerRoute extends EndpointRouteBuilder {

    private SongDataService songDataService;
    private SongDataDtoMapper songDataDtoMapper;

    @Autowired
    public SqsConsumerRoute(SongDataService songDataService,
                            SongDataDtoMapper songDataDtoMapper) {
        this.songDataService = songDataService;
        this.songDataDtoMapper = songDataDtoMapper;
    }

    @Override
    public void configure() {
        from(aws2Sqs("{{consumer-queue}}").deleteAfterRead(true).delay("{{delay:1000}}"))
                .unmarshal().json(JsonLibrary.Jackson, SongDataDto.class)
                .process(exchange -> {
                    SongDataDto body = exchange.getMessage().getBody(SongDataDto.class);
                    songDataService.createSongData(songDataDtoMapper.songDataDtoToSongData(body));
                });
    }
}
