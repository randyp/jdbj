package jdbj;

import jdbj.lambda.Binding;

import javax.annotation.Nullable;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;

public interface ValueBindingsBuilder<E extends PositionalBindingsBuilder> {

    E bind(String name, Binding b);

    default E bindArray(String name, @Nullable Array x) throws SQLException {
        return bind(name, pc->pc.setArray(x));
    }

    default E bindAsciiStream(String name, @Nullable InputStream x) throws SQLException {
        return bind(name, pc->pc.setAsciiStream(x));
    }

    default E bindAsciiStream(String name, @Nullable InputStream x, int length) throws SQLException {
        return bind(name, pc->pc.setAsciiStream(x, length));
    }

    default E bindAsciiStream(String name, @Nullable InputStream x, long length) throws SQLException {
        return bind(name, pc->pc.setAsciiStream(x, length));
    }

    default E bindBigDecimal(String name, @Nullable BigDecimal x) throws SQLException {
        return bind(name, pc->pc.setBigDecimal(x));
    }

    default E bindBinaryStream(String name, @Nullable InputStream x) throws SQLException {
        return bind(name, pc->pc.setBinaryStream(x));
    }

    default E bindBinaryStream(String name, @Nullable InputStream x, long length) throws SQLException {
        return bind(name, pc->pc.setBinaryStream(x, length));
    }

    default E bindBinaryStream(String name, @Nullable InputStream x, int length) throws SQLException {
        return bind(name, pc->pc.setBinaryStream(x, length));
    }

    default E bindBlob(String name, @Nullable Blob x) throws SQLException {
        return bind(name, pc->pc.setBlob(x));
    }

    default E bindBlob(String name, @Nullable InputStream inputStream) throws SQLException {
        return bind(name, pc->pc.setBlob(inputStream));
    }

    default E bindBlob(String name, @Nullable InputStream inputStream, long length) throws SQLException {
        return bind(name, pc->pc.setBlob(inputStream, length));
    }

    default E bindBoolean(String name, boolean x) throws SQLException {
        return bind(name, pc-> pc.setBoolean(x));
    }

    default E bindByte(String name, byte x) throws SQLException {
        return bind(name, pc->pc.setByte(x));
    }

    default E bindBytes(String name, byte[] x) throws SQLException {
        return bind(name, pc->pc.setBytes(x));
    }

    default E bindCharacterStream(String name, @Nullable Reader reader) throws SQLException {
        return bind(name, pc->pc.setCharacterStream(reader));
    }

    default E bindCharacterStream(String name, @Nullable Reader reader, int length) throws SQLException {
        return bind(name, pc->pc.setCharacterStream(reader, length));
    }

    default E bindCharacterStream(String name, @Nullable Reader reader, long length) throws SQLException {
        return bind(name, pc->pc.setCharacterStream(reader, length));
    }

    default E bindClob(String name, @Nullable Clob x) throws SQLException {
        return bind(name, pc->pc.setClob(x));
    }

    default E bindClob(String name, @Nullable Reader reader) throws SQLException {
        return bind(name, pc->pc.setClob(reader));
    }

    default E bindClob(String name, @Nullable Reader reader, long length) throws SQLException {
        return bind(name, pc->pc.setClob(reader, length));
    }

    default E bindDate(String name, @Nullable Date x) throws SQLException {
        return bind(name, pc->pc.setDate(x));
    }

    default E bindDate(String name, @Nullable Date x, Calendar cal) throws SQLException {
        return bind(name, pc->pc.setDate(x, cal));
    }

    default E bindDouble(String name, double x) throws SQLException {
        return bind(name, pc->pc.setDouble(x));
    }

    default E bindFloat(String name, float x) throws SQLException {
        return bind(name, pc->pc.setFloat(x));
    }

    default E bindInt(String name, int x) throws SQLException {
        return bind(name, pc->pc.setInt(x));
    }

    default E bindLong(String name, long x) throws SQLException {
        return bind(name, pc->pc.setLong(x));
    }

    default E bindNCharacterStream(String name, @Nullable Reader value) throws SQLException {
        return bind(name, pc->pc.setNCharacterStream(value));
    }

    default E bindNCharacterStream(String name, @Nullable Reader value, long length) throws SQLException {
        return bind(name, pc->pc.setNCharacterStream(value, length));
    }

    default E bindNClob(String name, @Nullable NClob value) throws SQLException {
        return bind(name, pc->pc.setNClob(value));
    }

    default E bindNClob(String name, @Nullable Reader reader) throws SQLException {
        return bind(name, pc->pc.setNClob(reader));
    }

    default E bindNClob(String name, @Nullable Reader reader, long length) throws SQLException {
        return bind(name, pc->pc.setNClob(reader, length));
    }

    default E bindNString(String name, @Nullable String value) throws SQLException {
        return bind(name, pc->pc.setNString(value));
    }

    default E bindNull(String name, int sqlType) throws SQLException {
        return bind(name, pc -> pc.setNull(sqlType));
    }

    default E bindNull(String name, int sqlType, String typeName) throws SQLException {
        return bind(name, pc->pc.setNull(sqlType, typeName));
    }

    default E bindObject(String name, @Nullable Object x) throws SQLException {
        return bind(name, pc->pc.setObject(x));
    }

    default E bindObject(String name, @Nullable Object x, int targetSqlType) throws SQLException {
        return bind(name, pc->pc.setObject(x, targetSqlType));
    }

    default E bindObject(String name, @Nullable Object x, SQLType targetSqlType) throws SQLException {
        return bind(name, pc -> pc.setObject(x, targetSqlType));
    }

    default E bindObject(String name, @Nullable Object x, int targetSqlType, int scaleOrLength) throws SQLException {
        return bind(name, pc->pc.setObject(x, targetSqlType, scaleOrLength));
    }

    default E bindObject(String name, @Nullable Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {
        return bind(name, pc->pc.setObject(x, targetSqlType, scaleOrLength));
    }

    default E bindRef(String name, @Nullable Ref x) throws SQLException {
        return bind(name, pc->pc.setRef(x));
    }

    default E bindShort(String name, short x) throws SQLException {
        return bind(name, pc->pc.setShort(x));
    }

    default E bindSQLXML(String name, @Nullable SQLXML xmlObject) throws SQLException {
        return bind(name, pc->pc.setSQLXML(xmlObject));
    }

    default E bindString(String name, @Nullable String x) throws SQLException {
        return bind(name, pc->pc.setString(x));
    }

    default E bindTime(String name, @Nullable Time x) throws SQLException {
        return bind(name, pc->pc.setTime(x));
    }

    default E bindTimestamp(String name, @Nullable Timestamp x) throws SQLException {
        return bind(name, pc->pc.setTimestamp(x));
    }

    default E bindTime(String name, @Nullable Time x, Calendar cal) throws SQLException {
        return bind(name, pc->pc.setTime(x, cal));
    }

    default E bindTimestamp(String name, @Nullable Timestamp x, Calendar cal) throws SQLException {
        return bind(name, pc->pc.setTimestamp(x, cal));
    }

    default E bindURL(String name, @Nullable URL x) throws SQLException {
        return bind(name, pc->pc.setURL(x));
    }
}
