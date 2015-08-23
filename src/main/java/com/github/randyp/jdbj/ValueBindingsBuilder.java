package com.github.randyp.jdbj;

import com.github.randyp.jdbj.lambda.Binding;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;

/**
 *
 * @param <P> prototype type since {@link Immutable}
 */
interface ValueBindingsBuilder<P> {

    P bind(String name, Binding binding);

    default P bindArray(String name, @Nullable Array x) {
        return bind(name, pc -> pc.setArray(x));
    }

    default P bindAsciiStream(String name, @Nullable InputStream x) {
        return bind(name, pc -> pc.setAsciiStream(x));
    }

    default P bindAsciiStream(String name, @Nullable InputStream x, int length) {
        return bind(name, pc -> pc.setAsciiStream(x, length));
    }

    default P bindAsciiStream(String name, @Nullable InputStream x, long length) {
        return bind(name, pc -> pc.setAsciiStream(x, length));
    }

    default P bindBigDecimal(String name, @Nullable BigDecimal x) {
        return bind(name, pc -> pc.setBigDecimal(x));
    }

    default P bindBinaryStream(String name, @Nullable InputStream x) {
        return bind(name, pc -> pc.setBinaryStream(x));
    }

    default P bindBinaryStream(String name, @Nullable InputStream x, long length) {
        return x == null
                ? bind(name, pc -> pc.setNull(Types.BINARY))
                : bind(name, pc -> pc.setBinaryStream(x, length));
    }

    default P bindBinaryStream(String name, @Nullable InputStream x, int length) {
        return bind(name, pc -> pc.setBinaryStream(x, length));
    }

    default P bindBlob(String name, @Nullable Blob x) {
        return bind(name, pc -> pc.setBlob(x));
    }

    default P bindBlob(String name, @Nullable InputStream inputStream) {
        return bind(name, pc -> pc.setBlob(inputStream));
    }

    default P bindBlob(String name, @Nullable InputStream inputStream, long length) {
        return bind(name, pc -> pc.setBlob(inputStream, length));
    }

    default P bindBoolean(String name, Boolean x) {
        return bind(name, pc -> pc.setBoolean(x));
    }

    default P bindBooleanPrimitive(String name, boolean x) {
        return bind(name, pc -> pc.setBooleanPrimitive(x));
    }

    default P bindByte(String name, Byte x) {
        return bind(name, pc -> pc.setByte(x));
    }

    default P bindBytePrimitive(String name, byte x) {
        return bind(name, pc -> pc.setBytePrimitive(x));
    }

    default P bindByteArray(String name, byte[] x) {
        return bind(name, pc -> pc.setBytes(x));
    }

    default P bindCharacterStream(String name, @Nullable Reader reader) {
        return bind(name, pc -> pc.setCharacterStream(reader));
    }

    default P bindCharacterStream(String name, @Nullable Reader reader, int length) {
        return bind(name, pc -> pc.setCharacterStream(reader, length));
    }

    default P bindCharacterStream(String name, @Nullable Reader reader, long length) {
        return bind(name, pc -> pc.setCharacterStream(reader, length));
    }

    default P bindClob(String name, @Nullable Clob x) {
        return bind(name, pc -> pc.setClob(x));
    }

    default P bindClob(String name, @Nullable Reader reader) {
        return bind(name, pc -> pc.setClob(reader));
    }

    default P bindClob(String name, @Nullable Reader reader, long length) {
        return bind(name, pc -> pc.setClob(reader, length));
    }

    default P bindDate(String name, @Nullable Date x) {
        return bind(name, pc -> pc.setDate(x));
    }

    default P bindDate(String name, @Nullable Date x, Calendar cal) {
        return bind(name, pc -> pc.setDate(x, cal));
    }

    default P bindDouble(String name, Double x) {
        return bind(name, pc -> pc.setDouble(x));
    }

    default P bindDoublePrimitive(String name, double x) {
        return bind(name, pc -> pc.setDoublePrimitive(x));
    }

