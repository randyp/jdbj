package oof.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;

public abstract class DecoratesBindingBuilder<E extends DecoratesBindingBuilder<E>> {
    
    final BindingsBuilder bindingsBuilder;

    public DecoratesBindingBuilder(BindingsBuilder bindingsBuilder) {
        this.bindingsBuilder = bindingsBuilder;
    }

    public E bind(String name, Binding binding) {
        return prototype(bindingsBuilder.bind(name, binding));
    }

    public E bindClob(String name, Reader reader, long length) throws SQLException {
        return prototype(bindingsBuilder.bindClob(name, reader, length));
    }

    public E bindDouble(String name, double x) throws SQLException {
        return prototype(bindingsBuilder.bindDouble(name, x));
    }

    public E bindObject(String name, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
        return prototype(bindingsBuilder.bindObject(name, x, targetSqlType, scaleOrLength));
    }

    public E bindBigDecimal(String name, BigDecimal x) throws SQLException {
        return prototype(bindingsBuilder.bindBigDecimal(name, x));
    }

    public E bindObject(String name, Object x, SQLType targetSqlType) throws SQLException {
        return prototype(bindingsBuilder.bindObject(name, x, targetSqlType));
    }

    public E bindInt(String name, int x) throws SQLException {
        return prototype(bindingsBuilder.bindInt(name, x));
    }

    public E bindBinaryStream(String name, InputStream x) throws SQLException {
        return prototype(bindingsBuilder.bindBinaryStream(name, x));
    }

    public E bindFloat(String name, float x) throws SQLException {
        return prototype(bindingsBuilder.bindFloat(name, x));
    }

    public E bindAsciiStream(String name, InputStream x, long length) throws SQLException {
        return prototype(bindingsBuilder.bindAsciiStream(name, x, length));
    }

    public E bindByte(String name, byte x) throws SQLException {
        return prototype(bindingsBuilder.bindByte(name, x));
    }

    public E bindShort(String name, short x) throws SQLException {
        return prototype(bindingsBuilder.bindShort(name, x));
    }

    public E bindAsciiStream(String name, InputStream x, int length) throws SQLException {
        return prototype(bindingsBuilder.bindAsciiStream(name, x, length));
    }

    public E bindString(String name, String x) throws SQLException {
        return prototype(bindingsBuilder.bindString(name, x));
    }

    public E bindBlob(String name, Blob x) throws SQLException {
        return prototype(bindingsBuilder.bindBlob(name, x));
    }

    public E bindBlob(String name, InputStream inputStream) throws SQLException {
        return prototype(bindingsBuilder.bindBlob(name, inputStream));
    }

    public E bindObject(String name, Object x) throws SQLException {
        return prototype(bindingsBuilder.bindObject(name, x));
    }

    public E bindBinaryStream(String name, InputStream x, int length) throws SQLException {
        return prototype(bindingsBuilder.bindBinaryStream(name, x, length));
    }

    public E bindRef(String name, Ref x) throws SQLException {
        return prototype(bindingsBuilder.bindRef(name, x));
    }

    public E bindTime(String name, Time x, Calendar cal) throws SQLException {
        return prototype(bindingsBuilder.bindTime(name, x, cal));
    }

    public E bindDate(String name, Date x) throws SQLException {
        return prototype(bindingsBuilder.bindDate(name, x));
    }

    public E bindBoolean(String name, boolean x) throws SQLException {
        return prototype(bindingsBuilder.bindBoolean(name, x));
    }

    public E bindTimestamp(String name, Timestamp x, Calendar cal) throws SQLException {
        return prototype(bindingsBuilder.bindTimestamp(name, x, cal));
    }

    public E bindTime(String name, Time x) throws SQLException {
        return prototype(bindingsBuilder.bindTime(name, x));
    }

    public E bindBytes(String name, byte[] x) throws SQLException {
        return prototype(bindingsBuilder.bindBytes(name, x));
    }

    public E bindObject(String name, Object x, int targetSqlType) throws SQLException {
        return prototype(bindingsBuilder.bindObject(name, x, targetSqlType));
    }

    public E bindAsciiStream(String name, InputStream x) throws SQLException {
        return prototype(bindingsBuilder.bindAsciiStream(name, x));
    }

    public E bindNClob(String name, Reader reader) throws SQLException {
        return prototype(bindingsBuilder.bindNClob(name, reader));
    }

    public E bindBinaryStream(String name, InputStream x, long length) throws SQLException {
        return prototype(bindingsBuilder.bindBinaryStream(name, x, length));
    }

    public E bindCharacterStream(String name, Reader reader, long length) throws SQLException {
        return prototype(bindingsBuilder.bindCharacterStream(name, reader, length));
    }

    public E bindNClob(String name, NClob value) throws SQLException {
        return prototype(bindingsBuilder.bindNClob(name, value));
    }

    public E bindNCharacterStream(String name, Reader value, long length) throws SQLException {
        return prototype(bindingsBuilder.bindNCharacterStream(name, value, length));
    }

    public E bindTimestamp(String name, Timestamp x) throws SQLException {
        return prototype(bindingsBuilder.bindTimestamp(name, x));
    }

    public E bindBlob(String name, InputStream inputStream, long length) throws SQLException {
        return prototype(bindingsBuilder.bindBlob(name, inputStream, length));
    }

    public E bindCharacterStream(String name, Reader reader, int length) throws SQLException {
        return prototype(bindingsBuilder.bindCharacterStream(name, reader, length));
    }

    public E bindNull(String name, int sqlType, String typeName) throws SQLException {
        return prototype(bindingsBuilder.bindNull(name, sqlType, typeName));
    }

    public E bindDate(String name, Date x, Calendar cal) throws SQLException {
        return prototype(bindingsBuilder.bindDate(name, x, cal));
    }

    public E bindClob(String name, Reader reader) throws SQLException {
        return prototype(bindingsBuilder.bindClob(name, reader));
    }

    public E bindLong(String name, long x) throws SQLException {
        return prototype(bindingsBuilder.bindLong(name, x));
    }

    public E bindSQLXML(String name, SQLXML xmlObject) throws SQLException {
        return prototype(bindingsBuilder.bindSQLXML(name, xmlObject));
    }

    public E bindArray(String name, Array x) throws SQLException {
        return prototype(bindingsBuilder.bindArray(name, x));
    }

    public E bindObject(String name, Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {
        return prototype(bindingsBuilder.bindObject(name, x, targetSqlType, scaleOrLength));
    }

    public E bindNString(String name, String value) throws SQLException {
        return prototype(bindingsBuilder.bindNString(name, value));
    }

    public E bindClob(String name, Clob x) throws SQLException {
        return prototype(bindingsBuilder.bindClob(name, x));
    }

    public E bindNull(String name, int sqlType) throws SQLException {
        return prototype(bindingsBuilder.bindNull(name, sqlType));
    }

    public E bindURL(String name, URL x) throws SQLException {
        return prototype(bindingsBuilder.bindURL(name, x));
    }

    public E bindNCharacterStream(String name, Reader value) throws SQLException {
        return prototype(bindingsBuilder.bindNCharacterStream(name, value));
    }

    public E bindNClob(String name, Reader reader, long length) throws SQLException {
        return prototype(bindingsBuilder.bindNClob(name, reader, length));
    }

    public E bindCharacterStream(String name, Reader reader) throws SQLException {
        return prototype(bindingsBuilder.bindCharacterStream(name, reader));
    }

    protected  abstract E prototype(BindingsBuilder newBindings);
}
