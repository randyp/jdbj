package oof.jdbc;

import oof.jdbc.lambda.ResultSetMapper;
import oof.jdbc.lambda.ResultSetRunnable;

import javax.annotation.concurrent.Immutable;

/**
 * Phase 2 Builder
 */
@Immutable
public final class ReturnsBuilder{

    final NamedParameterStatement statement;

    public ReturnsBuilder(NamedParameterStatement statement) {
        this.statement = statement;
    }

    /**
     *
     * @param mapper
     * @param <R>
     * @return phase 2 builder which maps individual results to elements
     */
    public <R> MapReturnsBuilder<R> map(ResultSetMapper<R> mapper){
        return new MapReturnsBuilder<>(statement, mapper);
    }

    public ExecuteQueryNoResult runnable(ResultSetRunnable runnable){
        return new ExecuteQueryNoResult(statement, runnable);
    }

}
