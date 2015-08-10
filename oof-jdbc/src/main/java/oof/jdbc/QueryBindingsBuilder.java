package oof.jdbc;


import javax.annotation.concurrent.Immutable;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Immutable
public class QueryBindingsBuilder<R> {

    private final BindingsBuilder bindingsBuilder;
    private final ResultSetToResult<R> toResult;

    QueryBindingsBuilder(BindingsBuilder bindingsBuilder, ResultSetToResult<R> toResult) {
        this.bindingsBuilder = bindingsBuilder;
        this.toResult = toResult;
    }

    public R execute(Connection connection) throws SQLException {
        String sql = bindingsBuilder.statement.jdbcSql(bindingsBuilder.bindings);
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            bindingsBuilder.statement.bind(bindingsBuilder.bindings, ps);
            try(ResultSet rs = ps.executeQuery()){
                return toResult.from(rs);
            }
        }
    }

    public QueryBindingsBuilder<R> bind(String name, Binding binding) {
        return prototype(bindingsBuilder.bind(name, binding));
    }

    public QueryBindingsBuilder<R> bindClob(String name, Reader reader, long length) throws SQLException {
        return prototype(bindingsBuilder.bindClob(name, reader, length));
    }

    public QueryBindingsBuilder<R> bindDouble(String name, double x) throws SQLException {
        return prototype(bindingsBuilder.bindDouble(name, x));
    }

    public QueryBindingsBuilder<R> bindObject(String name, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
        return prototype(bindingsBuilder.bindObject(name, x, targetSqlType, scaleOrLength));
    }

    public QueryBindingsBuilder<R> bindBigDecimal(String name, BigDecimal x) throws SQLException {
        return prototype(bindingsBuilder.bindBigDecimal(name, x));
    }

    public QueryBindingsBuilder<R> bindObject(String name, Object x, SQLType targetSqlType) throws SQLException {
        return prototype(bindingsBuilder.bindObject(name, x, targetSqlType));
    }

    public QueryBindingsBuilder<R> bindInt(String name, int x) throws SQLException {
        return prototype(bindingsBuilder.bindInt(name, x));
    }

    public QueryBindingsBuilder<R> bindBinaryStream(String name, InputStream x) throws SQLException {
        return prototype(bindingsBuilder.bindBinaryStream(name, x));
    }

    public QueryBindingsBuilder<R> bindFloat(String name, float x) throws SQLException {
        return prototype(bindingsBuilder.bindFloat(name, x));
    }

    public QueryBindingsBuilder<R> bindAsciiStream(String name, InputStream x, long length) throws SQLException {
        return prototype(bindingsBuilder.bindAsciiStream(name, x, length));
    }

    public QueryBindingsBuilder<R> bindByte(String name, byte x) throws SQLException {
        return prototype(bindingsBuilder.bindByte(name, x));
    }

    public QueryBindingsBuilder<R> bindShort(String name, short x) throws SQLException {
        return prototype(bindingsBuilder.bindShort(name, x));
    }

    public QueryBindingsBuilder<R> bindAsciiStream(String name, InputStream x, int length) throws SQLException {
        return prototype(bindingsBuilder.bindAsciiStream(name, x, length));
    }

    public QueryBindingsBuilder<R> bindString(String name, String x) throws SQLException {
        return prototype(bindingsBuilder.bindString(name, x));
    }

    public QueryBindingsBuilder<R> bindBlob(String name, Blob x) throws SQLException {
        return prototype(bindingsBuilder.bindBlob(name, x));
    }

    public QueryBindingsBuilder<R> bindBlob(String name, InputStream inputStream) throws SQLException {
        return prototype(bindingsBuilder.bindBlob(name, inputStream));
    }

    public QueryBindingsBuilder<R> bindObject(String name, Object x) throws SQLException {
        return prototype(bindingsBuilder.bindObject(name, x));
    }

    public QueryBindingsBuilder<R> bindBinaryStream(String name, InputStream x, int length) throws SQLException {
        return prototype(bindingsBuilder.bindBinaryStream(name, x, length));
    }

    public QueryBindingsBuilder<R> bindRef(String name, Ref x) throws SQLException {
        return prototype(bindingsBuilder.bindRef(name, x));
    }

    public QueryBindingsBuilder<R> bindTime(String name, Time x, Calendar cal) throws SQLException {
        return prototype(bindingsBuilder.bindTime(name, x, cal));
    }

    public QueryBindingsBuilder<R> bindDate(String name, Date x) throws SQLException {
        return prototype(bindingsBuilder.bindDate(name, x));
    }

    public QueryBindingsBuilder<R> bindBoolean(String name, boolean x) throws SQLException {
        return prototype(bindingsBuilder.bindBoolean(name, x));
    }

    public QueryBindingsBuilder<R> bindTimestamp(String name, Timestamp x, Calendar cal) throws SQLException {
        return prototype(bindingsBuilder.bindTimestamp(name, x, cal));
    }

    public QueryBindingsBuilder<R> bindTime(String name, Time x) throws SQLException {
        return prototype(bindingsBuilder.bindTime(name, x));
    }

    public QueryBindingsBuilder<R> bindBytes(String name, byte[] x) throws SQLException {
        return prototype(bindingsBuilder.bindBytes(name, x));
    }

    public QueryBindingsBuilder<R> bindObject(String name, Object x, int targetSqlType) throws SQLException {
        return prototype(bindingsBuilder.bindObject(name, x, targetSqlType));
    }

    public QueryBindingsBuilder<R> bindAsciiStream(String name, InputStream x) throws SQLException {
        return prototype(bindingsBuilder.bindAsciiStream(name, x));
    }

    public QueryBindingsBuilder<R> bindNClob(String name, Reader reader) throws SQLException {
        return prototype(bindingsBuilder.bindNClob(name, reader));
    }

    public QueryBindingsBuilder<R> bindBinaryStream(String name, InputStream x, long length) throws SQLException {
        return prototype(bindingsBuilder.bindBinaryStream(name, x, length));
    }

    public QueryBindingsBuilder<R> bindCharacterStream(String name, Reader reader, long length) throws SQLException {
        return prototype(bindingsBuilder.bindCharacterStream(name, reader, length));
    }

    public QueryBindingsBuilder<R> bindNClob(String name, NClob value) throws SQLException {
        return prototype(bindingsBuilder.bindNClob(name, value));
    }

    public QueryBindingsBuilder<R> bindNCharacterStream(String name, Reader value, long length) throws SQLException {
        return prototype(bindingsBuilder.bindNCharacterStream(name, value, length));
    }

    public QueryBindingsBuilder<R> bindTimestamp(String name, Timestamp x) throws SQLException {
        return prototype(bindingsBuilder.bindTimestamp(name, x));
    }

    public QueryBindingsBuilder<R> bindBlob(String name, InputStream inputStream, long length) throws SQLException {
        return prototype(bindingsBuilder.bindBlob(name, inputStream, length));
    }

    public QueryBindingsBuilder<R> bindCharacterStream(String name, Reader reader, int length) throws SQLException {
        return prototype(bindingsBuilder.bindCharacterStream(name, reader, length));
    }

    public QueryBindingsBuilder<R> bindNull(String name, int sqlType, String typeName) throws SQLException {
        return prototype(bindingsBuilder.bindNull(name, sqlType, typeName));
    }

    public QueryBindingsBuilder<R> bindDate(String name, Date x, Calendar cal) throws SQLException {
        return prototype(bindingsBuilder.bindDate(name, x, cal));
    }

    public QueryBindingsBuilder<R> bindClob(String name, Reader reader) throws SQLException {
        return prototype(bindingsBuilder.bindClob(name, reader));
    }

    public QueryBindingsBuilder<R> bindLong(String name, long x) throws SQLException {
        return prototype(bindingsBuilder.bindLong(name, x));
    }

    public QueryBindingsBuilder<R> bindSQLXML(String name, SQLXML xmlObject) throws SQLException {
        return prototype(bindingsBuilder.bindSQLXML(name, xmlObject));
    }

    public QueryBindingsBuilder<R> bindArray(String name, Array x) throws SQLException {
        return prototype(bindingsBuilder.bindArray(name, x));
    }

    public QueryBindingsBuilder<R> bindObject(String name, Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {
        return prototype(bindingsBuilder.bindObject(name, x, targetSqlType, scaleOrLength));
    }

    public QueryBindingsBuilder<R> bindNString(String name, String value) throws SQLException {
        return prototype(bindingsBuilder.bindNString(name, value));
    }

    public QueryBindingsBuilder<R> bindClob(String name, Clob x) throws SQLException {
        return prototype(bindingsBuilder.bindClob(name, x));
    }

    public QueryBindingsBuilder<R> bindNull(String name, int sqlType) throws SQLException {
        return prototype(bindingsBuilder.bindNull(name, sqlType));
    }

    public QueryBindingsBuilder<R> bindURL(String name, URL x) throws SQLException {
        return prototype(bindingsBuilder.bindURL(name, x));
    }

    public QueryBindingsBuilder<R> bindNCharacterStream(String name, Reader value) throws SQLException {
        return prototype(bindingsBuilder.bindNCharacterStream(name, value));
    }

    public QueryBindingsBuilder<R> bindNClob(String name, Reader reader, long length) throws SQLException {
        return prototype(bindingsBuilder.bindNClob(name, reader, length));
    }

    public QueryBindingsBuilder<R> bindCharacterStream(String name, Reader reader) throws SQLException {
        return prototype(bindingsBuilder.bindCharacterStream(name, reader));
    }

    private QueryBindingsBuilder<R> prototype(BindingsBuilder newBindings) {
        return new QueryBindingsBuilder<>(newBindings, toResult);
    }
}
