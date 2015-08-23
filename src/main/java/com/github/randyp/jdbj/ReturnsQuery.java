package com.github.randyp.jdbj;

import com.github.randyp.jdbj.lambda.ResultMapper;
import com.github.randyp.jdbj.lambda.ResultSetRunnable;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;

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

    public ExecuteQueryRunnable runnable(ResultSetRunnable runnable){
        return new ExecuteQueryRunnable(statement, bindings, runnable);
    }
}
