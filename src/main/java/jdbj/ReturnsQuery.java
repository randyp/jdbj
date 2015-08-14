package jdbj;

import jdbj.lambda.ResultSetMapper;
import jdbj.lambda.ResultSetRunnable;

import javax.annotation.concurrent.Immutable;

/**
 * Phase 2 Builder
 */
@Immutable
public final class ReturnsQuery {

    final NamedParameterStatement statement;

    public ReturnsQuery(NamedParameterStatement statement) {
        this.statement = statement;
    }

    /**
     *
     * @param mapper
     * @param <R>
     * @return phase 2 builder which maps individual results to elements
     */
    public <R> MapQuery<R> map(ResultSetMapper<R> mapper){
        return new MapQuery<>(statement, mapper);
    }

    public ExecuteQueryRunnable runnable(ResultSetRunnable runnable){
        return new ExecuteQueryRunnable(statement, runnable);
    }

}
