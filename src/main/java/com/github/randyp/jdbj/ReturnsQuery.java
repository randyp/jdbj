package com.github.randyp.jdbj;

import com.github.randyp.jdbj.lambda.ResultSetMapper;
import com.github.randyp.jdbj.lambda.ResultSetRunnable;

import javax.annotation.concurrent.Immutable;

/**
 * Phase 2 Builder
 */
@Immutable
public final class ReturnsQuery extends PositionalBindingsBuilder<ReturnsQuery> {

    ReturnsQuery(NamedParameterStatement statement) {
        this(statement, PositionalBindings.empty());
    }

    ReturnsQuery(NamedParameterStatement statement, PositionalBindings bindings) {
        super(statement, bindings, ReturnsQuery::new);
    }

    public <R> MapQuery<R> map(ResultSetMapper<R> mapper){
        return new MapQuery<>(statement, bindings, mapper);
    }

    public ExecuteQueryRunnable runnable(ResultSetRunnable runnable){
        return new ExecuteQueryRunnable(statement, runnable);
    }

}