    default P bindFloat(String name, Float x) {
        return bind(name, pc -> pc.setFloat(x));
    }

    default P bindFloatPrimitive(String name, float x) {
        return bind(name, pc -> pc.setFloatPrimitive(x));
    }

    default P bindInteger(String name, Integer x) {
        return bind(name, pc -> pc.setInteger(x));
    }

    default P bindIntegerPrimitive(String name, int x) {
        return bind(name, pc -> pc.setIntegerPrimitive(x));
    }

    default P bindInt(String name, int x) {
        return bind(name, pc -> pc.setInt(x));
    }

    default P bindLong(String name, Long x) {
        return bind(name, pc -> pc.setLong(x));
    }

    default P bindLongPrimitive(String name, long x) {
        return bind(name, pc -> pc.setLongPrimitive(x));
    }

    default P bindNCharacterStream(String name, @Nullable Reader value) {
        return bind(name, pc -> pc.setNCharacterStream(value));
    }

    default P bindNCharacterStream(String name, @Nullable Reader value, long length) {
        return bind(name, pc -> pc.setNCharacterStream(value, length));
    }

    default P bindNClob(String name, @Nullable NClob value) {
        return bind(name, pc -> pc.setNClob(value));
    }

    default P bindNClob(String name, @Nullable Reader reader) {
        return bind(name, pc -> pc.setNClob(reader));
    }

    default P bindNClob(String name, @Nullable Reader reader, long length) {
        return bind(name, pc -> pc.setNClob(reader, length));
    }

    default P bindNString(String name, @Nullable String value) {
        return bind(name, pc -> pc.setNString(value));
    }

    default P bindNull(String name, int sqlType) {
        return bind(name, pc -> pc.setNull(sqlType));
    }

    default P bindNull(String name, int sqlType, String typeName) {
        return bind(name, pc -> pc.setNull(sqlType, typeName));
    }

    default P bindObject(String name, @Nullable Object x) {
        return bind(name, pc -> pc.setObject(x));
    }

    default P bindObject(String name, @Nullable Object x, int targetSqlType) {
        return bind(name, pc -> pc.setObject(x, targetSqlType));
    }

    default P bindObject(String name, @Nullable Object x, SQLType targetSqlType) {
        return bind(name, pc -> pc.setObject(x, targetSqlType));
    }

    default P bindObject(String name, @Nullable Object x, int targetSqlType, int scaleOrLength) {
        return bind(name, pc -> pc.setObject(x, targetSqlType, scaleOrLength));
    }

    default P bindObject(String name, @Nullable Object x, SQLType targetSqlType, int scaleOrLength) {
        return bind(name, pc -> pc.setObject(x, targetSqlType, scaleOrLength));
    }

    default P bindShort(String name, Short x) {
        return bind(name, pc -> pc.setShort(x));
    }

    default P bindShortPrimitive(String name, short x) {
        return bind(name, pc -> pc.setShortPrimitive(x));
    }

    default P bindSQLXML(String name, @Nullable SQLXML xmlObject) {
        return bind(name, pc -> pc.setSQLXML(xmlObject));
    }

    default P bindString(String name, @Nullable String x) {
        return bind(name, pc -> pc.setString(x));
    }

    default P bindTime(String name, @Nullable Time x) {
        return bind(name, pc -> pc.setTime(x));
    }

    default P bindTime(String name, @Nullable Time x, Calendar cal) {
        return bind(name, pc -> pc.setTime(x, cal));
    }

    default P bindTimestamp(String name, @Nullable Timestamp x) {
        return bind(name, pc -> pc.setTimestamp(x));
    }

    default P bindTimestamp(String name, @Nullable Timestamp x, Calendar cal) {
        return bind(name, pc -> pc.setTimestamp(x, cal));
    }

    default P bindURL(String name, @Nullable URL x) {
        return bind(name, pc -> pc.setURL(x));
    }

}
