package oof.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamBindingsBuilder<R> {


    private final BindingsBuilder bindingsBuilder;
    private final ResultSetMapper<R> mapper;

    public StreamBindingsBuilder(BindingsBuilder bindingsBuilder, ResultSetMapper<R> mapper) {
        this.bindingsBuilder = bindingsBuilder;
        this.mapper = mapper;
    }

    public Stream<R> execute(Connection connection) throws SQLException {
        final String sql = bindingsBuilder.statement.jdbcSql(bindingsBuilder.bindings);
        final PreparedStatement ps = connection.prepareStatement(sql);
        bindingsBuilder.statement.bind(bindingsBuilder.bindings, ps);
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

    public StreamBindingsBuilder<R> bind(String name, Binding binding) {
        return prototype(bindingsBuilder.bind(name, binding));
    }

    public StreamBindingsBuilder<R> bindClob(String name, Reader reader, long length) throws SQLException {
        return prototype(bindingsBuilder.bindClob(name, reader, length));
    }

    public StreamBindingsBuilder<R> bindDouble(String name, double x) throws SQLException {
        return prototype(bindingsBuilder.bindDouble(name, x));
    }

    public StreamBindingsBuilder<R> bindObject(String name, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
        return prototype(bindingsBuilder.bindObject(name, x, targetSqlType, scaleOrLength));
    }

    public StreamBindingsBuilder<R> bindBigDecimal(String name, BigDecimal x) throws SQLException {
        return prototype(bindingsBuilder.bindBigDecimal(name, x));
    }

    public StreamBindingsBuilder<R> bindObject(String name, Object x, SQLType targetSqlType) throws SQLException {
        return prototype(bindingsBuilder.bindObject(name, x, targetSqlType));
    }

    public StreamBindingsBuilder<R> bindInt(String name, int x) throws SQLException {
        return prototype(bindingsBuilder.bindInt(name, x));
    }

    public StreamBindingsBuilder<R> bindBinaryStream(String name, InputStream x) throws SQLException {
        return prototype(bindingsBuilder.bindBinaryStream(name, x));
    }

    public StreamBindingsBuilder<R> bindFloat(String name, float x) throws SQLException {
        return prototype(bindingsBuilder.bindFloat(name, x));
    }

    public StreamBindingsBuilder<R> bindAsciiStream(String name, InputStream x, long length) throws SQLException {
        return prototype(bindingsBuilder.bindAsciiStream(name, x, length));
    }

    public StreamBindingsBuilder<R> bindByte(String name, byte x) throws SQLException {
        return prototype(bindingsBuilder.bindByte(name, x));
    }

    public StreamBindingsBuilder<R> bindShort(String name, short x) throws SQLException {
        return prototype(bindingsBuilder.bindShort(name, x));
    }

    public StreamBindingsBuilder<R> bindAsciiStream(String name, InputStream x, int length) throws SQLException {
        return prototype(bindingsBuilder.bindAsciiStream(name, x, length));
    }

    public StreamBindingsBuilder<R> bindString(String name, String x) throws SQLException {
        return prototype(bindingsBuilder.bindString(name, x));
    }

    public StreamBindingsBuilder<R> bindBlob(String name, Blob x) throws SQLException {
        return prototype(bindingsBuilder.bindBlob(name, x));
    }

    public StreamBindingsBuilder<R> bindBlob(String name, InputStream inputStream) throws SQLException {
        return prototype(bindingsBuilder.bindBlob(name, inputStream));
    }

    public StreamBindingsBuilder<R> bindObject(String name, Object x) throws SQLException {
        return prototype(bindingsBuilder.bindObject(name, x));
    }

    public StreamBindingsBuilder<R> bindBinaryStream(String name, InputStream x, int length) throws SQLException {
        return prototype(bindingsBuilder.bindBinaryStream(name, x, length));
    }

    public StreamBindingsBuilder<R> bindRef(String name, Ref x) throws SQLException {
        return prototype(bindingsBuilder.bindRef(name, x));
    }

    public StreamBindingsBuilder<R> bindTime(String name, Time x, Calendar cal) throws SQLException {
        return prototype(bindingsBuilder.bindTime(name, x, cal));
    }

    public StreamBindingsBuilder<R> bindDate(String name, Date x) throws SQLException {
        return prototype(bindingsBuilder.bindDate(name, x));
    }

    public StreamBindingsBuilder<R> bindBoolean(String name, boolean x) throws SQLException {
        return prototype(bindingsBuilder.bindBoolean(name, x));
    }

    public StreamBindingsBuilder<R> bindTimestamp(String name, Timestamp x, Calendar cal) throws SQLException {
        return prototype(bindingsBuilder.bindTimestamp(name, x, cal));
    }

    public StreamBindingsBuilder<R> bindTime(String name, Time x) throws SQLException {
        return prototype(bindingsBuilder.bindTime(name, x));
    }

    public StreamBindingsBuilder<R> bindBytes(String name, byte[] x) throws SQLException {
        return prototype(bindingsBuilder.bindBytes(name, x));
    }

    public StreamBindingsBuilder<R> bindObject(String name, Object x, int targetSqlType) throws SQLException {
        return prototype(bindingsBuilder.bindObject(name, x, targetSqlType));
    }

    public StreamBindingsBuilder<R> bindAsciiStream(String name, InputStream x) throws SQLException {
        return prototype(bindingsBuilder.bindAsciiStream(name, x));
    }

    public StreamBindingsBuilder<R> bindNClob(String name, Reader reader) throws SQLException {
        return prototype(bindingsBuilder.bindNClob(name, reader));
    }

    public StreamBindingsBuilder<R> bindBinaryStream(String name, InputStream x, long length) throws SQLException {
        return prototype(bindingsBuilder.bindBinaryStream(name, x, length));
    }

    public StreamBindingsBuilder<R> bindCharacterStream(String name, Reader reader, long length) throws SQLException {
        return prototype(bindingsBuilder.bindCharacterStream(name, reader, length));
    }

    public StreamBindingsBuilder<R> bindNClob(String name, NClob value) throws SQLException {
        return prototype(bindingsBuilder.bindNClob(name, value));
    }

    public StreamBindingsBuilder<R> bindNCharacterStream(String name, Reader value, long length) throws SQLException {
        return prototype(bindingsBuilder.bindNCharacterStream(name, value, length));
    }

    public StreamBindingsBuilder<R> bindTimestamp(String name, Timestamp x) throws SQLException {
        return prototype(bindingsBuilder.bindTimestamp(name, x));
    }

    public StreamBindingsBuilder<R> bindBlob(String name, InputStream inputStream, long length) throws SQLException {
        return prototype(bindingsBuilder.bindBlob(name, inputStream, length));
    }

    public StreamBindingsBuilder<R> bindCharacterStream(String name, Reader reader, int length) throws SQLException {
        return prototype(bindingsBuilder.bindCharacterStream(name, reader, length));
    }

    public StreamBindingsBuilder<R> bindNull(String name, int sqlType, String typeName) throws SQLException {
        return prototype(bindingsBuilder.bindNull(name, sqlType, typeName));
    }

    public StreamBindingsBuilder<R> bindDate(String name, Date x, Calendar cal) throws SQLException {
        return prototype(bindingsBuilder.bindDate(name, x, cal));
    }

    public StreamBindingsBuilder<R> bindClob(String name, Reader reader) throws SQLException {
        return prototype(bindingsBuilder.bindClob(name, reader));
    }

    public StreamBindingsBuilder<R> bindLong(String name, long x) throws SQLException {
        return prototype(bindingsBuilder.bindLong(name, x));
    }

    public StreamBindingsBuilder<R> bindSQLXML(String name, SQLXML xmlObject) throws SQLException {
        return prototype(bindingsBuilder.bindSQLXML(name, xmlObject));
    }

    public StreamBindingsBuilder<R> bindArray(String name, Array x) throws SQLException {
        return prototype(bindingsBuilder.bindArray(name, x));
    }

    public StreamBindingsBuilder<R> bindObject(String name, Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {
        return prototype(bindingsBuilder.bindObject(name, x, targetSqlType, scaleOrLength));
    }

    public StreamBindingsBuilder<R> bindNString(String name, String value) throws SQLException {
        return prototype(bindingsBuilder.bindNString(name, value));
    }

    public StreamBindingsBuilder<R> bindClob(String name, Clob x) throws SQLException {
        return prototype(bindingsBuilder.bindClob(name, x));
    }

    public StreamBindingsBuilder<R> bindNull(String name, int sqlType) throws SQLException {
        return prototype(bindingsBuilder.bindNull(name, sqlType));
    }

    public StreamBindingsBuilder<R> bindURL(String name, URL x) throws SQLException {
        return prototype(bindingsBuilder.bindURL(name, x));
    }

    public StreamBindingsBuilder<R> bindNCharacterStream(String name, Reader value) throws SQLException {
        return prototype(bindingsBuilder.bindNCharacterStream(name, value));
    }

    public StreamBindingsBuilder<R> bindNClob(String name, Reader reader, long length) throws SQLException {
        return prototype(bindingsBuilder.bindNClob(name, reader, length));
    }

    public StreamBindingsBuilder<R> bindCharacterStream(String name, Reader reader) throws SQLException {
        return prototype(bindingsBuilder.bindCharacterStream(name, reader));
    }

    private StreamBindingsBuilder<R> prototype(BindingsBuilder newBindings) {
        return new StreamBindingsBuilder<>(newBindings, mapper);
    }
}
