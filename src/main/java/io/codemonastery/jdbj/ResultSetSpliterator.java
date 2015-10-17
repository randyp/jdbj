package io.codemonastery.jdbj;

import io.codemonastery.jdbj.lambda.ResultMapper;

import javax.annotation.Nullable;
import java.sql.ResultSet;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * Wrapper around {@link ResultSet} and {@link ResultMapper} to create a {@link Spliterator} or {@link E}.
 * <p>
 * Does not close the {@link ResultSet}.
 * @param <E> element type     
 * @see StreamQuery
 */
public class ResultSetSpliterator<E> implements Spliterator<E> {

    private final SmartResultSet rs;
    private final ResultMapper<E> mapper;

    public ResultSetSpliterator(ResultSet rs, ResultMapper<E> mapper) {
        this.rs = new SmartResultSet(rs);
        this.mapper = mapper;
    }

    @Override
    public boolean tryAdvance(Consumer<? super E> action) {
        try {
            final boolean advanced = rs.next();
            if (advanced) {
                action.accept(mapper.map(rs));
            }
            return advanced;
        } catch (Exception e) {
            throw new AdvanceFailedException(e);
        }
    }

    @Override
    public @Nullable Spliterator<E> trySplit() {
        return null;
    }

    @Override
    public long estimateSize() {
        return Long.MAX_VALUE;
    }

    @Override
    public int characteristics() {
        return Spliterator.IMMUTABLE;
    }
}
