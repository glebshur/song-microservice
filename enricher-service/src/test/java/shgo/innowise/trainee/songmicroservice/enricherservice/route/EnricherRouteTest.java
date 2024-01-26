package shgo.innowise.trainee.songmicroservice.enricherservice.route;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import shgo.innowise.trainee.songmicroservice.enricherservice.entity.AudioMetadata;
import shgo.innowise.trainee.songmicroservice.enricherservice.service.SpotifyTokenProvider;

import static org.apache.camel.builder.AdviceWith.adviceWith;

public class EnricherRouteTest extends CamelTestSupport {

    @Override
    protected RoutesBuilder createRouteBuilder() {
        SpotifyTokenProvider spotifyTokenProvider = new SpotifyTokenProvider();
        ReflectionTestUtils.setField(spotifyTokenProvider, "url", "https://accounts.spotify.com/api/token");
        ReflectionTestUtils.setField(spotifyTokenProvider, "clientId", "4a67fa16eb1943e581d4853135ad4099");
        ReflectionTestUtils.setField(spotifyTokenProvider, "clientSecret", "69d6db9055c64ed88c3fbafcb84227d5");
        return new EnricherRoute(spotifyTokenProvider);
    }

    @Override
    public boolean isUseAdviceWith() {
        return true;
    }

    @Test
    public void givenInsufficientMetadata_whenEnriches_thenFillsWithDefaultValues() throws Exception {
        AudioMetadata audioMetadata = new AudioMetadata();
        audioMetadata.setName(null);
        audioMetadata.setArtist(null);
        // mock 'to' endpoint
        RouteDefinition route = context.getRouteDefinitions().get(0);
        adviceWith(route, context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                weaveByToUri("direct://" + RouteNames.MESSAGE_PRODUCER.getRouteName())
                        .replace().to("mock:test");
            }
        });
        context.start();

        MockEndpoint mockEndpoint = getMockEndpoint("mock:test");
        mockEndpoint.expectedMessageCount(1);

        template.sendBody("direct:" + RouteNames.SPOTIFY_ENRICHER.getRouteName(), audioMetadata);

        mockEndpoint.assertIsSatisfied();
        AudioMetadata resultMetadata = mockEndpoint.getExchanges().get(0).getIn().getBody(AudioMetadata.class);
        String defaultValue = "Unknown";
        Assertions.assertEquals(resultMetadata.getName(), defaultValue);
        Assertions.assertEquals(resultMetadata.getArtist(), defaultValue);

    }

    @Test
    public void givenSufficientMetadata_whenEnriches_thenReturnEnrichedMetadata() throws Exception {
        AudioMetadata audioMetadata = new AudioMetadata();
        audioMetadata.setName("Ich Will");
        audioMetadata.setArtist("Rammstein");
        // mock 'to' endpoint
        RouteDefinition route = context.getRouteDefinitions().get(0);
        adviceWith(route, context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                weaveByToUri("direct://" + RouteNames.MESSAGE_PRODUCER.getRouteName())
                        .replace().to("mock:test");
            }
        });
        context.start();

        MockEndpoint mockEndpoint = getMockEndpoint("mock:test");
        mockEndpoint.expectedMessageCount(1);

        template.sendBody("direct:" + RouteNames.SPOTIFY_ENRICHER.getRouteName(), audioMetadata);

        mockEndpoint.assertIsSatisfied();
        AudioMetadata resultMetadata = mockEndpoint.getExchanges().get(0).getIn().getBody(AudioMetadata.class);
        Assertions.assertNotNull(resultMetadata.getDuration());
        Assertions.assertNotNull(resultMetadata.getAlbum());
        Assertions.assertNotNull(resultMetadata.getReleaseDate());
    }
}
