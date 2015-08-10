package oof.jdbc;


import javax.annotation.concurrent.Immutable;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.sql.Date;
import java.util.*;

@Immutable
public class BindingsBuilder {

    final NamedParameterStatement statement;
    final Map<String, Binding> bindings;

    public BindingsBuilder(NamedParameterStatement statement) {
        this(statement, new HashMap<>());
    }

    BindingsBuilder(NamedParameterStatement statement, Map<String, Binding> bindings){
        this.statement = statement;
        this.bindings = bindings;
    }

    public BindingsBuilder bind(String name, Binding binding){
        if(!statement.containsParameter(name)){
            throw new IllegalArgumentException("\""+name+"\" is not a named parameter");
        }
        if(bindings.containsKey(name)){
            throw new IllegalArgumentException("named parameter \""+name+"\" already has a binding");
        }

        final Map<String, Binding> newBindings = new HashMap<>(bindings);
        newBindings.put(name, binding);

        return new BindingsBuilder(statement, newBindings);
    }

    public BindingsBuilder bindNull(String name, int sqlType) throws SQLException {
        return bind(name, pc -> pc.setNull(sqlType));
    }

    public BindingsBuilder bindBoolean(String name, boolean x) throws SQLException {
        return bind(name, pc-> pc.setBoolean(x));
    }

    public BindingsBuilder bindByte(String name, byte x) throws SQLException {
        return bind(name, pc->pc.setByte(x));
    }

    public BindingsBuilder bindShort(String name, short x) throws SQLException {
        return bind(name, pc->pc.setShort(x));
    }

    public BindingsBuilder bindInt(String name, int x) throws SQLException {
        return bind(name, pc->pc.setInt(x));
    }

    public BindingsBuilder bindLong(String name, long x) throws SQLException {
        return bind(name, pc->pc.setLong(x));
    }

    public BindingsBuilder bindFloat(String name, float x) throws SQLException {
        return bind(name, pc->pc.setFloat(x));
    }

    public BindingsBuilder bindDouble(String name, double x) throws SQLException {
        return bind(name, pc->pc.setDouble(x));
    }

    public BindingsBuilder bindBigDecimal(String name, BigDecimal x) throws SQLException {
        return bind(name, pc->pc.setBigDecimal(x));
    }

    public BindingsBuilder bindString(String name, String x) throws SQLException {
        return bind(name, pc->pc.setString(x));
    }

    public BindingsBuilder bindBytes(String name, byte[] x) throws SQLException {
        return bind(name, pc->pc.setBytes(x));
    }

    public BindingsBuilder bindDate(String name, Date x) throws SQLException {
        return bind(name, pc->pc.setDate(x));
    }

    public BindingsBuilder bindTime(String name, Time x) throws SQLException {
        return bind(name, pc->pc.setTime(x));
    }

    public BindingsBuilder bindTimestamp(String name, Timestamp x) throws SQLException {
        return bind(name, pc->pc.setTimestamp(x));
    }

    public BindingsBuilder bindAsciiStream(String name, InputStream x, int length) throws SQLException {
        return bind(name, pc->pc.setAsciiStream(x, length));
    }

    public BindingsBuilder bindBinaryStream(String name, InputStream x, int length) throws SQLException {
        return bind(name, pc->pc.setBinaryStream(x, length));
    }

    public BindingsBuilder bindObject(String name, Object x, int targetSqlType) throws SQLException {
        return bind(name, pc->pc.setObject(x, targetSqlType));
    }

    public BindingsBuilder bindObject(String name, Object x) throws SQLException {
        return bind(name, pc->pc.setObject(x));
    }

    public BindingsBuilder bindCharacterStream(String name, Reader reader, int length) throws SQLException {
        return bind(name, pc->pc.setCharacterStream(reader, length));
    }

    public BindingsBuilder bindRef(String name, Ref x) throws SQLException {
        return bind(name, pc->pc.setRef(x));
    }

    public BindingsBuilder bindBlob(String name, Blob x) throws SQLException {
        return bind(name, pc->pc.setBlob(x));
    }

    public BindingsBuilder bindClob(String name, Clob x) throws SQLException {
        return bind(name, pc->pc.setClob(x));
    }

