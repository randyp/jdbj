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
import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Immutable
@ThreadSafe
public final class StreamQuery<R> extends PositionalBindingsBuilder<StreamQuery<R>> {

    private final ResultMapper<R> mapper;

    StreamQuery(NamedParameterStatement statement, PositionalBindings bindings, ResultMapper<R> mapper) {
        super(statement, bindings, ((s, b) -> new StreamQuery<>(s, b, mapper)));
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
