package oof.jdbc;

import oof.jdbc.lambda.Binding;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;

public abstract class DecoratesBindingBuilder<E extends DecoratesBindingBuilder<E>> {
    
    final BindingsBuilder bindingsBuilder;

    DecoratesBindingBuilder(BindingsBuilder bindingsBuilder) {
        this.bindingsBuilder = bindingsBuilder;
    }

    public final E bind(String name, Binding binding) {
        return prototype(bindingsBuilder.bind(name, binding));
    }

    public final E bindClob(String name, Reader reader, long length) throws SQLException {
        return prototype(bindingsBuilder.bindClob(name, reader, length));
    }

    public final E bindDouble(String name, double x) throws SQLException {
        return prototype(bindingsBuilder.bindDouble(name, x));
    }

    public final E bindObject(String name, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
        return prototype(bindingsBuilder.bindObject(name, x, targetSqlType, scaleOrLength));
    }

    public final E bindBigDecimal(String name, BigDecimal x) throws SQLException {
        return prototype(bindingsBuilder.bindBigDecimal(name, x));
    }

    public final E bindObject(String name, Object x, SQLType targetSqlType) throws SQLException {
        return prototype(bindingsBuilder.bindObject(name, x, targetSqlType));
    }

    public final E bindInt(String name, int x) throws SQLException {
        return prototype(bindingsBuilder.bindInt(name, x));
    }

    public final E bindBinaryStream(String name, InputStream x) throws SQLException {
        return prototype(bindingsBuilder.bindBinaryStream(name, x));
    }

    public final E bindFloat(String name, float x) throws SQLException {
        return prototype(bindingsBuilder.bindFloat(name, x));
    }

    public final E bindAsciiStream(String name, InputStream x, long length) throws SQLException {
        return prototype(bindingsBuilder.bindAsciiStream(name, x, length));
    }

    public final E bindByte(String name, byte x) throws SQLException {
        return prototype(bindingsBuilder.bindByte(name, x));
    }

    public final E bindShort(String name, short x) throws SQLException {
        return prototype(bindingsBuilder.bindShort(name, x));
    }

    public final E bindAsciiStream(String name, InputStream x, int length) throws SQLException {
        return prototype(bindingsBuilder.bindAsciiStream(name, x, length));
    }

    public final E bindString(String name, String x) throws SQLException {
        return prototype(bindingsBuilder.bindString(name, x));
    }

    public final E bindBlob(String name, Blob x) throws SQLException {
        return prototype(bindingsBuilder.bindBlob(name, x));
    }

    public final E bindBlob(String name, InputStream inputStream) throws SQLException {
        return prototype(bindingsBuilder.bindBlob(name, inputStream));
    }

    public final E bindObject(String name, Object x) throws SQLException {
        return prototype(bindingsBuilder.bindObject(name, x));
    }

    public final E bindBinaryStream(String name, InputStream x, int length) throws SQLException {
        return prototype(bindingsBuilder.bindBinaryStream(name, x, length));
    }

    public final E bindRef(String name, Ref x) throws SQLException {
        return prototype(bindingsBuilder.bindRef(name, x));
    }

    public final E bindTime(String name, Time x, Calendar cal) throws SQLException {
        return prototype(bindingsBuilder.bindTime(name, x, cal));
    }

    public final E bindDate(String name, Date x) throws SQLException {
        return prototype(bindingsBuilder.bindDate(name, x));
    }

    public final E bindBoolean(String name, boolean x) throws SQLException {
        return prototype(bindingsBuilder.bindBoolean(name, x));
    }

    public final E bindTimestamp(String name, Timestamp x, Calendar cal) throws SQLException {
        return prototype(bindingsBuilder.bindTimestamp(name, x, cal));
    }

    public final E bindTime(String name, Time x) throws SQLException {
        return prototype(bindingsBuilder.bindTime(name, x));
    }

    public final E bindBytes(String name, byte[] x) throws SQLException {
        return prototype(bindingsBuilder.bindBytes(name, x));
    }

    public final E bindObject(String name, Object x, int targetSqlType) throws SQLException {
        return prototype(bindingsBuilder.bindObject(name, x, targetSqlType));
    }

    public final E bindAsciiStream(String name, InputStream x) throws SQLException {
        return prototype(bindingsBuilder.bindAsciiStream(name, x));
    }

    public final E bindNClob(String name, Reader reader) throws SQLException {
        return prototype(bindingsBuilder.bindNClob(name, reader));
    }

    public final E bindBinaryStream(String name, InputStream x, long length) throws SQLException {
        return prototype(bindingsBuilder.bindBinaryStream(name, x, length));
    }

    public final E bindCharacterStream(String name, Reader reader, long length) throws SQLException {
        return prototype(bindingsBuilder.bindCharacterStream(name, reader, length));
    }

    public final E bindNClob(String name, NClob value) throws SQLException {
        return prototype(bindingsBuilder.bindNClob(name, value));
    }

    public final E bindNCharacterStream(String name, Reader value, long length) throws SQLException {
        return prototype(bindingsBuilder.bindNCharacterStream(name, value, length));
    }

    public final E bindTimestamp(String name, Timestamp x) throws SQLException {
        return prototype(bindingsBuilder.bindTimestamp(name, x));
    }

    public final E bindBlob(String name, InputStream inputStream, long length) throws SQLException {
        return prototype(bindingsBuilder.bindBlob(name, inputStream, length));
    }

    public final E bindCharacterStream(String name, Reader reader, int length) throws SQLException {
        return prototype(bindingsBuilder.bindCharacterStream(name, reader, length));
    }

    public final E bindNull(String name, int sqlType, String typeName) throws SQLException {
        return prototype(bindingsBuilder.bindNull(name, sqlType, typeName));
    }

    public final E bindDate(String name, Date x, Calendar cal) throws SQLException {
        return prototype(bindingsBuilder.bindDate(name, x, cal));
    }

    public final E bindClob(String name, Reader reader) throws SQLException {
        return prototype(bindingsBuilder.bindClob(name, reader));
    }

    public final E bindLong(String name, long x) throws SQLException {
        return prototype(bindingsBuilder.bindLong(name, x));
    }

    public final E bindSQLXML(String name, SQLXML xmlObject) throws SQLException {
        return prototype(bindingsBuilder.bindSQLXML(name, xmlObject));
    }

    public final E bindArray(String name, Array x) throws SQLException {
        return prototype(bindingsBuilder.bindArray(name, x));
    }

    public final E bindObject(String name, Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {
        return prototype(bindingsBuilder.bindObject(name, x, targetSqlType, scaleOrLength));
    }

    public final E bindNString(String name, String value) throws SQLException {
        return prototype(bindingsBuilder.bindNString(name, value));
    }

    public final E bindClob(String name, Clob x) throws SQLException {
        return prototype(bindingsBuilder.bindClob(name, x));
    }

    public final E bindNull(String name, int sqlType) throws SQLException {
        return prototype(bindingsBuilder.bindNull(name, sqlType));
    }

    public final E bindURL(String name, URL x) throws SQLException {
        return prototype(bindingsBuilder.bindURL(name, x));
    }

    public final E bindNCharacterStream(String name, Reader value) throws SQLException {
        return prototype(bindingsBuilder.bindNCharacterStream(name, value));
    }

    public final E bindNClob(String name, Reader reader, long length) throws SQLException {
        return prototype(bindingsBuilder.bindNClob(name, reader, length));
    }

    public final E bindCharacterStream(String name, Reader reader) throws SQLException {
        return prototype(bindingsBuilder.bindCharacterStream(name, reader));
    }

    abstract E prototype(BindingsBuilder newBindings);
}
