package com.github.randyp.jdbj;

import com.github.randyp.jdbj.lambda.ResultMapper;

import javax.annotation.Nullable;
import java.sql.ResultSet;
import java.util.Spliterator;
import java.util.function.Consumer;

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
