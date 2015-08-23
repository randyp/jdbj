package com.github.randyp.jdbj;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.Map;

/**
 * Retrieves values from a single row in the result set.
 */
public abstract class SmartResult {

    public <A> A[] getArray(int columnIndex) throws SQLException {
        final Array array = rs().getArray(columnIndex);
        //noinspection unchecked
        return array == null || rs().wasNull() ? null : (A[]) array.getArray();
    }

    public <A> A[] getArray(String columnLabel) throws SQLException {
        final Array array = rs().getArray(columnLabel);
        //noinspection unchecked
        return array == null || rs().wasNull() ? null : (A[]) array.getArray();
    }

    public Array getSQLArray(int columnIndex) throws SQLException {
        return rs().getArray(columnIndex);
    }

    public Array getSQLArray(String columnLabel) throws SQLException {
        return rs().getArray(columnLabel);
    }

    public InputStream getAsciiStream(int columnIndex) throws SQLException {
        return rs().getAsciiStream(columnIndex);
    }

    public InputStream getAsciiStream(String columnLabel) throws SQLException {
        return rs().getAsciiStream(columnLabel);
    }

    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        return rs().getBigDecimal(columnIndex);
    }

    public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
        return rs().getBigDecimal(columnLabel);
    }

    public InputStream getBinaryStream(int columnIndex) throws SQLException {
        return rs().getBinaryStream(columnIndex);
    }

    public InputStream getBinaryStream(String columnLabel) throws SQLException {
        return rs().getBinaryStream(columnLabel);
    }

    public Blob getBlob(int columnIndex) throws SQLException {
        return rs().getBlob(columnIndex);
    }

    public Blob getBlob(String columnLabel) throws SQLException {
        return rs().getBlob(columnLabel);
    }

    public Boolean getBoolean(int columnIndex) throws SQLException {
        final boolean toReturn = rs().getBoolean(columnIndex);
        return rs().wasNull() ? null : toReturn ;
    }

    public Boolean getBoolean(String columnLabel) throws SQLException {
        final boolean toReturn = rs().getBoolean(columnLabel);
        return rs().wasNull() ? null : toReturn ;
    }

    public boolean getBooleanPrimitive(int columnIndex) throws SQLException {
        final boolean toReturn = rs().getBoolean(columnIndex);
        nullCheckForPrimitive(columnIndex);
        return toReturn;
    }

    public boolean getBooleanPrimitive(String columnLabel) throws SQLException {
        final boolean toReturn = rs().getBoolean(columnLabel);
        nullCheckForPrimitive(columnLabel);
        return toReturn;
    }

    public Byte getByte(int columnIndex) throws SQLException {
        final byte toReturn = rs().getByte(columnIndex);
        return rs().wasNull() ? null : toReturn;
    }

    public Byte getByte(String columnLabel) throws SQLException {
        final byte toReturn = rs().getByte(columnLabel);
        return rs().wasNull() ? null : toReturn;
    }

    public byte getBytePrimitive(int columnIndex) throws SQLException {
        final byte toReturn = rs().getByte(columnIndex);
        nullCheckForPrimitive(columnIndex);
        return toReturn;
    }

    public byte getBytePrimitive(String columnLabel) throws SQLException {
        final byte toReturn = rs().getByte(columnLabel);
        nullCheckForPrimitive(columnLabel);
        return toReturn;
    }

    public byte[] getBytes(int columnIndex) throws SQLException {
        return rs().getBytes(columnIndex);
    }

    public byte[] getBytes(String columnLabel) throws SQLException {
        return rs().getBytes(columnLabel);
    }

    public Reader getCharacterStream(int columnIndex) throws SQLException {
        return rs().getCharacterStream(columnIndex);
    }

    public Reader getCharacterStream(String columnLabel) throws SQLException {
        return rs().getCharacterStream(columnLabel);
    }

    public Clob getClob(int columnIndex) throws SQLException {
        return rs().getClob(columnIndex);
    }

    public Clob getClob(String columnLabel) throws SQLException {
        return rs().getClob(columnLabel);
    }

    public Date getDate(int columnIndex) throws SQLException {
        return rs().getDate(columnIndex);
    }

    public Date getDate(String columnLabel) throws SQLException {
        return rs().getDate(columnLabel);
    }

    public Date getDate(int columnIndex, Calendar cal) throws SQLException {
        return rs().getDate(columnIndex, cal);
    }

    public Date getDate(String columnLabel, Calendar cal) throws SQLException {
        return rs().getDate(columnLabel, cal);
    }

    public Double getDouble(int columnIndex) throws SQLException {
        final double toReturn = rs().getDouble(columnIndex);
        return rs().wasNull() ? null : toReturn;
    }

    public Double getDouble(String columnLabel) throws SQLException {
        final double toReturn = rs().getDouble(columnLabel);
        return rs().wasNull() ? null : toReturn;
    }

    public double getDoublePrimitive(int columnIndex) throws SQLException {
        final double toReturn = rs().getDouble(columnIndex);
        nullCheckForPrimitive(columnIndex);
        return toReturn;
    }

    public double getDoublePrimitive(String columnLabel) throws SQLException {
        final double toReturn = rs().getDouble(columnLabel);
        nullCheckForPrimitive(columnLabel);
        return toReturn;
    }

    public <T extends Enum<T>> T getEnum(String columnLabel, Class<T> enumType) throws SQLException {
        String enumName = rs().getString(columnLabel);
        return Enum.valueOf(enumType, enumName);
    }

    public <T extends Enum<T>> T getEnum(int columnIndex, Class<T> enumType) throws SQLException {
        String enumName = rs().getString(columnIndex);
        return Enum.valueOf(enumType, enumName);
    }

    public Float getFloat(int columnIndex) throws SQLException {
        final float toReturn = rs().getFloat(columnIndex);
        return rs().wasNull() ? null : toReturn;
    }

    public Float getFloat(String columnLabel) throws SQLException {
        final float toReturn = rs().getFloat(columnLabel);
        return rs().wasNull() ? null : toReturn;
    }

    public float getFloatPrimitive(int columnIndex) throws SQLException {
        final float toReturn = rs().getFloat(columnIndex);
        nullCheckForPrimitive(columnIndex);
        return toReturn;
    }

    public float getFloatPrimitive(String columnLabel) throws SQLException {
        final float toReturn = rs().getFloat(columnLabel);
        nullCheckForPrimitive(columnLabel);
        return toReturn;
    }

    public Integer getInteger(int columnIndex) throws SQLException {
        final int toReturn = rs().getInt(columnIndex);
        return rs().wasNull() ? null : toReturn;
    }

    public Integer getInteger(String columnLabel) throws SQLException {
        final int toReturn = rs().getInt(columnLabel);
        return rs().wasNull() ? null : toReturn;
    }

    public int getIntegerPrimitive(int columnIndex) throws SQLException {
        return getInt(columnIndex);
    }

    public int getIntegerPrimitive(String columnLabel) throws SQLException {
        return getInt(columnLabel);
    }

    public int getInt(int columnIndex) throws SQLException {
        final int toReturn = rs().getInt(columnIndex);
        nullCheckForPrimitive(columnIndex);
        return toReturn;
    }

    public int getInt(String columnLabel) throws SQLException {
        final int toReturn = rs().getInt(columnLabel);
        nullCheckForPrimitive(columnLabel);
        return toReturn;
    }

    public Long getLong(int columnIndex) throws SQLException {
        final long toReturn = rs().getLong(columnIndex);
        return rs().wasNull() ? null : toReturn;
    }

    public Long getLong(String columnLabel) throws SQLException {
        final long toReturn = rs().getLong(columnLabel);
        return rs().wasNull() ? null : toReturn;
    }

    public long getLongPrimitive(int columnIndex) throws SQLException {
        final long toReturn = rs().getLong(columnIndex);
        nullCheckForPrimitive(columnIndex);
        return toReturn;
    }

    public long getLongPrimitive(String columnLabel) throws SQLException {
        final long toReturn = rs().getLong(columnLabel);
        nullCheckForPrimitive(columnLabel);
        return toReturn;
    }

    public Reader getNCharacterStream(int columnIndex) throws SQLException {
        return rs().getNCharacterStream(columnIndex);
    }

    public Reader getNCharacterStream(String columnLabel) throws SQLException {
        return rs().getNCharacterStream(columnLabel);
    }

    public NClob getNClob(int columnIndex) throws SQLException {
        return rs().getNClob(columnIndex);
    }

    public NClob getNClob(String columnLabel) throws SQLException {
        return rs().getNClob(columnLabel);
    }

    public String getNString(int columnIndex) throws SQLException {
        return rs().getNString(columnIndex);
    }

    public String getNString(String columnLabel) throws SQLException {
        return rs().getNString(columnLabel);
    }

    public Object getObject(int columnIndex) throws SQLException {
        return rs().getObject(columnIndex);
    }

    public Object getObject(String columnLabel) throws SQLException {
        return rs().getObject(columnLabel);
    }

    public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {
        return rs().getObject(columnIndex, map);
    }

    public Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException {
        return rs().getObject(columnLabel, map);
    }

    public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
        return rs().getObject(columnIndex, type);
    }

    public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
        return rs().getObject(columnLabel, type);
    }

    public Short getShort(int columnIndex) throws SQLException {
        final short toReturn = rs().getShort(columnIndex);
        return rs().wasNull() ? null : toReturn;
    }

    public Short getShort(String columnLabel) throws SQLException {
        final short toReturn = rs().getShort(columnLabel);
        return rs().wasNull() ? null : toReturn;
    }

    public short getShortPrimitive(int columnIndex) throws SQLException {
        final short toReturn = rs().getShort(columnIndex);
        nullCheckForPrimitive(columnIndex);
        return toReturn;
    }

    public short getShortPrimitive(String columnLabel) throws SQLException {
        final short toReturn = rs().getShort(columnLabel);
        nullCheckForPrimitive(columnLabel);
        return toReturn;
    }

    public String getString(int columnIndex) throws SQLException {
        return rs().getString(columnIndex);
    }

    public SQLXML getSQLXML(int columnIndex) throws SQLException {
        return rs().getSQLXML(columnIndex);
    }

    public SQLXML getSQLXML(String columnLabel) throws SQLException {
        return rs().getSQLXML(columnLabel);
    }

    public String getString(String columnLabel) throws SQLException {
        return rs().getString(columnLabel);
    }

    public Time getTime(int columnIndex) throws SQLException {
        return rs().getTime(columnIndex);
    }

    public Time getTime(String columnLabel) throws SQLException {
        return rs().getTime(columnLabel);
    }

    public Time getTime(int columnIndex, Calendar cal) throws SQLException {
        return rs().getTime(columnIndex, cal);
    }

    public Time getTime(String columnLabel, Calendar cal) throws SQLException {
        return rs().getTime(columnLabel, cal);
    }

    public Timestamp getTimestamp(int columnIndex) throws SQLException {
        return rs().getTimestamp(columnIndex);
    }

    public Timestamp getTimestamp(String columnLabel) throws SQLException {
        return rs().getTimestamp(columnLabel);
    }

    public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
        return rs().getTimestamp(columnIndex, cal);
    }

    public Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException {
        return rs().getTimestamp(columnLabel, cal);
    }

    public URL getURL(int columnIndex) throws SQLException {
        return rs().getURL(columnIndex);
    }

    public URL getURL(String columnLabel) throws SQLException {
        return rs().getURL(columnLabel);
    }

    public SQLWarning getWarnings() throws SQLException {
        return rs().getWarnings();
    }

    public ResultSetMetaData getMetaData() throws SQLException {
        return rs().getMetaData();
    }
    
    abstract ResultSet rs();

    private void nullCheckForPrimitive(int columnIndex) throws SQLException {
        if (rs().getObject(columnIndex) == null){
            throw new SQLException("tried to get primitive for column " + columnIndex + " but was null");
        }
    }

    private void nullCheckForPrimitive(String columnLabel) throws SQLException {
        if(rs().wasNull()){
            throw new SQLException("tried to get primitive for column \"" + columnLabel + "\" but was null");
        }
    }
}
