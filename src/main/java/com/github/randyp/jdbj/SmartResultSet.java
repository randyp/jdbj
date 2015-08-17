package com.github.randyp.jdbj;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.Map;

public class SmartResultSet implements AutoCloseable{

    private final ResultSet rs;

    public SmartResultSet(ResultSet rs) {
        this.rs = rs;
    }

    public boolean next() throws SQLException {
        return rs.next();
    }

    @Override
    public void close() throws SQLException {
        rs.close();
    }

    public String getString(int columnIndex) throws SQLException {
        return rs.getString(columnIndex);
    }

    public boolean getBoolean(int columnIndex) throws SQLException {
        return rs.getBoolean(columnIndex);
    }

    public byte getByte(int columnIndex) throws SQLException {
        return rs.getByte(columnIndex);
    }

    public short getShort(int columnIndex) throws SQLException {
        return rs.getShort(columnIndex);
    }

    public int getInt(int columnIndex) throws SQLException {
        return rs.getInt(columnIndex);
    }

    public long getLong(int columnIndex) throws SQLException {
        return rs.getLong(columnIndex);
    }

    public float getFloat(int columnIndex) throws SQLException {
        return rs.getFloat(columnIndex);
    }

    public double getDouble(int columnIndex) throws SQLException {
        return rs.getDouble(columnIndex);
    }

