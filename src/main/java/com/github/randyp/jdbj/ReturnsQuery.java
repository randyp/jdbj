package com.github.randyp.jdbj;

import com.github.randyp.jdbj.lambda.ResultSetMapper;
import com.github.randyp.jdbj.lambda.ResultSetRunnable;

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

    public <R> MapQuery<R> map(ResultSetMapper<R> mapper){
        return new MapQuery<>(statement, mapper);
    }

    public ExecuteQueryRunnable runnable(ResultSetRunnable runnable){
        return new ExecuteQueryRunnable(statement, runnable);
    }

}
