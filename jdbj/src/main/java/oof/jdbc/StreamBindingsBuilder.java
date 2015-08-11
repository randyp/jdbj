package oof.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamBindingsBuilder<R> extends DecoratesBindingBuilder<StreamBindingsBuilder<R>> {

    private final ResultSetMapper<R> mapper;

    public StreamBindingsBuilder(BindingsBuilder bindingsBuilder, ResultSetMapper<R> mapper) {
        super(bindingsBuilder);
        this.mapper = mapper;
    }

    public Stream<R> execute(Connection connection) throws SQLException {
        final PreparedStatement ps = connection.prepareStatement(bindingsBuilder.buildSql());
        bindingsBuilder.bindToStatement(ps);
        final ResultSet rs = ps.executeQuery();

        final Spliterator<R> rsplit = new Spliterator<R>() {
            @Override
            public boolean tryAdvance(Consumer<? super R> action) {
                try {
                    final boolean advanced = rs.next();
                    if (advanced) {
                        action.accept(mapper.map(rs));
                    }
                    return advanced;
                } catch (SQLException e) {
                    throw new AdvanceFailedException(e);
                }
            }

            @Override
            public Spliterator<R> trySplit() {
                return null;
            }

            @Override
            public long estimateSize() {
                return Long.MAX_VALUE;
            }

            @Override
            public int characteristics() {
                return Spliterator.IMMUTABLE | Spliterator.NONNULL;
            }
        };

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
    protected StreamBindingsBuilder<R> prototype(BindingsBuilder newBindings) {
        return new StreamBindingsBuilder<>(newBindings, mapper);
    }
}
