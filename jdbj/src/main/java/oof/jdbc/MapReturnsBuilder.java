package oof.jdbc;


import javax.annotation.concurrent.Immutable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Immutable
public class MapReturnsBuilder<R> {

    private final NamedParameterStatement statement;
    private final ResultSetMapper<R> mapper;

    public MapReturnsBuilder(NamedParameterStatement statement, ResultSetMapper<R> mapper) {
        this.statement = statement;
        this.mapper = mapper;
    }

    public <R2> MapReturnsBuilder<R2> remap(Function<R, R2> remap){
        return new MapReturnsBuilder<>(statement, rs -> remap.apply(mapper.map(rs)));
    }

    public StreamBindingsBuilder<R> stream() throws SQLException {
        return new StreamBindingsBuilder<>(new BindingsBuilder(statement), mapper);
    }

    public QueryBindingsBuilder <List<R>> toList(){
        return new QueryBindingsBuilder<>(new BindingsBuilder(statement), rs -> {
            final List<R> results = new ArrayList<>();
            while(rs.next()){
                results.add(mapper.map(rs));
            }
            return results;
        });
    }



}
