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
public final class StreamQuery<R> extends DecoratesBindingBuilder<StreamQuery<R>> {

    private final ResultSetMapper<R> mapper;

    StreamQuery(NamedParameterStatement statement, ResultSetMapper<R> mapper) {
        this(new BindingsBuilder(statement), mapper);
    }

    StreamQuery(BindingsBuilder bindingsBuilder, ResultSetMapper<R> mapper) {
        super(bindingsBuilder);
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
        final PreparedStatement ps = connection.prepareStatement(bindingsBuilder.buildSql());
        bindingsBuilder.bindToStatement(ps);
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

    @Override
    StreamQuery<R> prototype(BindingsBuilder newBindings) {
        return new StreamQuery<>(newBindings, mapper);
    }
}
