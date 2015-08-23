package com.github.randyp.jdbj;

import com.github.randyp.jdbj.lambda.ResultMapper;

import javax.annotation.Nullable;
import java.sql.ResultSet;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * Wrapper around {@link ResultSet} and {@link ResultMapper} to create a {@link Spliterator} or {@link R}.
 * <p>
 * Does not close the {@link ResultSet}.
 * @param <R>
 * @see StreamQuery
 */
public class ResultSetSpliterator<R> implements Spliterator<R> {

    private final SmartResultSet rs;
    private final ResultMapper<R> mapper;

    public ResultSetSpliterator(ResultSet rs, ResultMapper<R> mapper) {
        this.rs = new SmartResultSet(rs);
        this.mapper = mapper;
    }

    @Override
    public boolean tryAdvance(Consumer<? super R> action) {
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
    public @Nullable Spliterator<R> trySplit() {
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
