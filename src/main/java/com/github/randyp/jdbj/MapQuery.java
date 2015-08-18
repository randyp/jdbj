package com.github.randyp.jdbj;


import com.github.randyp.jdbj.lambda.ResultSetMapper;

import javax.annotation.concurrent.Immutable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Immutable
public final class MapQuery<R> extends PositionalBindingsBuilder<MapQuery<R>> {

    private final ResultSetMapper<R> mapper;

    MapQuery(NamedParameterStatement statement, PositionalBindings bindings, ResultSetMapper<R> mapper) {
        super(statement, bindings, (s,b)->new MapQuery<>(s,b,mapper));
        this.mapper = mapper;
    }

    public <R2> MapQuery<R2> remap(Function<R, R2> remap){
        return new MapQuery<>(statement, bindings, rs -> remap.apply(mapper.map(rs)));
    }

    public StreamQuery<R> toStream() {
        return new StreamQuery<>(statement, bindings, mapper);
    }

    public ExecuteQuery<List<R>> toList(){
        return new ExecuteQuery<>(statement, bindings, rs -> {
            final List<R> results = new ArrayList<>();
            while(rs.next()){
                results.add(mapper.map(rs));
            }
            return results;
        });
    }


    public ExecuteQuery<Optional<R>> first() {
        return new ExecuteQuery<>(statement, bindings, rs -> {
            Optional<R> result = Optional.empty();
            if(rs.next()){
                result = Optional.ofNullable(mapper.map(rs));
            }
            return result;
        });

    }
}
