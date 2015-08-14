package oof.jdbc;


import oof.jdbc.lambda.Binding;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Immutable
public final class PositionalBindingsBuilder {

    private final NamedParameterStatement statement;
    private final PositionalBindings bindings;

    PositionalBindingsBuilder(NamedParameterStatement statement) {
        this(statement, PositionalBindings.empty());
    }

    PositionalBindingsBuilder(NamedParameterStatement statement, PositionalBindings bindings){
        this.statement = statement;
        this.bindings = bindings;
    }

    public void checkAllBindingsPresent(){
        statement.checkAllBindingsPresent(bindings);
    }

    public String buildSql() {
        return statement.jdbcSql(bindings);
    }

    public void bindToStatement(PreparedStatement ps) throws SQLException {
        statement.bind(ps, bindings);
    }

    public PositionalBindingsBuilder bind(String name, Binding binding){
        if(!statement.containsParameter(name)){
            throw new IllegalArgumentException("\""+name+"\" is not a named parameter");
        }

        return new PositionalBindingsBuilder(statement, bindings.addValueBinding(name, binding));
    }

    public PositionalBindingsBuilder bindArray(String name, @Nullable Array x) throws SQLException {
        return bind(name, pc->pc.setArray(x));
    }

    public PositionalBindingsBuilder bindAsciiStream(String name, @Nullable InputStream x) throws SQLException {
        return bind(name, pc->pc.setAsciiStream(x));
    }

    public PositionalBindingsBuilder bindAsciiStream(String name, @Nullable InputStream x, int length) throws SQLException {
        return bind(name, pc->pc.setAsciiStream(x, length));
    }

    public PositionalBindingsBuilder bindAsciiStream(String name, @Nullable InputStream x, long length) throws SQLException {
        return bind(name, pc->pc.setAsciiStream(x, length));
    }

    public PositionalBindingsBuilder bindBigDecimal(String name, @Nullable BigDecimal x) throws SQLException {
        return bind(name, pc->pc.setBigDecimal(x));
    }

    public PositionalBindingsBuilder bindBinaryStream(String name, @Nullable InputStream x) throws SQLException {
        return bind(name, pc->pc.setBinaryStream(x));
    }

    public PositionalBindingsBuilder bindBinaryStream(String name, @Nullable InputStream x, long length) throws SQLException {
        return bind(name, pc->pc.setBinaryStream(x, length));
    }

    public PositionalBindingsBuilder bindBinaryStream(String name, @Nullable InputStream x, int length) throws SQLException {
        return bind(name, pc->pc.setBinaryStream(x, length));
    }

    public PositionalBindingsBuilder bindBlob(String name, @Nullable  Blob x) throws SQLException {
        return bind(name, pc->pc.setBlob(x));
    }

    public PositionalBindingsBuilder bindBlob(String name, @Nullable InputStream inputStream) throws SQLException {
        return bind(name, pc->pc.setBlob(inputStream));
    }

    public PositionalBindingsBuilder bindBlob(String name, @Nullable InputStream inputStream, long length) throws SQLException {
        return bind(name, pc->pc.setBlob(inputStream, length));
    }

    public PositionalBindingsBuilder bindBoolean(String name, boolean x) throws SQLException {
        return bind(name, pc-> pc.setBoolean(x));
    }

    public PositionalBindingsBuilder bindByte(String name, byte x) throws SQLException {
        return bind(name, pc->pc.setByte(x));
    }

    public PositionalBindingsBuilder bindBytes(String name, byte[] x) throws SQLException {
        return bind(name, pc->pc.setBytes(x));
    }

    public PositionalBindingsBuilder bindCharacterStream(String name, @Nullable Reader reader) throws SQLException {
        return bind(name, pc->pc.setCharacterStream(reader));
    }

    public PositionalBindingsBuilder bindCharacterStream(String name, @Nullable Reader reader, int length) throws SQLException {
        return bind(name, pc->pc.setCharacterStream(reader, length));
    }

    public PositionalBindingsBuilder bindCharacterStream(String name, @Nullable Reader reader, long length) throws SQLException {
        return bind(name, pc->pc.setCharacterStream(reader, length));
    }

    public PositionalBindingsBuilder bindClob(String name, @Nullable Clob x) throws SQLException {
        return bind(name, pc->pc.setClob(x));
    }

    public PositionalBindingsBuilder bindClob(String name, @Nullable Reader reader) throws SQLException {
        return bind(name, pc->pc.setClob(reader));
    }

    public PositionalBindingsBuilder bindClob(String name, @Nullable Reader reader, long length) throws SQLException {
        return bind(name, pc->pc.setClob(reader, length));
    }

    public PositionalBindingsBuilder bindDate(String name, @Nullable Date x) throws SQLException {
        return bind(name, pc->pc.setDate(x));
    }

    public PositionalBindingsBuilder bindDate(String name, @Nullable Date x, Calendar cal) throws SQLException {
        return bind(name, pc->pc.setDate(x, cal));
    }

    public PositionalBindingsBuilder bindDouble(String name, double x) throws SQLException {
        return bind(name, pc->pc.setDouble(x));
    }

    public PositionalBindingsBuilder bindFloat(String name, float x) throws SQLException {
        return bind(name, pc->pc.setFloat(x));
    }

    public PositionalBindingsBuilder bindInt(String name, int x) throws SQLException {
        return bind(name, pc->pc.setInt(x));
    }