    @Deprecated
    public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
        //noinspection deprecation
        return rs.getBigDecimal(columnIndex, scale);
    }

    public byte[] getBytes(int columnIndex) throws SQLException {
        return rs.getBytes(columnIndex);
    }

    public Date getDate(int columnIndex) throws SQLException {
        return rs.getDate(columnIndex);
    }

    public Time getTime(int columnIndex) throws SQLException {
        return rs.getTime(columnIndex);
    }

    public Timestamp getTimestamp(int columnIndex) throws SQLException {
        return rs.getTimestamp(columnIndex);
    }

    public InputStream getAsciiStream(int columnIndex) throws SQLException {
        return rs.getAsciiStream(columnIndex);
    }

    @Deprecated
    public InputStream getUnicodeStream(int columnIndex) throws SQLException {
        //noinspection deprecation
        return rs.getUnicodeStream(columnIndex);
    }

    public InputStream getBinaryStream(int columnIndex) throws SQLException {
        return rs.getBinaryStream(columnIndex);
    }

    public String getString(String columnLabel) throws SQLException {
        return rs.getString(columnLabel);
    }

    public boolean getBoolean(String columnLabel) throws SQLException {
        return rs.getBoolean(columnLabel);
    }

    public byte getByte(String columnLabel) throws SQLException {
        return rs.getByte(columnLabel);
    }

    public short getShort(String columnLabel) throws SQLException {
        return rs.getShort(columnLabel);
    }

    public int getInt(String columnLabel) throws SQLException {
        return rs.getInt(columnLabel);
    }

    public long getLong(String columnLabel) throws SQLException {
        return rs.getLong(columnLabel);
    }

    public float getFloat(String columnLabel) throws SQLException {
        return rs.getFloat(columnLabel);
    }

    public double getDouble(String columnLabel) throws SQLException {
        return rs.getDouble(columnLabel);
    }

    @Deprecated
    public BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException {
        //noinspection deprecation
        return rs.getBigDecimal(columnLabel, scale);
    }

    public byte[] getBytes(String columnLabel) throws SQLException {
        return rs.getBytes(columnLabel);
    }

    public Date getDate(String columnLabel) throws SQLException {
        return rs.getDate(columnLabel);
    }

    public Time getTime(String columnLabel) throws SQLException {
        return rs.getTime(columnLabel);
    }

    public Timestamp getTimestamp(String columnLabel) throws SQLException {
        return rs.getTimestamp(columnLabel);
    }

    public InputStream getAsciiStream(String columnLabel) throws SQLException {
        return rs.getAsciiStream(columnLabel);
    }

    @Deprecated
    public InputStream getUnicodeStream(String columnLabel) throws SQLException {
        //noinspection deprecation
        return rs.getUnicodeStream(columnLabel);
    }

    public InputStream getBinaryStream(String columnLabel) throws SQLException {
        return rs.getBinaryStream(columnLabel);
    }

    public SQLWarning getWarnings() throws SQLException {
        return rs.getWarnings();
    }

    public ResultSetMetaData getMetaData() throws SQLException {
        return rs.getMetaData();
    }

    public Object getObject(int columnIndex) throws SQLException {
        return rs.getObject(columnIndex);
    }

    public Object getObject(String columnLabel) throws SQLException {
        return rs.getObject(columnLabel);
    }

    public int findColumn(String columnLabel) throws SQLException {
        return rs.findColumn(columnLabel);
    }

    public Reader getCharacterStream(int columnIndex) throws SQLException {
        return rs.getCharacterStream(columnIndex);
    }

    public Reader getCharacterStream(String columnLabel) throws SQLException {
        return rs.getCharacterStream(columnLabel);
    }

    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        return rs.getBigDecimal(columnIndex);
    }

    public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
        return rs.getBigDecimal(columnLabel);
    }

    public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {
        return rs.getObject(columnIndex, map);
    }

    public Ref getRef(int columnIndex) throws SQLException {
        return rs.getRef(columnIndex);
    }

    public Blob getBlob(int columnIndex) throws SQLException {
        return rs.getBlob(columnIndex);
    }

    public Clob getClob(int columnIndex) throws SQLException {
        return rs.getClob(columnIndex);
    }

    public Array getArray(int columnIndex) throws SQLException {
        return rs.getArray(columnIndex);
    }

    public Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException {
        return rs.getObject(columnLabel, map);
    }

    public Ref getRef(String columnLabel) throws SQLException {
        return rs.getRef(columnLabel);
    }

    public Blob getBlob(String columnLabel) throws SQLException {
        return rs.getBlob(columnLabel);
    }

    public Clob getClob(String columnLabel) throws SQLException {
        return rs.getClob(columnLabel);
    }

    public Array getArray(String columnLabel) throws SQLException {
        return rs.getArray(columnLabel);
    }

    public Date getDate(int columnIndex, Calendar cal) throws SQLException {
        return rs.getDate(columnIndex, cal);
    }

    public Date getDate(String columnLabel, Calendar cal) throws SQLException {
        return rs.getDate(columnLabel, cal);
    }

    public Time getTime(int columnIndex, Calendar cal) throws SQLException {
        return rs.getTime(columnIndex, cal);
    }

    public Time getTime(String columnLabel, Calendar cal) throws SQLException {
        return rs.getTime(columnLabel, cal);
    }

    public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
        return rs.getTimestamp(columnIndex, cal);
    }

    public Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException {
        return rs.getTimestamp(columnLabel, cal);
    }

    public URL getURL(int columnIndex) throws SQLException {
        return rs.getURL(columnIndex);
    }

    public URL getURL(String columnLabel) throws SQLException {
        return rs.getURL(columnLabel);
    }

    public RowId getRowId(int columnIndex) throws SQLException {
        return rs.getRowId(columnIndex);
    }

    public RowId getRowId(String columnLabel) throws SQLException {
        return rs.getRowId(columnLabel);
    }

    public NClob getNClob(int columnIndex) throws SQLException {
        return rs.getNClob(columnIndex);
    }

    public NClob getNClob(String columnLabel) throws SQLException {
        return rs.getNClob(columnLabel);
    }

    public SQLXML getSQLXML(int columnIndex) throws SQLException {
        return rs.getSQLXML(columnIndex);
    }

    public SQLXML getSQLXML(String columnLabel) throws SQLException {
        return rs.getSQLXML(columnLabel);
    }

    public String getNString(int columnIndex) throws SQLException {
        return rs.getNString(columnIndex);
    }

    public String getNString(String columnLabel) throws SQLException {
        return rs.getNString(columnLabel);
    }

    public Reader getNCharacterStream(int columnIndex) throws SQLException {
        return rs.getNCharacterStream(columnIndex);
    }

    public Reader getNCharacterStream(String columnLabel) throws SQLException {
        return rs.getNCharacterStream(columnLabel);
    }

    public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
        return rs.getObject(columnIndex, type);
    }

    public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
        return rs.getObject(columnLabel, type);
    }
}
