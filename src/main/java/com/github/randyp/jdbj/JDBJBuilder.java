package com.github.randyp.jdbj;

import com.github.randyp.jdbj.lambda.ResultSetMapper;

import javax.annotation.concurrent.Immutable;

@Immutable
public class JDBJBuilder {

    private final String string;

    JDBJBuilder(String string) {
        this.string = string;
    }

    public ReturnsQuery query() {
        return new ReturnsQuery(NamedParameterStatement.make(string));
    }

    public ExecuteUpdate update() {
        return new ExecuteUpdate(NamedParameterStatement.make(string));
    }

    public <R> ExecuteInsert<R> insert(ResultSetMapper<R> keyMapper) {
        return new ExecuteInsert<>(NamedParameterStatement.make(string), keyMapper);
    }

    public ExecuteStatement statement() {
        return new ExecuteStatement(NamedParameterStatement.make(string));
    }

    public ExecuteScript script() {
        return ExecuteScript.from(string);
    }

}
