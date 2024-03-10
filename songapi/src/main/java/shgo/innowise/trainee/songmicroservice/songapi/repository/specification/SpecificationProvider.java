package shgo.innowise.trainee.songmicroservice.songapi.repository.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

/**
 * Contains utils methods for specification providers.
 */
public abstract class SpecificationProvider {

    /**
     * Creates contain predicate.
     *
     * @param criteriaBuilder criteria builder
     * @param expression field name
     * @param searchTerm string to find
     * @return predicate, if searchTerm isn't null or empty,
     * otherwise predicate that is always true
     */
    protected static Predicate fieldContains(final CriteriaBuilder criteriaBuilder,
                                             final Expression<String> expression,
                                             final String searchTerm) {
        if (searchTerm != null && !searchTerm.isEmpty()) {
            return criteriaBuilder.like(criteriaBuilder.lower(expression),
                    "%" + searchTerm.toLowerCase() + "%");
        }
        return criteriaBuilder.conjunction(); // always true
    }

    /**
     * Creates equal predicate.
     *
     * @param criteriaBuilder criteria builder
     * @param expression field name
     * @param searchTerm string to be equal
     * @return predicate, if searchTerm isn't null or empty,
     * otherwise predicate that is always true
     */
    protected static Predicate fieldEquals(final CriteriaBuilder criteriaBuilder,
                                         final Expression<String> expression,
                                         final String searchTerm) {
        if (searchTerm != null && !searchTerm.isEmpty()) {
            return criteriaBuilder.equal(criteriaBuilder.lower(expression), searchTerm);
        }
        return criteriaBuilder.conjunction(); // always true
    }
}
