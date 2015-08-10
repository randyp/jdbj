package oof.jdbc;

import javax.annotation.concurrent.Immutable;

@Immutable
public class ReturnsBuilder{

    final NamedParameterStatement statement;

    public ReturnsBuilder(NamedParameterStatement statement) {
        this.statement = statement;
    }

    public <R> MapReturnsBuilder<R> map(ResultSetMapper<R> mapper){
        return new MapReturnsBuilder<>(statement, mapper);
    }

}
