package shgo.innowise.trainee.songmicroservice.songapi.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import shgo.innowise.trainee.songmicroservice.songapi.controller.request.PlaylistRequest;
import shgo.innowise.trainee.songmicroservice.songapi.entity.Playlist;
import shgo.innowise.trainee.songmicroservice.songapi.entity.Playlist_;


/**
 * Provides specifications for searching requests on playlists.
 */
public class PlaylistSpecificationProvider extends SpecificationProvider {

    /**
     * Creates specification for searching by name and user id.
     * All parameters are optional.
     *
     * @param request search request
     * @return specification
     */
    public static Specification<Playlist> searchByRequest(final PlaylistRequest request) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.and(
                        fieldContains(criteriaBuilder, root.get(Playlist_.NAME), request.getName()),
                        fieldEquals(criteriaBuilder, root.get(Playlist_.USER_ID), request.getUserId()),
                        fieldEquals(criteriaBuilder, root.get(Playlist_.PERSONAL), request.getPersonal())
                );
    }
}
