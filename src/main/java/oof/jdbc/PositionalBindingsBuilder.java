package oof.jdbc;


import oof.jdbc.lambda.Binding;
import oof.jdbc.lambda.PositionalBindingsBuilderFactory;

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
public class PositionalBindingsBuilder<E extends PositionalBindingsBuilder<E>> {

    private final NamedParameterStatement statement;
    private final PositionalBindings bindings;
    private final PositionalBindingsBuilderFactory<E> factory;

    PositionalBindingsBuilder(NamedParameterStatement statement, PositionalBindings bindings, PositionalBindingsBuilderFactory<E> factory){
        this.statement = statement;
        this.bindings = bindings;
        this.factory = factory;
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

    public E bind(String name, Binding binding){
        if(!statement.containsParameter(name)){
            throw new IllegalArgumentException("\""+name+"\" is not a named parameter");
        }

        return factory.make(statement, bindings.addValueBinding(name, binding));
    }

    public E bindArray(String name, @Nullable Array x) throws SQLException {
        return bind(name, pc->pc.setArray(x));
    }

    public E bindAsciiStream(String name, @Nullable InputStream x) throws SQLException {
        return bind(name, pc->pc.setAsciiStream(x));
    }

    public E bindAsciiStream(String name, @Nullable InputStream x, int length) throws SQLException {
        return bind(name, pc->pc.setAsciiStream(x, length));
    }

    public E bindAsciiStream(String name, @Nullable InputStream x, long length) throws SQLException {
        return bind(name, pc->pc.setAsciiStream(x, length));
    }

    public E bindBigDecimal(String name, @Nullable BigDecimal x) throws SQLException {
        return bind(name, pc->pc.setBigDecimal(x));
    }

    public E bindBinaryStream(String name, @Nullable InputStream x) throws SQLException {
        return bind(name, pc->pc.setBinaryStream(x));
    }

    public E bindBinaryStream(String name, @Nullable InputStream x, long length) throws SQLException {
        return bind(name, pc->pc.setBinaryStream(x, length));
    }

    public E bindBinaryStream(String name, @Nullable InputStream x, int length) throws SQLException {
        return bind(name, pc->pc.setBinaryStream(x, length));
    }

    public E bindBlob(String name, @Nullable  Blob x) throws SQLException {
        return bind(name, pc->pc.setBlob(x));
    }

    public E bindBlob(String name, @Nullable InputStream inputStream) throws SQLException {
        return bind(name, pc->pc.setBlob(inputStream));
    }

    public E bindBlob(String name, @Nullable InputStream inputStream, long length) throws SQLException {
        return bind(name, pc->pc.setBlob(inputStream, length));
    }

    public E bindBoolean(String name, boolean x) throws SQLException {
        return bind(name, pc-> pc.setBoolean(x));
    }

    public E bindByte(String name, byte x) throws SQLException {
        return bind(name, pc->pc.setByte(x));
    }

    public E bindBytes(String name, byte[] x) throws SQLException {
        return bind(name, pc->pc.setBytes(x));
    }

    public E bindCharacterStream(String name, @Nullable Reader reader) throws SQLException {
        return bind(name, pc->pc.setCharacterStream(reader));
    }

    public E bindCharacterStream(String name, @Nullable Reader reader, int length) throws SQLException {
        return bind(name, pc->pc.setCharacterStream(reader, length));
    }

    public E bindCharacterStream(String name, @Nullable Reader reader, long length) throws SQLException {
        return bind(name, pc->pc.setCharacterStream(reader, length));
    }

    public E bindClob(String name, @Nullable Clob x) throws SQLException {
        return bind(name, pc->pc.setClob(x));
    }

    public E bindClob(String name, @Nullable Reader reader) throws SQLException {
        return bind(name, pc->pc.setClob(reader));
    }

    public E bindClob(String name, @Nullable Reader reader, long length) throws SQLException {
        return bind(name, pc->pc.setClob(reader, length));
    }

    public E bindDate(String name, @Nullable Date x) throws SQLException {
        return bind(name, pc->pc.setDate(x));
    }

    public E bindDate(String name, @Nullable Date x, Calendar cal) throws SQLException {
        return bind(name, pc->pc.setDate(x, cal));
    }

    public E bindDouble(String name, double x) throws SQLException {
        return bind(name, pc->pc.setDouble(x));
    }

    public E bindFloat(String name, float x) throws SQLException {
        return bind(name, pc->pc.setFloat(x));
    }

    public E bindInt(String name, int x) throws SQLException {
        return bind(name, pc->pc.setInt(x));
    }

    public E bindLong(String name, long x) throws SQLException {
        return bind(name, pc->pc.setLong(x));
    }

    public E bindNCharacterStream(String name, @Nullable Reader value) throws SQLException {
        return bind(name, pc->pc.setNCharacterStream(value));
    }

    public E bindNCharacterStream(String name, @Nullable Reader value, long length) throws SQLException {
        return bind(name, pc->pc.setNCharacterStream(value, length));
    }

    public E bindNClob(String name, @Nullable NClob value) throws SQLException {
        return bind(name, pc->pc.setNClob(value));
    }

    public E bindNClob(String name, @Nullable Reader reader) throws SQLException {
        return bind(name, pc->pc.setNClob(reader));
    }

    public E bindNClob(String name, @Nullable Reader reader, long length) throws SQLException {
        return bind(name, pc->pc.setNClob(reader, length));
    }

    public E bindNString(String name, @Nullable String value) throws SQLException {
        return bind(name, pc->pc.setNString(value));
    }

    public E bindNull(String name, int sqlType) throws SQLException {
        return bind(name, pc -> pc.setNull(sqlType));
    }

    public E bindNull(String name, int sqlType, String typeName) throws SQLException {
        return bind(name, pc->pc.setNull(sqlType, typeName));
    }

    public E bindObject(String name, @Nullable Object x) throws SQLException {
        return bind(name, pc->pc.setObject(x));
    }

    public E bindObject(String name, @Nullable Object x, int targetSqlType) throws SQLException {
        return bind(name, pc->pc.setObject(x, targetSqlType));
    }

    public E bindObject(String name, @Nullable Object x, SQLType targetSqlType) throws SQLException {
        return bind(name, pc -> pc.setObject(x, targetSqlType));
    }

    public E bindObject(String name, @Nullable Object x, int targetSqlType, int scaleOrLength) throws SQLException {
        return bind(name, pc->pc.setObject(x, targetSqlType, scaleOrLength));
    }

    public E bindObject(String name, @Nullable Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {
        return bind(name, pc->pc.setObject(x, targetSqlType, scaleOrLength));
    }

    public E bindRef(String name, @Nullable Ref x) throws SQLException {
        return bind(name, pc->pc.setRef(x));
    }

    public E bindShort(String name, short x) throws SQLException {
        return bind(name, pc->pc.setShort(x));
    }

    public E bindSQLXML(String name, @Nullable SQLXML xmlObject) throws SQLException {
        return bind(name, pc->pc.setSQLXML(xmlObject));
    }

    public E bindString(String name, @Nullable String x) throws SQLException {
        return bind(name, pc->pc.setString(x));
    }

    public E bindTime(String name, @Nullable Time x) throws SQLException {
        return bind(name, pc->pc.setTime(x));
    }

    public E bindTimestamp(String name, @Nullable Timestamp x) throws SQLException {
        return bind(name, pc->pc.setTimestamp(x));
    }

    public E bindTime(String name, @Nullable Time x, Calendar cal) throws SQLException {
        return bind(name, pc->pc.setTime(x, cal));
    }

    public E bindTimestamp(String name, @Nullable Timestamp x, Calendar cal) throws SQLException {
        return bind(name, pc->pc.setTimestamp(x, cal));
    }

    public E bindURL(String name, @Nullable URL x) throws SQLException {
        return bind(name, pc->pc.setURL(x));
    }

    public E bindList(String name, List<Binding> bindings){
        if(!statement.containsParameter(name)){
            throw new IllegalArgumentException("\""+name+"\" is not a named parameter");
        }

        return factory.make(statement, this.bindings.addListBinding(name, bindings));
    }

    public E bindLongs(String name, long... xs){
        if (xs == null) {
            throw new IllegalArgumentException("xs cannot be null - consider using bindArray");
        }
        final List<Binding> bindings = new ArrayList<>();
        for (final long x : xs) {
            bindings.add(pc -> pc.setLong(x));
        }

        return bindList(name, bindings);
    }

    public E bindStrings(String name, List<String> xs){
        if (xs == null) {
            throw new IllegalArgumentException("xs cannot be null - consider using bindArray");
        }
        final Function<String, Binding> createBinding = x -> (Binding) preparedColumn -> preparedColumn.setString(x);
        final List<Binding> bindings = xs.stream().map(createBinding).collect(Collectors.toList());
        return bindList(name, bindings);
    }

    public E bindStrings(String name, String... xs){
        if (xs == null) {
            throw new IllegalArgumentException("xs cannot be null - consider using bindArray");
        }
        return bindStrings(name, Arrays.asList(xs));
    }

}
