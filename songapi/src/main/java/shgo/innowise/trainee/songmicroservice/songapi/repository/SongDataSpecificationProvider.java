package shgo.innowise.trainee.songmicroservice.songapi.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import shgo.innowise.trainee.songmicroservice.songapi.controller.request.SongDataRequest;
import shgo.innowise.trainee.songmicroservice.songapi.entity.SongData;
import shgo.innowise.trainee.songmicroservice.songapi.entity.SongData_;

/**
 * Provides specifications for searching requests on song data.
 */
public class SongDataSpecificationProvider {

    /**
     * Creates specification for searching by name, artist, album and user id.
     * All parameters are optional.
     *
     * @param request search request
     * @return specification
     */
    public static Specification<SongData> searchByRequest(SongDataRequest request) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.and(
                        fieldContains(criteriaBuilder, root.get(SongData_.NAME), request.getName()),
                        fieldContains(criteriaBuilder, root.get(SongData_.ARTIST), request.getArtist()),
                        fieldContains(criteriaBuilder, root.get(SongData_.ALBUM), request.getAlbum()),
                        fieldEquals(criteriaBuilder, root.get(SongData_.USER_ID), request.getUserId())
                );
    }

    private static Predicate fieldContains(CriteriaBuilder criteriaBuilder,
                                           Expression<String> expression,
                                           String searchTerm) {
        if (searchTerm != null && !searchTerm.isEmpty()) {
            return criteriaBuilder.like(criteriaBuilder.lower(expression),
                    "%" + searchTerm.toLowerCase() + "%");
        }
        return criteriaBuilder.conjunction(); // always true
    }

    private static Predicate fieldEquals(CriteriaBuilder criteriaBuilder,
                                         Expression<String> expression,
                                         String searchTerm) {
        if (searchTerm != null && !searchTerm.isEmpty()) {
            return criteriaBuilder.equal(criteriaBuilder.lower(expression), searchTerm);
        }
        return criteriaBuilder.conjunction(); // always true
    }
}
