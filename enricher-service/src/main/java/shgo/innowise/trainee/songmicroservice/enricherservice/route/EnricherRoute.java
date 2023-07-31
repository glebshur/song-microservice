package shgo.innowise.trainee.songmicroservice.enricherservice.route;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import shgo.innowise.trainee.songmicroservice.enricherservice.entity.AudioMetadata;
import shgo.innowise.trainee.songmicroservice.enricherservice.entity.DatePrecision;
import shgo.innowise.trainee.songmicroservice.enricherservice.service.SpotifyTokenProvider;

import java.time.LocalDate;

/**
 * Camel route that enriches metadata by querying from Spotify.
 */
@Component
public class EnricherRoute extends EndpointRouteBuilder {

    private SpotifyTokenProvider tokenProvider;
    private ObjectMapper objectMapper;

    @Autowired
    public EnricherRoute(SpotifyTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
        objectMapper = new ObjectMapper();
    }

    @Override
    public void configure() {
        final String path = "//api.spotify.com/v1/search";
        final String enoughMetadataHeader = "enoughMetadata";
        final String authorizationHeader = "Authorization";
        final String audioMetadataHeader = "audioMetadata";

        from(direct(RouteNames.SPOTIFY_ENRICHER.getRouteName()))
                .process(exchange -> {
                    AudioMetadata metadata = exchange.getMessage().getBody(AudioMetadata.class);

                    if (metadata.getArtist() == null || metadata.getName() == null) {
                        exchange.getMessage().setHeader(enoughMetadataHeader, false);
                    } else {
                        exchange.getMessage().setHeader(enoughMetadataHeader, true);
                        String queryParam = "q=track:" + metadata.getName() +
                                "%20artist:" + metadata.getArtist();
                        String typeParam = "type=track";
                        String limitParam = "limit=1";
                        exchange.getMessage().setHeader(Exchange.HTTP_QUERY, String.join("&",
                                queryParam, typeParam, limitParam));
                    }
                    exchange.getIn().setHeader(audioMetadataHeader, metadata);
                })
                .choice()
                    .when(header(enoughMetadataHeader))
                        .setBody(simple(null))
                        .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                        .setHeader(authorizationHeader).simple("Bearer " + tokenProvider.getToken())
                        .to(https(path))
                        .process(exchange -> {
                            AudioMetadata audioMetadata = exchange.getIn().getHeader(audioMetadataHeader,
                                    AudioMetadata.class);

                            try {
                                exchange.getMessage().setHeader(audioMetadataHeader,
                                        fillMetadataWithSpotifyValue(audioMetadata,
                                                exchange.getIn().getBody(String.class)));
                            } catch (JsonProcessingException | NullPointerException ex) {
                                log.error("JSON parsing error: " + ex.getMessage());
                            }
                        })
                    .otherwise()
                        .log(LoggingLevel.ERROR, "Metadata didn't have enough data to enrich")
                .end()
                .process(exchange -> {
                    exchange.getMessage().setBody(fillMetadataWithDefaultValue(exchange.getMessage()
                                .getHeader(audioMetadataHeader, AudioMetadata.class)));
                })
                .to(direct(RouteNames.SQS_PRODUCER.getRouteName()));
    }

    private AudioMetadata fillMetadataWithDefaultValue(AudioMetadata metadata) {
        String defaultValue = "Unknown";

        metadata.setName(metadata.getName() != null ? metadata.getName() : defaultValue);
        metadata.setArtist(metadata.getArtist() != null ? metadata.getArtist() : defaultValue);

        return metadata;
    }

    private AudioMetadata fillMetadataWithSpotifyValue(AudioMetadata metadata, final String json) throws JsonProcessingException {
        JsonNode trackNode = objectMapper.readTree(json).get("tracks").get("items").get(0);
        metadata.setAlbum(trackNode.get("album").get("name").asText());
        metadata.setAlbumLink(trackNode.get("album").get("external_urls").get("spotify").asText());
        metadata.setArtistLink(trackNode.get("artists").get(0).get("external_urls").get("spotify").asText());
        metadata.setDuration(trackNode.get("duration_ms").asLong());
        metadata.setReleaseDate(getReleaseDate(trackNode));
        return metadata;
    }

    private LocalDate getReleaseDate(JsonNode trackNode) {
        LocalDate localDate = null;

        try {
            String datePrecision = trackNode.get("album").get("release_date_precision").asText();
            localDate = LocalDate.parse(trackNode.get("album").get("release_date").asText(),
                    DatePrecision.valueOfByCode(datePrecision).getFormatter());
        }
        catch (IllegalArgumentException ex){
            log.error(ex.getMessage());
        }

        return localDate;
    }
}
