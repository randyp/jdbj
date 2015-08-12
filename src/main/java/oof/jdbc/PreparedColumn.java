package oof.jdbc;

import oof.jdbc.lambda.Binding;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.List;

public class PreparedColumn {

    private final PreparedStatement ps;
    private final int parameterIndex;

    public PreparedColumn(PreparedStatement ps, int parameterIndex) {
        this.ps = ps;
        this.parameterIndex = parameterIndex;
    }

    public void setNull(int sqlType) throws SQLException {
        ps.setNull(parameterIndex, sqlType);
    }

    public void setBoolean( boolean x) throws SQLException {
        ps.setBoolean(parameterIndex, x);
    }

    public void setByte( byte x) throws SQLException {
        ps.setByte(parameterIndex, x);
    }

    public void setShort( short x) throws SQLException {
        ps.setShort(parameterIndex, x);
    }

    public void setInt( int x) throws SQLException {
        ps.setInt(parameterIndex, x);
    }

    public void setLong( long x) throws SQLException {
        ps.setLong(parameterIndex, x);
    }

    public void setFloat( float x) throws SQLException {
        ps.setFloat(parameterIndex, x);
    }

    public void setDouble( double x) throws SQLException {
        ps.setDouble(parameterIndex, x);
    }

    public void setBigDecimal( BigDecimal x) throws SQLException {
        ps.setBigDecimal(parameterIndex, x);
    }

    public void setString( String x) throws SQLException {
        ps.setString(parameterIndex, x);
    }

    public void setBytes( byte[] x) throws SQLException {
        ps.setBytes(parameterIndex, x);
    }

    public void setDate( Date x) throws SQLException {
        ps.setDate(parameterIndex, x);
    }

    public void setTime( Time x) throws SQLException {
        ps.setTime(parameterIndex, x);
    }

    public void setTimestamp( Timestamp x) throws SQLException {
        ps.setTimestamp(parameterIndex, x);
    }

    public void setAsciiStream( InputStream x, int length) throws SQLException {
        ps.setAsciiStream(parameterIndex, x, length);
    }

    public void setBinaryStream( InputStream x, int length) throws SQLException {
        ps.setBinaryStream(parameterIndex, x, length);
    }

    public void setObject( Object x, int targetSqlType) throws SQLException {
        ps.setObject(parameterIndex, x, targetSqlType);
    }

    public void setObject( Object x) throws SQLException {
        ps.setObject(parameterIndex, x);
    }

    public void setCharacterStream( Reader reader, int length) throws SQLException {
        ps.setCharacterStream(parameterIndex, reader, length);
    }

    public void setRef( Ref x) throws SQLException {
        ps.setRef(parameterIndex, x);
    }

    public void setBlob( Blob x) throws SQLException {
        ps.setBlob(parameterIndex, x);
    }

    public void setClob( Clob x) throws SQLException {
        ps.setClob(parameterIndex, x);
    }

    public void setArray( Array x) throws SQLException {
        ps.setArray(parameterIndex, x);
    }

    public void setDate( Date x, Calendar cal) throws SQLException {
        ps.setDate(parameterIndex, x, cal);
    }

    public void setTime( Time x, Calendar cal) throws SQLException {
        ps.setTime(parameterIndex, x, cal);
    }

    public void setTimestamp( Timestamp x, Calendar cal) throws SQLException {
        ps.setTimestamp(parameterIndex, x, cal);
    }

    public void setNull( int sqlType, String typeName) throws SQLException {
        ps.setNull(parameterIndex, sqlType, typeName);
    }

    public void setURL( URL x) throws SQLException {
        ps.setURL(parameterIndex, x);
    }

    public void setNString( String value) throws SQLException {
        ps.setNString(parameterIndex, value);
    }

    public void setNCharacterStream( Reader value, long length) throws SQLException {
        ps.setNCharacterStream(parameterIndex, value, length);
    }

    public void setNClob( NClob value) throws SQLException {
        ps.setNClob(parameterIndex, value);
    }

    public void setClob( Reader reader, long length) throws SQLException {
        ps.setClob(parameterIndex, reader, length);
    }

    public void setBlob( InputStream inputStream, long length) throws SQLException {
        ps.setBlob(parameterIndex, inputStream, length);
    }

    public void setNClob( Reader reader, long length) throws SQLException {
        ps.setNClob(parameterIndex, reader, length);
    }

    public void setSQLXML( SQLXML xmlObject) throws SQLException {
        ps.setSQLXML(parameterIndex, xmlObject);
    }

    public void setObject( Object x, int targetSqlType, int scaleOrLength) throws SQLException {
        ps.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
    }

    public void setAsciiStream( InputStream x, long length) throws SQLException {
        ps.setAsciiStream(parameterIndex, x, length);
    }

    public void setBinaryStream( InputStream x, long length) throws SQLException {
        ps.setBinaryStream(parameterIndex, x, length);
    }

    public void setCharacterStream( Reader reader, long length) throws SQLException {
        ps.setCharacterStream(parameterIndex, reader, length);
    }

    public void setAsciiStream( InputStream x) throws SQLException {
        ps.setAsciiStream(parameterIndex, x);
    }

    public void setCharacterStream( Reader reader) throws SQLException {
        ps.setCharacterStream(parameterIndex, reader);
    }

    public void setBinaryStream( InputStream x) throws SQLException {
        ps.setBinaryStream(parameterIndex, x);
    }

    public void setNCharacterStream( Reader value) throws SQLException {
        ps.setNCharacterStream(parameterIndex, value);
    }

    public void setClob( Reader reader) throws SQLException {
        ps.setClob(parameterIndex, reader);
    }

    public void setBlob( InputStream inputStream) throws SQLException {
        ps.setBlob(parameterIndex, inputStream);
    }

    public void setNClob( Reader reader) throws SQLException {
        ps.setNClob(parameterIndex, reader);
    }

    public void setObject( Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {
        ps.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
    }

    public void setObject( Object x, SQLType targetSqlType) throws SQLException {
        ps.setObject(parameterIndex, x, targetSqlType);
    }

    public Array createArray(String typeName, Object[] elements) throws SQLException {
        return ps.getConnection().createArrayOf(typeName, elements);
    }

    public Blob createBlob() throws SQLException {
        return ps.getConnection().createBlob();
    }

    public Clob createClob() throws SQLException {
        return ps.getConnection().createClob();
    }

    public NClob createNClob() throws SQLException {
        return ps.getConnection().createNClob();
    }

    public SQLXML createSQLXML() throws SQLException {
        return ps.getConnection().createSQLXML();
    }

    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        return ps.getConnection().createStruct(typeName, attributes);
    }

    public void setList(List<Binding> bindings) throws SQLException {
        for (int i = 0; i < bindings.size(); i++) {
            Binding binding = bindings.get(i);
            final PreparedColumn pc = i == 0 ? this : new PreparedColumn(ps, parameterIndex + i);
            binding.bind(pc);
        }
    }
}
