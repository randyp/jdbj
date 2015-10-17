package io.codemonastery.jdbj;


import io.codemonastery.jdbj.lambda.ResultMapper;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * Use to return lists or streams of objects from queries, and related behaviour.
 * <pre>
 * {@code
 * List<Student> students = JDBJ.query("SELECT * FROM student").map(Student::from).toList().execute(db);
 * }
 * </pre>
 * Is {@link Immutable}, so you will need to (re)assign to a variable after every binding or call to {@link MapQuery#remap(Function)}.
 * <p>
 * Encapsulates building of {@link ExecuteQuery} or {@link StreamQuery} using a {@link ResultMapper}.
 * @param <R> return type
 * @see ExecuteQuery
 * @see StreamQuery
 * @see ResultMapper
 */
@Immutable
@ThreadSafe
public final class MapQuery<R> extends PositionalBindingsBuilder<MapQuery<R>> {

    private final ResultMapper<R> mapper;

    MapQuery(NamedParameterStatement statement, PositionalBindings bindings, ResultMapper<R> mapper) {
        super(statement, bindings, (s,b)->new MapQuery<>(s,b,mapper));
        Objects.requireNonNull(mapper, "mapper must not be null");
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
