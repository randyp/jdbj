package com.github.randyp.jdbj;

import com.github.randyp.jdbj.lambda.ResultMapper;
import com.github.randyp.jdbj.lambda.ResultSetRunnable;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Return objects from queries or just execute {@link ResultSetRunnable}.
 * <p>
 * Is {@link Immutable}, so you will need to (re)assign to a variable after every binding.
 * <p>
 * Encapsulates building of{@link MapQuery} or {@link StreamQuery}
 * @see MapQuery
 * @see StreamQuery
 */
@Immutable
@ThreadSafe
public final class ReturnsQuery extends PositionalBindingsBuilder<ReturnsQuery> {

    ReturnsQuery(NamedParameterStatement statement) {
        this(statement, PositionalBindings.empty());
    }

    ReturnsQuery(NamedParameterStatement statement, PositionalBindings bindings) {
        super(statement, bindings, ReturnsQuery::new);
    }

    public <R> MapQuery<R> map(ResultMapper<R> mapper){
        return new MapQuery<>(statement, bindings, mapper);
    }

    public ExecuteQueryRunnable run(ResultSetRunnable runnable){
        return new ExecuteQueryRunnable(statement, bindings, runnable);
    }
}
