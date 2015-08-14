package com.github.randyp.jdbj;


import com.github.randyp.jdbj.lambda.ResultSetMapper;

import javax.annotation.concurrent.Immutable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Immutable
public final class MapQuery<R> {

    private final NamedParameterStatement statement;
    private final ResultSetMapper<R> mapper;

    MapQuery(NamedParameterStatement statement, ResultSetMapper<R> mapper) {
        this.statement = statement;
        this.mapper = mapper;
    }

    /**
     *
     * @param remap
     * @param <R2>
     * @return phase 2 builder
     */
    public <R2> MapQuery<R2> remap(Function<R, R2> remap){
        return new MapQuery<>(statement, rs -> remap.apply(mapper.map(rs)));
    }

    /**
     *
     * @return phase 3 builder
     */
    public StreamQuery<R> stream() {
        return new StreamQuery<>(statement, mapper);
    }

    /**
     *
     * @return phase 3 builder
     */
    public ExecuteQuery<List<R>> toList(){
        return new ExecuteQuery<>(statement, rs -> {
            final List<R> results = new ArrayList<>();
            while(rs.next()){
                results.add(mapper.map(rs));
            }
            return results;
        });
    }


    public ExecuteQuery<R> first() {
        return new ExecuteQuery<>(statement, rs -> {
            R result = null;
            if(rs.next()){
                result = mapper.map(rs);
            }
            return result;
        });

    }
}