    public PositionalBindingsBuilder bindLong(String name, long x) throws SQLException {
        return bind(name, pc->pc.setLong(x));
    }

    public PositionalBindingsBuilder bindNCharacterStream(String name, @Nullable Reader value) throws SQLException {
        return bind(name, pc->pc.setNCharacterStream(value));
    }

    public PositionalBindingsBuilder bindNCharacterStream(String name, @Nullable Reader value, long length) throws SQLException {
        return bind(name, pc->pc.setNCharacterStream(value, length));
    }

    public PositionalBindingsBuilder bindNClob(String name, @Nullable NClob value) throws SQLException {
        return bind(name, pc->pc.setNClob(value));
    }

    public PositionalBindingsBuilder bindNClob(String name, @Nullable Reader reader) throws SQLException {
        return bind(name, pc->pc.setNClob(reader));
    }

    public PositionalBindingsBuilder bindNClob(String name, @Nullable Reader reader, long length) throws SQLException {
        return bind(name, pc->pc.setNClob(reader, length));
    }

    public PositionalBindingsBuilder bindNString(String name, @Nullable String value) throws SQLException {
        return bind(name, pc->pc.setNString(value));
    }

    public PositionalBindingsBuilder bindNull(String name, int sqlType) throws SQLException {
        return bind(name, pc -> pc.setNull(sqlType));
    }

    public PositionalBindingsBuilder bindNull(String name, int sqlType, String typeName) throws SQLException {
        return bind(name, pc->pc.setNull(sqlType, typeName));
    }

    public PositionalBindingsBuilder bindObject(String name, @Nullable Object x) throws SQLException {
        return bind(name, pc->pc.setObject(x));
    }

    public PositionalBindingsBuilder bindObject(String name, @Nullable Object x, int targetSqlType) throws SQLException {
        return bind(name, pc->pc.setObject(x, targetSqlType));
    }

    public PositionalBindingsBuilder bindObject(String name, @Nullable Object x, SQLType targetSqlType) throws SQLException {
        return bind(name, pc -> pc.setObject(x, targetSqlType));
    }

    public PositionalBindingsBuilder bindObject(String name, @Nullable Object x, int targetSqlType, int scaleOrLength) throws SQLException {
        return bind(name, pc->pc.setObject(x, targetSqlType, scaleOrLength));
    }

    public PositionalBindingsBuilder bindObject(String name, @Nullable Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {
        return bind(name, pc->pc.setObject(x, targetSqlType, scaleOrLength));
    }

    public PositionalBindingsBuilder bindRef(String name, @Nullable Ref x) throws SQLException {
        return bind(name, pc->pc.setRef(x));
    }

    public PositionalBindingsBuilder bindShort(String name, short x) throws SQLException {
        return bind(name, pc->pc.setShort(x));
    }

    public PositionalBindingsBuilder bindSQLXML(String name, @Nullable SQLXML xmlObject) throws SQLException {
        return bind(name, pc->pc.setSQLXML(xmlObject));
    }

    public PositionalBindingsBuilder bindString(String name, @Nullable String x) throws SQLException {
        return bind(name, pc->pc.setString(x));
    }

    public PositionalBindingsBuilder bindTime(String name, @Nullable Time x) throws SQLException {
        return bind(name, pc->pc.setTime(x));
    }

    public PositionalBindingsBuilder bindTimestamp(String name, @Nullable Timestamp x) throws SQLException {
        return bind(name, pc->pc.setTimestamp(x));
    }

    public PositionalBindingsBuilder bindTime(String name, @Nullable Time x, Calendar cal) throws SQLException {
        return bind(name, pc->pc.setTime(x, cal));
    }

    public PositionalBindingsBuilder bindTimestamp(String name, @Nullable Timestamp x, Calendar cal) throws SQLException {
        return bind(name, pc->pc.setTimestamp(x, cal));
    }

    public PositionalBindingsBuilder bindURL(String name, @Nullable URL x) throws SQLException {
        return bind(name, pc->pc.setURL(x));
    }

    public PositionalBindingsBuilder bindList(String name, List<Binding> bindings){
        if(!statement.containsParameter(name)){
            throw new IllegalArgumentException("\""+name+"\" is not a named parameter");
        }

        return new PositionalBindingsBuilder(statement, this.bindings.addListBinding(name, bindings));
    }

    public PositionalBindingsBuilder bindLongs(String name, long... xs){
        if (xs == null) {
            throw new IllegalArgumentException("xs cannot be null - consider using bindArray");
        }
        final List<Binding> bindings = new ArrayList<>();
        for (final long x : xs) {
            bindings.add(pc -> pc.setLong(x));
        }

        return bindList(name, bindings);
    }

    public PositionalBindingsBuilder bindStrings(String name, List<String> xs){
        if (xs == null) {
            throw new IllegalArgumentException("xs cannot be null - consider using bindArray");
        }
        final Function<String, Binding> createBinding = x -> (Binding) preparedColumn -> preparedColumn.setString(x);
        final List<Binding> bindings = xs.stream().map(createBinding).collect(Collectors.toList());
        return bindList(name, bindings);
    }

    public PositionalBindingsBuilder bindStrings(String name, String... xs){
        if (xs == null) {
            throw new IllegalArgumentException("xs cannot be null - consider using bindArray");
        }
        return bindStrings(name, Arrays.asList(xs));
    }
}
