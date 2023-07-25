package shgo.innowise.trainee.songmicroservice.songapi.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Class for requests to database with limit and offset.
 */
public class OffsetLimitPageRequest implements Pageable {
    private final int limit;
    private final long offset;

    private final Sort sort = Sort.unsorted();

    public OffsetLimitPageRequest(int limit, long offset) {
        if (limit < 1) {
            throw new IllegalArgumentException("Limit cannot be less than 1");
        }
        if (offset < 0) {
            throw new IllegalArgumentException("Offset cannot be less than 0");
        }

        this.limit = limit;
        this.offset = offset;
    }

    @Override
    public int getPageNumber() {
        return (int) (offset / limit);
    }

    @Override
    public int getPageSize() {
        return limit;
    }

    @Override
    public long getOffset() {
        return offset;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public Pageable next() {
        return new OffsetLimitPageRequest(getPageSize(), getOffset() + getPageSize());
    }

    public Pageable previous() {
        return hasPrevious() ? new OffsetLimitPageRequest(getPageSize(), getOffset() - getPageSize()) : this;
    }

    @Override
    public Pageable previousOrFirst() {
        return hasPrevious() ? previous() : first();
    }

    @Override
    public Pageable first() {
        return new OffsetLimitPageRequest(getPageSize(), 0);
    }

    @Override
    public Pageable withPage(int pageNumber) {
        return new OffsetLimitPageRequest(getPageSize(), (long) pageNumber * getPageSize());
    }

    @Override
    public boolean hasPrevious() {
        return offset > limit;
    }
}
