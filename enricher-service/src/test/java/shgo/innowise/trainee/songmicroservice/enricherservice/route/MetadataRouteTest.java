package shgo.innowise.trainee.songmicroservice.enricherservice.route;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import shgo.innowise.trainee.songmicroservice.enricherservice.entity.AudioMetadata;
import shgo.innowise.trainee.songmicroservice.enricherservice.service.DefaultMetadataProvider;

import static org.apache.camel.builder.AdviceWith.adviceWith;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class MetadataRouteTest extends CamelTestSupport {

    @Mock
    DefaultMetadataProvider metadataProvider;

    @Override
    protected RoutesBuilder createRouteBuilder() {
        return new MetadataRoute(metadataProvider);
    }

    @Override
    public boolean isUseAdviceWith() {
        return true;
    }

    @Test
    void givenRoute_whenGotId_thenSendMetadataToNextRoute() throws Exception {
        AudioMetadata audioMetadata = new AudioMetadata();
        Mockito.when(metadataProvider.getMetadata(any())).thenReturn(audioMetadata);
        // mock 'to' endpoint
        RouteDefinition route = context.getRouteDefinitions().get(0);
        adviceWith(route, context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                weaveByToUri("direct://" + RouteNames.SPOTIFY_ENRICHER.getRouteName())
                        .replace().to("mock:test");
            }
        });
        context.start();
        MockEndpoint mockEndpoint = getMockEndpoint("mock:test");

        mockEndpoint.expectedBodiesReceived(audioMetadata);
        final long id = 1L;
        template.sendBody("direct:" + RouteNames.METADATA_PROVIDER.getRouteName(), id);

        mockEndpoint.assertIsSatisfied();
    }
}