    public BindingsBuilder bindArray(String name, Array x) throws SQLException {
        return bind(name, pc->pc.setArray(x));
    }

    public BindingsBuilder bindDate(String name, Date x, Calendar cal) throws SQLException {
        return bind(name, pc->pc.setDate(x, cal));
    }

    public BindingsBuilder bindTime(String name, Time x, Calendar cal) throws SQLException {
        return bind(name, pc->pc.setTime(x, cal));
    }

    public BindingsBuilder bindTimestamp(String name, Timestamp x, Calendar cal) throws SQLException {
        return bind(name, pc->pc.setTimestamp(x, cal));
    }

    public BindingsBuilder bindNull(String name, int sqlType, String typeName) throws SQLException {
        return bind(name, pc->pc.setNull(sqlType, typeName));
    }

    public BindingsBuilder bindURL(String name, URL x) throws SQLException {
        return bind(name, pc->pc.setURL(x));
    }

    public BindingsBuilder bindNString(String name, String value) throws SQLException {
        return bind(name, pc->pc.setNString(value));
    }

    public BindingsBuilder bindNCharacterStream(String name, Reader value, long length) throws SQLException {
        return bind(name, pc->pc.setNCharacterStream(value, length));
    }

    public BindingsBuilder bindNClob(String name, NClob value) throws SQLException {
        return bind(name, pc->pc.setNClob(value));
    }

    public BindingsBuilder bindClob(String name, Reader reader, long length) throws SQLException {
        return bind(name, pc->pc.setClob(reader, length));
    }

    public BindingsBuilder bindBlob(String name, InputStream inputStream, long length) throws SQLException {
        return bind(name, pc->pc.setBlob(inputStream, length));
    }

    public BindingsBuilder bindNClob(String name, Reader reader, long length) throws SQLException {
        return bind(name, pc->pc.setNClob(reader, length));
    }

    public BindingsBuilder bindSQLXML(String name, SQLXML xmlObject) throws SQLException {
        return bind(name, pc->pc.setSQLXML(xmlObject));
    }

    public BindingsBuilder bindObject(String name, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
        return bind(name, pc->pc.setObject(x, targetSqlType, scaleOrLength));
    }

    public BindingsBuilder bindAsciiStream(String name, InputStream x, long length) throws SQLException {
        return bind(name, pc->pc.setAsciiStream(x, length));
    }

    public BindingsBuilder bindBinaryStream(String name, InputStream x, long length) throws SQLException {
        return bind(name, pc->pc.setBinaryStream(x, length));
    }

    public BindingsBuilder bindCharacterStream(String name, Reader reader, long length) throws SQLException {
        return bind(name, pc->pc.setCharacterStream(reader, length));
    }

    public BindingsBuilder bindAsciiStream(String name, InputStream x) throws SQLException {
        return bind(name, pc->pc.setAsciiStream(x));
    }

    public BindingsBuilder bindCharacterStream(String name, Reader reader) throws SQLException {
        return bind(name, pc->pc.setCharacterStream(reader));
    }

    public BindingsBuilder bindBinaryStream(String name, InputStream x) throws SQLException {
        return bind(name, pc->pc.setBinaryStream(x));
    }

    public BindingsBuilder bindNCharacterStream(String name, Reader value) throws SQLException {
        return bind(name, pc->pc.setNCharacterStream(value));
    }

    public BindingsBuilder bindClob(String name, Reader reader) throws SQLException {
        return bind(name, pc->pc.setClob(reader));
    }

    public BindingsBuilder bindBlob(String name, InputStream inputStream) throws SQLException {
        return bind(name, pc->pc.setBlob(inputStream));
    }

    public BindingsBuilder bindNClob(String name, Reader reader) throws SQLException {
        return bind(name, pc->pc.setNClob(reader));
    }

    public BindingsBuilder bindObject(String name, Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {
        return bind(name, pc->pc.setObject(x, targetSqlType, scaleOrLength));
    }

    public BindingsBuilder bindObject(String name, Object x, SQLType targetSqlType) throws SQLException {
        return bind(name, pc->pc.setObject(x, targetSqlType));
    }
}
