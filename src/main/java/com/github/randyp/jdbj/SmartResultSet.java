package com.github.randyp.jdbj;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.Map;

public class SmartResultSet implements AutoCloseable {

    private final ResultSet rs;

    public SmartResultSet(ResultSet rs) {
        this.rs = rs;
    }

    public boolean next() throws SQLException {
        return rs.next();
    }

    public int findColumn(String columnLabel) throws SQLException {
        return rs.findColumn(columnLabel);
    }

    @Override
    public void close() throws SQLException {
        rs.close();
    }


    public Array getArray(int columnIndex) throws SQLException {
        return rs.getArray(columnIndex);
    }

    public Array getArray(String columnLabel) throws SQLException {
        return rs.getArray(columnLabel);
    }

    public InputStream getAsciiStream(int columnIndex) throws SQLException {
        return rs.getAsciiStream(columnIndex);
    }

    public InputStream getAsciiStream(String columnLabel) throws SQLException {
        return rs.getAsciiStream(columnLabel);
    }

    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        return rs.getBigDecimal(columnIndex);
    }

    public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
        return rs.getBigDecimal(columnLabel);
    }

    public InputStream getBinaryStream(int columnIndex) throws SQLException {
        return rs.getBinaryStream(columnIndex);
    }

    public InputStream getBinaryStream(String columnLabel) throws SQLException {
        return rs.getBinaryStream(columnLabel);
    }

    public Blob getBlob(int columnIndex) throws SQLException {
        return rs.getBlob(columnIndex);
    }

    public Blob getBlob(String columnLabel) throws SQLException {
        return rs.getBlob(columnLabel);
    }

    public Boolean getBoolean(int columnIndex) throws SQLException {
        return rs.getObject(columnIndex) == null ? null : rs.getBoolean(columnIndex);
    }

    public Boolean getBoolean(String columnLabel) throws SQLException {
        return isNull(columnLabel) ? null : rs.getBoolean(columnLabel);
    }

    public boolean getBooleanPrimitive(int columnIndex) throws SQLException {
        nullCheckForPrimitive(columnIndex);
        return rs.getBoolean(columnIndex);
    }

    public boolean getBooleanPrimitive(String columnLabel) throws SQLException {
        nullCheckForPrimitive(columnLabel);
        return rs.getBoolean(columnLabel);
    }

    public Byte getByte(int columnIndex) throws SQLException {
        return rs.getObject(columnIndex) == null ? null : rs.getByte(columnIndex);
    }

    public Byte getByte(String columnLabel) throws SQLException {
        return isNull(columnLabel) ? null : rs.getByte(columnLabel);
    }

    public byte getBytePrimitive(int columnIndex) throws SQLException {
        nullCheckForPrimitive(columnIndex);
        return rs.getByte(columnIndex);
    }

    public byte getBytePrimitive(String columnLabel) throws SQLException {
        nullCheckForPrimitive(columnLabel);
        return rs.getByte(columnLabel);
    }

    public byte[] getBytes(int columnIndex) throws SQLException {
        return rs.getBytes(columnIndex);
    }

    public byte[] getBytes(String columnLabel) throws SQLException {
        return rs.getBytes(columnLabel);
    }

    public Reader getCharacterStream(int columnIndex) throws SQLException {
        return rs.getCharacterStream(columnIndex);
    }

    public Reader getCharacterStream(String columnLabel) throws SQLException {
        return rs.getCharacterStream(columnLabel);
    }

    public Clob getClob(int columnIndex) throws SQLException {
        return rs.getClob(columnIndex);
    }

    public Clob getClob(String columnLabel) throws SQLException {
        return rs.getClob(columnLabel);
    }

    public Date getDate(int columnIndex) throws SQLException {
        return rs.getDate(columnIndex);
    }

    public Date getDate(String columnLabel) throws SQLException {
        return rs.getDate(columnLabel);
    }

    public Date getDate(int columnIndex, Calendar cal) throws SQLException {
        return rs.getDate(columnIndex, cal);
    }

    public Date getDate(String columnLabel, Calendar cal) throws SQLException {
        return rs.getDate(columnLabel, cal);
    }

    public Double getDouble(int columnIndex) throws SQLException {
        return rs.getObject(columnIndex) == null ? null : rs.getDouble(columnIndex);
    }

    public Double getDouble(String columnLabel) throws SQLException {
        return isNull(columnLabel) ? null : rs.getDouble(columnLabel);
    }

    public double getDoublePrimitive(int columnIndex) throws SQLException {
        nullCheckForPrimitive(columnIndex);
        return rs.getDouble(columnIndex);
    }

    public double getDoublePrimitive(String columnLabel) throws SQLException {
        nullCheckForPrimitive(columnLabel);
        return rs.getDouble(columnLabel);
    }

    public Float getFloat(int columnIndex) throws SQLException {
        return rs.getObject(columnIndex) == null ? null : rs.getFloat(columnIndex);
    }

    public Float getFloat(String columnLabel) throws SQLException {
        return isNull(columnLabel) ? null : rs.getFloat(columnLabel);
    }

    public float getFloatPrimitive(int columnIndex) throws SQLException {
        nullCheckForPrimitive(columnIndex);
        return rs.getFloat(columnIndex);
    }

    public float getFloatPrimitive(String columnLabel) throws SQLException {
        nullCheckForPrimitive(columnLabel);
        return rs.getFloat(columnLabel);
    }

    public Integer getInteger(int columnIndex) throws SQLException {
        return isNull(columnIndex) ? null : rs.getInt(columnIndex);
    }

    public Integer getInteger(String columnLabel) throws SQLException {
        return isNull(columnLabel) ? null : rs.getInt(columnLabel);
    }

    public int getInt(int columnIndex) throws SQLException {
        nullCheckForPrimitive(columnIndex);
        return rs.getInt(columnIndex);
    }

    public int getInt(String columnLabel) throws SQLException {
        nullCheckForPrimitive(columnLabel);
        return rs.getInt(columnLabel);
    }

    public int getIntegerPrimitive(int columnIndex) throws SQLException {
        return getInt(columnIndex);
    }

    public int getIntegerPrimitive(String columnLabel) throws SQLException {
        return getInt(columnLabel);
    }

    public Long getLong(int columnIndex) throws SQLException {
        return isNull(columnIndex) ? null : rs.getLong(columnIndex);
    }

    public Long getLong(String columnLabel) throws SQLException {
        return isNull(columnLabel) ? null : rs.getLong(columnLabel);
    }

    public long getLongPrimitive(int columnIndex) throws SQLException {
        nullCheckForPrimitive(columnIndex);
        return rs.getLong(columnIndex);
    }

    public long getLongPrimitive(String columnLabel) throws SQLException {
        nullCheckForPrimitive(columnLabel);
        return rs.getLong(columnLabel);
    }

    public Reader getNCharacterStream(int columnIndex) throws SQLException {
        return rs.getNCharacterStream(columnIndex);
    }

    public Reader getNCharacterStream(String columnLabel) throws SQLException {
        return rs.getNCharacterStream(columnLabel);
    }

    public NClob getNClob(int columnIndex) throws SQLException {
        return rs.getNClob(columnIndex);
    }

    public NClob getNClob(String columnLabel) throws SQLException {
        return rs.getNClob(columnLabel);
    }

    public String getNString(int columnIndex) throws SQLException {
        return rs.getNString(columnIndex);
    }

    public String getNString(String columnLabel) throws SQLException {
        return rs.getNString(columnLabel);
    }

    public Object getObject(int columnIndex) throws SQLException {
        return rs.getObject(columnIndex);
    }

    public Object getObject(String columnLabel) throws SQLException {
        return rs.getObject(columnLabel);
    }

    public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {
        return rs.getObject(columnIndex, map);
    }

    public Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException {
        return rs.getObject(columnLabel, map);
    }

    public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
        return rs.getObject(columnIndex, type);
    }

    public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
        return rs.getObject(columnLabel, type);
    }

    public Ref getRef(int columnIndex) throws SQLException {
        return rs.getRef(columnIndex);
    }

    public Ref getRef(String columnLabel) throws SQLException {
        return rs.getRef(columnLabel);
    }

    public RowId getRowId(int columnIndex) throws SQLException {
        return rs.getRowId(columnIndex);
    }

    public RowId getRowId(String columnLabel) throws SQLException {
        return rs.getRowId(columnLabel);
    }

    public Short getShort(int columnIndex) throws SQLException {
        return isNull(columnIndex) ? null : rs.getShort(columnIndex);
    }

    public Short getShort(String columnLabel) throws SQLException {
        return isNull(columnLabel) ? null : rs.getShort(columnLabel);
    }

    public short getShortPrimitive(int columnIndex) throws SQLException {
        nullCheckForPrimitive(columnIndex);
        return rs.getShort(columnIndex);
    }

    public short getShortPrimitive(String columnLabel) throws SQLException {
        nullCheckForPrimitive(columnLabel);
        return rs.getShort(columnLabel);
    }

    public String getString(int columnIndex) throws SQLException {
        return rs.getString(columnIndex);
    }

    public SQLXML getSQLXML(int columnIndex) throws SQLException {
        return rs.getSQLXML(columnIndex);
    }

    public SQLXML getSQLXML(String columnLabel) throws SQLException {
        return rs.getSQLXML(columnLabel);
    }

    public String getString(String columnLabel) throws SQLException {
        return rs.getString(columnLabel);
    }

    public Time getTime(int columnIndex) throws SQLException {
        return rs.getTime(columnIndex);
    }

    public Time getTime(String columnLabel) throws SQLException {
        return rs.getTime(columnLabel);
    }

    public Time getTime(int columnIndex, Calendar cal) throws SQLException {
        return rs.getTime(columnIndex, cal);
    }

    public Time getTime(String columnLabel, Calendar cal) throws SQLException {
        return rs.getTime(columnLabel, cal);
    }

    public Timestamp getTimestamp(int columnIndex) throws SQLException {
        return rs.getTimestamp(columnIndex);
    }

    public Timestamp getTimestamp(String columnLabel) throws SQLException {
        return rs.getTimestamp(columnLabel);
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

    public SQLWarning getWarnings() throws SQLException {
        return rs.getWarnings();
    }

    public ResultSetMetaData getMetaData() throws SQLException {
        return rs.getMetaData();
    }

    public boolean isNull(String columnLabel) throws SQLException {
        return rs.getObject(columnLabel) == null;
    }

    public boolean isNull(int columnIndex) throws SQLException {
        return rs.getObject(columnIndex) == null;
    }

    private void nullCheckForPrimitive(int columnIndex) throws SQLException {
        if(rs.getObject(columnIndex) == null){
            throw new SQLException("tried to get primitive for column " + columnIndex + " but was null");
        }
    }

    private void nullCheckForPrimitive(String columnLabel) throws SQLException {
        if(isNull(columnLabel)){
            throw new SQLException("tried to get primitive for column \"" + columnLabel + "\" but was null");
        }
    }
}
