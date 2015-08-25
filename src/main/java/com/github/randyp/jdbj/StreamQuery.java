package com.github.randyp.jdbj;

import com.github.randyp.jdbj.lambda.ConnectionSupplier;
import com.github.randyp.jdbj.lambda.ResultMapper;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Use to return {@link Stream} of objects from queries. Please close your streams, perhaps using try-with-resources:
 * <pre>
 * {@code 
 * MapQuery<String> nameQuery = JDBJ.query("SELECT first_name FROM student").map(rs->rs.getString(1));
 * try(Stream<String> names = nameQuery.toStream().execute(db)){
 *     names.forEach(System.out.println);
 * }catch(AdvanceFailedException e){
 *     //possibly thrown when consuming from stream
 *     throw e.getCause();
 * }
 * }
 * </pre>   
 * Is {@link Immutable}, so you will need to (re)assign to a variable after every binding or call to {@link MapQuery#remap(Function)}.
 * <p>
 * Streams created with {@link StreamQuery#execute(Connection)} and similar methods will throw {@link AdvanceFailedException} if there is any unexpected exception while consuming from stream, so be sure to 
 * <p>     
 * Encapsulates executing {@link PreparedStatement#executeQuery()}, providing a stream of the results, and calling {@link ResultSet#close()} {@link PreparedStatement#close()} when caller calls {@link Stream#close()}.
 * @param <R> return type
 * @see MapQuery
 * @see ResultMapper
 */
@Immutable
@ThreadSafe
public final class StreamQuery<R> extends PositionalBindingsBuilder<StreamQuery<R>> {

    private final ResultMapper<R> mapper;

    StreamQuery(NamedParameterStatement statement, PositionalBindings bindings, ResultMapper<R> mapper) {
        super(statement, bindings, ((s, b) -> new StreamQuery<>(s, b, mapper)));
        Objects.requireNonNull(mapper, "mapper must not be null");
        this.mapper = mapper;
    }

    public Stream<R> execute(DataSource db) throws SQLException {
        return execute(db::getConnection);
    }

    public Stream<R> execute(ConnectionSupplier db) throws SQLException {
        checkAllBindingsPresent();
        final Connection connection = db.getConnection();
        return execute(connection).onClose( ()->{
            try {
                connection.close();
            } catch (SQLException e) {
                //ignore
            }
        });
    }

    public Stream<R> execute(Connection connection) throws SQLException {
        checkAllBindingsPresent();

        final PreparedStatement ps = connection.prepareStatement(
                buildSql(),
                ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY
        );
        bindToStatement(ps);
        final ResultSet rs = ps.executeQuery();
        final Spliterator<R> rsplit = new ResultSetSpliterator<>(rs, mapper);

        return StreamSupport.stream(rsplit, false)
                .onClose(() -> {
                    try {
                        rs.close();
                    } catch (SQLException e) {
                        //ignore
                    }
                })
                .onClose(() -> {
                    try {
                        ps.close();
                    } catch (SQLException e) {
                        //ignore
                    }
                });
    }
}
