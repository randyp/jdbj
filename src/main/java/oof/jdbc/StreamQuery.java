package oof.jdbc;

import oof.jdbc.lambda.ResultSetMapper;

import javax.annotation.concurrent.Immutable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Phase 3 class
 * @param <R>
 */
@Immutable
public final class StreamQuery<R> extends PositionalBindingsBuilder<StreamQuery<R>> {

    private final ResultSetMapper<R> mapper;

    StreamQuery(NamedParameterStatement statement, ResultSetMapper<R> mapper) {
        this(statement, PositionalBindings.empty(), mapper);
    }

    StreamQuery(NamedParameterStatement statement, PositionalBindings bindings, ResultSetMapper<R> mapper) {
        super(statement, bindings, ((s, b) -> new StreamQuery<>(s, b, mapper)));
        this.mapper = mapper;
    }

    /**
     * Phase 4 method
     *
     * Be sure to close the stream!!!
     *
     * @param connection
     * @return java.util.Stream of mapped results
     * @throws SQLException
     */
    public Stream<R> execute(Connection connection) throws SQLException {
        checkAllBindingsPresent();

        final PreparedStatement ps = connection.prepareStatement(buildSql());
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
