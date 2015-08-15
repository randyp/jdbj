package com.github.randyp.jdbj;

import com.github.randyp.jdbj.lambda.Binding;

import javax.annotation.Nullable;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;

public interface DefaultValueBindingsBuilder<E> extends ValueBindingsBuilder<E> {

    E bindDefault(String name, Binding binding);

    default E bindDefaultArray(String name, @Nullable Array x) {
        return bindDefault(name, pc -> pc.setArray(x));
    }

    default E bindDefaultAsciiStream(String name, @Nullable InputStream x) {
        return bindDefault(name, pc -> pc.setAsciiStream(x));
    }

    default E bindDefaultAsciiStream(String name, @Nullable InputStream x, int length) {
        return bindDefault(name, pc -> pc.setAsciiStream(x, length));
    }

    default E bindDefaultAsciiStream(String name, @Nullable InputStream x, long length) {
        return bindDefault(name, pc -> pc.setAsciiStream(x, length));
    }

    default E bindDefaultBigDecimal(String name, @Nullable BigDecimal x) {
        return bindDefault(name, pc -> pc.setBigDecimal(x));
    }

    default E bindDefaultBinaryStream(String name, @Nullable InputStream x) {
        return bindDefault(name, pc -> pc.setBinaryStream(x));
    }

    default E bindDefaultBinaryStream(String name, @Nullable InputStream x, long length) {
        return bindDefault(name, pc -> pc.setBinaryStream(x, length));
    }

    default E bindDefaultBinaryStream(String name, @Nullable InputStream x, int length) {
        return bindDefault(name, pc -> pc.setBinaryStream(x, length));
    }

    default E bindDefaultBlob(String name, @Nullable Blob x) {
        return bindDefault(name, pc -> pc.setBlob(x));
    }

    default E bindDefaultBlob(String name, @Nullable InputStream inputStream) {
        return bindDefault(name, pc -> pc.setBlob(inputStream));
    }

    default E bindDefaultBlob(String name, @Nullable InputStream inputStream, long length) {
        return bindDefault(name, pc -> pc.setBlob(inputStream, length));
    }

    default E bindDefaultBoolean(String name, boolean x) {
        return bindDefault(name, pc -> pc.setBoolean(x));
    }

    default E bindDefaultByte(String name, byte x) {
        return bindDefault(name, pc -> pc.setByte(x));
    }

    default E bindDefaultBytes(String name, byte[] x) {
        return bindDefault(name, pc -> pc.setBytes(x));
    }

    default E bindDefaultCharacterStream(String name, @Nullable Reader reader) {
        return bindDefault(name, pc -> pc.setCharacterStream(reader));
    }

    default E bindDefaultCharacterStream(String name, @Nullable Reader reader, int length) {
        return bindDefault(name, pc -> pc.setCharacterStream(reader, length));
    }

    default E bindDefaultCharacterStream(String name, @Nullable Reader reader, long length) {
        return bindDefault(name, pc -> pc.setCharacterStream(reader, length));
    }

    default E bindDefaultClob(String name, @Nullable Clob x) {
        return bindDefault(name, pc -> pc.setClob(x));
    }

    default E bindDefaultClob(String name, @Nullable Reader reader) {
        return bindDefault(name, pc -> pc.setClob(reader));
    }

    default E bindDefaultClob(String name, @Nullable Reader reader, long length) {
        return bindDefault(name, pc -> pc.setClob(reader, length));
    }

    default E bindDefaultDate(String name, @Nullable Date x) {
        return bindDefault(name, pc -> pc.setDate(x));
    }

    default E bindDefaultDate(String name, @Nullable Date x, Calendar cal) {
        return bindDefault(name, pc -> pc.setDate(x, cal));
    }

    default E bindDefaultDouble(String name, double x) {
        return bindDefault(name, pc -> pc.setDouble(x));
    }

    default E bindDefaultFloat(String name, float x) {
        return bindDefault(name, pc -> pc.setFloat(x));
    }

    default E bindDefaultInt(String name, int x) {
        return bindDefault(name, pc -> pc.setInt(x));
    }

    default E bindDefaultLong(String name, long x) {
        return bindDefault(name, pc -> pc.setLong(x));
    }

    default E bindDefaultNCharacterStream(String name, @Nullable Reader value) {
        return bindDefault(name, pc -> pc.setNCharacterStream(value));
    }

    default E bindDefaultNCharacterStream(String name, @Nullable Reader value, long length) {
        return bindDefault(name, pc -> pc.setNCharacterStream(value, length));
    }

    default E bindDefaultNClob(String name, @Nullable NClob value) {
        return bindDefault(name, pc -> pc.setNClob(value));
    }

    default E bindDefaultNClob(String name, @Nullable Reader reader) {
        return bindDefault(name, pc -> pc.setNClob(reader));
    }

    default E bindDefaultNClob(String name, @Nullable Reader reader, long length) {
        return bindDefault(name, pc -> pc.setNClob(reader, length));
    }

    default E bindDefaultNString(String name, @Nullable String value) {
        return bindDefault(name, pc -> pc.setNString(value));
    }

    default E bindDefaultNull(String name, int sqlType) {
        return bindDefault(name, pc -> pc.setNull(sqlType));
    }

    default E bindDefaultNull(String name, int sqlType, String typeName) {
        return bindDefault(name, pc -> pc.setNull(sqlType, typeName));
    }

    default E bindDefaultObject(String name, @Nullable Object x) {
        return bindDefault(name, pc -> pc.setObject(x));
    }

    default E bindDefaultObject(String name, @Nullable Object x, int targetSqlType) {
        return bindDefault(name, pc -> pc.setObject(x, targetSqlType));
    }

    default E bindDefaultObject(String name, @Nullable Object x, SQLType targetSqlType) {
        return bindDefault(name, pc -> pc.setObject(x, targetSqlType));
    }

    default E bindDefaultObject(String name, @Nullable Object x, int targetSqlType, int scaleOrLength) {
        return bindDefault(name, pc -> pc.setObject(x, targetSqlType, scaleOrLength));
    }

    default E bindDefaultObject(String name, @Nullable Object x, SQLType targetSqlType, int scaleOrLength) {
        return bindDefault(name, pc -> pc.setObject(x, targetSqlType, scaleOrLength));
    }

    default E bindDefaultRef(String name, @Nullable Ref x) {
        return bindDefault(name, pc -> pc.setRef(x));
    }

    default E bindDefaultShort(String name, short x) {
        return bindDefault(name, pc -> pc.setShort(x));
    }

    default E bindDefaultSQLXML(String name, @Nullable SQLXML xmlObject) {
        return bindDefault(name, pc -> pc.setSQLXML(xmlObject));
    }

    default E bindDefaultString(String name, @Nullable String x) {
        return bindDefault(name, pc -> pc.setString(x));
    }

    default E bindDefaultTime(String name, @Nullable Time x) {
        return bindDefault(name, pc -> pc.setTime(x));
    }

    default E bindDefaultTimestamp(String name, @Nullable Timestamp x) {
        return bindDefault(name, pc -> pc.setTimestamp(x));
    }

    default E bindDefaultTime(String name, @Nullable Time x, Calendar cal) {
        return bindDefault(name, pc -> pc.setTime(x, cal));
    }

    default E bindDefaultTimestamp(String name, @Nullable Timestamp x, Calendar cal) {
        return bindDefault(name, pc -> pc.setTimestamp(x, cal));
    }

    default E bindDefaultURL(String name, @Nullable URL x) {
        return bindDefault(name, pc -> pc.setURL(x));
    }
}
