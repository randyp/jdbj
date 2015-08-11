package oof.jdbc;


import oof.jdbc.lambda.ResultSetMapper;

import javax.annotation.concurrent.Immutable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Immutable
public final class MapReturnsBuilder<R> {

    private final NamedParameterStatement statement;
    private final ResultSetMapper<R> mapper;

    MapReturnsBuilder(NamedParameterStatement statement, ResultSetMapper<R> mapper) {
        this.statement = statement;
        this.mapper = mapper;
    }

    /**
     *
     * @param remap
     * @param <R2>
     * @return phase 2 builder
     */
    public <R2> MapReturnsBuilder<R2> remap(Function<R, R2> remap){
        return new MapReturnsBuilder<>(statement, rs -> remap.apply(mapper.map(rs)));
    }

    /**
     *
     * @return phase 3 builder
     */
    public StreamBindingsBuilder<R> stream() {
        return new StreamBindingsBuilder<>(statement, mapper);
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



}
