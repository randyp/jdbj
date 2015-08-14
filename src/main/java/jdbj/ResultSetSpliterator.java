package jdbj;

import jdbj.lambda.ResultSetMapper;

import javax.annotation.Nullable;
import java.sql.ResultSet;
import java.util.Spliterator;
import java.util.function.Consumer;

public class ResultSetSpliterator<R> implements Spliterator<R> {

    private final ResultSet rs;
    private final ResultSetMapper<R> mapper;

    public ResultSetSpliterator(ResultSet rs, ResultSetMapper<R> mapper) {
        this.rs = rs;
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
