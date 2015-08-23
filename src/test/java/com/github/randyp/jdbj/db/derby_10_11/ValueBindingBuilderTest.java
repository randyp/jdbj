package com.github.randyp.jdbj.db.derby_10_11;

import com.github.randyp.jdbj.ExecuteUpdate;
import com.github.randyp.jdbj.JDBJ;
import com.github.randyp.jdbj.NamedParameterStatement;
import com.github.randyp.jdbj.SimpleBuilder;
import com.github.randyp.jdbj.test.binding.value.*;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import javax.sql.DataSource;
import java.io.Reader;
import java.net.URL;
import java.sql.*;
import java.util.GregorianCalendar;

@RunWith(Enclosed.class)
public class ValueBindingBuilderTest {

    @ClassRule
    public static final DerbyRule db = new DerbyRule() {
        @Override
        protected void before() throws Throwable {
            super.before();
            cleanup();
            try (Connection connection = getConnection()) {
                try (PreparedStatement ps = connection.prepareStatement("CREATE TABLE binding(bound VARCHAR(500))")) {
                    ps.execute();
                }

                try (PreparedStatement ps = connection.prepareStatement("CREATE TABLE binding_blob(bound BLOB)")) {
                    ps.execute();
                }
            }
        }

        @Override
        protected void after() {
            cleanup();
            super.after();
        }

        private void cleanup() {
            try {
                try (Connection connection = getConnection()) {
                    try (PreparedStatement ps = connection.prepareStatement("DROP TABLE binding_blob")) {
                        ps.execute();
                    } catch (SQLException e) {
                        //ignore
                    }
                    try (PreparedStatement ps = connection.prepareStatement("DROP TABLE binding")) {
                        ps.execute();
                    } catch (SQLException e) {
                        //ignore
                    }
                }
            } catch (SQLException e) {
                //ignore
            }

        }
    };

    public static final NamedParameterStatement statement = NamedParameterStatement.make("SELECT bound FROM binding WHERE :binding <> 'A' OR :binding IS NULL");

    public static ExecuteUpdate update = JDBJ.update("INSERT INTO binding(bound) VALUES(:binding)");

    public static void clearBindings() throws Exception {
        try (Connection connection = db.getConnection();
             PreparedStatement ps = connection.prepareStatement("DELETE FROM binding")) {
            ps.execute();
        }
    }

    public static class BindArray extends BindArrayTest {

        @After
        public void tearDown() throws Exception {
            clearBindings();
        }

        @Override
        public DataSource db() {
            return db;
        }

        @Test(expected = SQLFeatureNotSupportedException.class)
        @Override
        public void value() throws Exception {
            try(Connection connection = db.getConnection()){
                final Array array = connection.createArrayOf("varchar", expected);
                update.bindArray(":binding", array).execute(connection);
            }
            super.value();
        }

        @Test(expected = SQLFeatureNotSupportedException.class)
        @Override
        public void Null() throws Exception {
            update.bindArray(":binding", null).execute(db);
            super.Null();
        }

        @Override
        public SimpleBuilder builder() {
            return new SimpleBuilder(statement);
        }
    }

    public static class BindAsciiStream extends BindAsciiStreamTest {

        @After
        public void tearDown() throws Exception {
            clearBindings();
        }

        @Override
        public DataSource db() {
            return db;
        }


        @Test(expected = SQLException.class) //appears to be bug in driver
        @Override
        public void stream() throws Exception {
            update.bindAsciiStream(":binding", expectedStream()).execute(db);
            super.stream();
        }

        @Test
        @Override
        public void streamNull() throws Exception {
            update.bindAsciiStream(":binding", null).execute(db);
            super.streamNull();
        }

        @Test
        @Override
        public void streamLength() throws Exception {
            update.bindAsciiStream(":binding", expectedStream(), expected.length()).execute(db);
            super.streamLength();
        }

        @Test
        @Override
        public void streamNullLength() throws Exception {
            update.bindAsciiStream(":binding", null, expected.length()).execute(db);
            super.streamNullLength();
        }

        @Test
        @Override
        public void streamLengthLong() throws Exception {
            update.bindAsciiStream(":binding", expectedStream(), (long) expected.length()).execute(db);
            super.streamLengthLong();
        }

        @Test
        @Override
        public void streamNullLengthLong() throws Exception {
            update.bindAsciiStream(":binding", null, (long) expected.length()).execute(db);
            super.streamNullLengthLong();
        }

        @Override
        public SimpleBuilder builder() {
            return new SimpleBuilder(statement);
        }
    }

    public static class BindBigDecimal extends BindBigDecimalTest {

        @After
        public void tearDown() throws Exception {
            clearBindings();
        }

        @Override
        public DataSource db() {
            return db;
        }

        @Test
        @Override
        public void value() throws Exception {
            update.bindBigDecimal(":binding", expected).execute(db);
            super.value();
        }

        @Test
        @Override
        public void Null() throws Exception {
            update.bindBigDecimal(":binding", null).execute(db);
            super.Null();
        }

        @Override
        public SimpleBuilder builder() {
            return new SimpleBuilder(statement);
        }
    }

    /*
    public static class BindBinaryStream extends BindBinaryStreamTest {

        @After
        public void tearDown() throws Exception {
            clearBindings();
        }

        @Override
        public DataSource db() {
            return db;
        }

        @Test
        @Override
        public void inputStream() throws Exception {
            updateBinary.bindBinaryStream(":binding", expectedStream()).execute(db);
            super.inputStream();
        }

        @Test
        @Override
        public void inputStreamNull() throws Exception {
            updateBinary.bindBinaryStream(":binding", null).execute(db);
            super.inputStreamNull();
        }

        @Test
        @Override
        public void inputLengthStream() throws Exception {
            updateBinary.bindBinaryStream(":binding", expectedStream(), expected.length).execute(db);
            super.inputLengthStream();
        }

        @Test
        @Override
        public void inputLengthStreamNull() throws Exception {
            updateBinary.bindBinaryStream(":binding", null, expected.length).execute(db);
            super.inputLengthStreamNull();
        }

        @Test
        @Override
        public void inputLengthLongStream() throws Exception {
            updateBinary.bindBinaryStream(":binding", expectedStream(), (long) expected.length).execute(db);
            super.inputLengthLongStream();
        }

        @Test
        @Override
        public void inputLengthLongStreamNull() throws Exception {
            updateBinary.bindBinaryStream(":binding", null, (long) expected.length).execute(db);
            super.inputLengthLongStreamNull();
        }

        @Override
        public SimpleBuilder builder() {
            return new SimpleBuilder(statement_binary);
        }
    }
    */

    /*
    public static class BindBlob extends BindBlobTest {

        @After
        public void tearDown() throws Exception {
            clearBindings();
        }

        @Rule
        public ExpectedException thrown = ExpectedException.none();

        @Override
        public DataSource db() {
            return db;
        }

        @Test
        @Override
        public void value() throws Exception {
            try(Connection connection = db.getConnection()){
                final Blob blob = connection.createBlob();
                blob.setBytes(1, expected);
                update_blob.bindBlob(":binding", blob).execute(connection);
            }
            super.value();
        }

        @Test
        @Override
        public void valueNull() throws Exception {
            update_blob.bindBlob(":binding", (Blob) null).execute(db);
            super.valueNull();
        }

        @Test
        @Override
        public void inputStream() throws Exception {
            update_blob.bindBlob(":binding", expectedStream()).execute(db);
            super.inputStream();
        }

        @Test
        @Override
        public void inputStreamNull() throws Exception {
            update_blob.bindBlob(":binding", (InputStream) null).execute(db);
            super.inputStreamNull();
        }

        @Test
        @Override
        public void inputStreamLength() throws Exception {
            update_blob.bindBlob(":binding", expectedStream(), expected.length).execute(db);
            super.inputStreamLength();
        }

        @Test
        @Override
        public void inputStreamNullLength() throws Exception {
            update_blob.bindBlob(":binding", null, expected.length).execute(db);
            super.inputStreamNullLength();
        }

        @Override
        public SimpleBuilder builder() {
            return new SimpleBuilder(statement_blob);
        }
    }
    */

    public static class BindBoolean extends BindBooleanTest {

        @After
        public void tearDown() throws Exception {
            clearBindings();
        }

        @Override
        public DataSource db() {
            return db;
        }

        @Test
        @Override
        public void False() throws Exception {
            update.bindBoolean(":binding", Boolean.FALSE).execute(db);
            super.False();
        }

        @Test
        @Override
        public void True() throws Exception {
            update.bindBoolean(":binding", Boolean.TRUE).execute(db);
            super.True();
        }

        @Test
        @Override
        public void Null() throws Exception {
            update.bindBoolean(":binding", null).execute(db);
            super.Null();
        }

        @Test
        @Override
        public void primitiveFalse() throws Exception {
            update.bindBooleanPrimitive(":binding", false).execute(db);
            super.primitiveFalse();
        }

        @Test
        @Override
        public void primitiveTrue() throws Exception {
            update.bindBoolean(":binding", true).execute(db);
            super.primitiveTrue();
        }

        @Override
        public SimpleBuilder builder() {
            return new SimpleBuilder(statement);
        }
    }

    public static class BindByte extends BindByteTest {

        @After
        public void tearDown() throws Exception {
            clearBindings();
        }

        @Override
        public DataSource db() {
            return db;
        }

        @Test
        @Override
        public void value() throws Exception {
            update.bindByte(":binding", expected).execute(db);
            super.value();
        }

        @Test
        @Override
        public void Null() throws Exception {
            update.bindByte(":binding", null).execute(db);
            super.Null();
        }

        @Test
        @Override
        public void primitive() throws Exception {
            update.bindBytePrimitive(":binding", expected).execute(db);
            super.primitive();
        }

        @Override
        public SimpleBuilder builder() {
            return new SimpleBuilder(statement);
        }
    }

//    public static class BindByteArray extends BindByteArrayTest {
//        @Override
//        public DataSource db() {
//            return db;
//        }
//    }
//
//    public static class BindCharacterStream extends BindCharacterStreamTest {
//        @Override
//        public DataSource db() {
//            return db;
//        }
//    }
//
//    public static class BindClob extends BindClobTest {
//        @Override
//        public DataSource db() {
//            return db;
//        }
//    }

    public static class BindDate extends BindDateTest {

        @After
        public void tearDown() throws Exception {
            clearBindings();
        }

        @Override
        public DataSource db() {
            return db;
        }

        @Test
        @Override
        public void value() throws Exception {
            update.bindDate(":binding", expectedDate).execute(db);
            super.value();
        }

        @Test
        @Override
        public void valueNull() throws Exception {
            update.bindDate(":binding", null).execute(db);
            super.valueNull();
        }

        @Test
        @Override
        public void valueCalendar() throws Exception {
            update.bindDate(":binding", expectedDate, GregorianCalendar.getInstance()).execute(db);
            super.valueCalendar();
        }

        @Test
        @Override
        public void valueCalendarNull() throws Exception {
            update.bindDate(":binding", null, GregorianCalendar.getInstance()).execute(db);
            super.valueCalendarNull();
        }

        @Override
        public SimpleBuilder builder() {
            return new SimpleBuilder(statement);
        }
    }

    public static class BindDouble extends BindDoubleTest {

        @After
        public void tearDown() throws Exception {
            clearBindings();
        }

        @Override
        public DataSource db() {
            return db;
        }

        @Test
        @Override
        public void value() throws Exception {
            update.bindDouble(":binding", expected).execute(db);
            super.value();
        }

        @Test
        @Override
        public void Null() throws Exception {
            update.bindDouble(":binding", null).execute(db);
            super.Null();
        }

        @Test
        @Override
        public void primitive() throws Exception {
            update.bindDoublePrimitive(":binding", expected).execute(db);
            super.primitive();
        }

        @Override
        public SimpleBuilder builder() {
            return new SimpleBuilder(statement);
        }
    }

    public static class BindFloat extends BindFloatTest {

        @After
        public void tearDown() throws Exception {
            clearBindings();
        }

        @Override
        public DataSource db() {
            return db;
        }

        @Test
        @Override
        public void value() throws Exception {
            update.bindFloat(":binding", expected).execute(db);
            super.value();
        }

        @Test
        @Override
        public void Null() throws Exception {
            update.bindFloat(":binding", null).execute(db);
            super.Null();
        }

        @Test
        @Override
        public void primitive() throws Exception {
            update.bindFloatPrimitive(":binding", expected).execute(db);
            super.primitive();
        }

        @Override
        public SimpleBuilder builder() {
            return new SimpleBuilder(statement);
        }
    }

    public static class BindInteger extends BindIntegerTest {

        @After
        public void tearDown() throws Exception {
            clearBindings();
        }

        @Override
        public DataSource db() {
            return db;
        }

        @Test
        @Override
        public void value() throws Exception {
            update.bindInteger(":binding", expected).execute(db);
            super.value();
        }

        @Test
        @Override
        public void Null() throws Exception {
            update.bindInteger(":binding", null).execute(db);
            super.Null();
        }

        @Test
        @Override
        public void primitive() throws Exception {
            update.bindIntegerPrimitive(":binding", expected).execute(db);
            super.primitive();
        }

        @Test
        @Override
        public void primitiveAlias() throws Exception {
            update.bindInt(":binding", expected).execute(db);
            super.primitiveAlias();
        }

        @Override
        public SimpleBuilder builder() {
            return new SimpleBuilder(statement);
        }
    }

    public static class BindLong extends BindLongTest {

        @After
        public void tearDown() throws Exception {
            clearBindings();
        }

        @Override
        public DataSource db() {
            return db;
        }

        @Test
        @Override
        public void value() throws Exception {
            update.bindLong(":binding", expected).execute(db);
            super.value();
        }

        @Test
        @Override
        public void Null() throws Exception {
            update.bindLong(":binding", null).execute(db);
            super.Null();
        }

        @Test
        @Override
        public void primitive() throws Exception {
            update.bindLongPrimitive(":binding", expected).execute(db);
            super.primitive();
        }

        @Override
        public SimpleBuilder builder() {
            return new SimpleBuilder(statement);
        }
    }

    public static class BindNCharacterStream extends BindNCharacterStreamTest {

        @After
        public void tearDown() throws Exception {
            clearBindings();
        }

        @Override
        public DataSource db() {
            return db;
        }

        @Test(expected = SQLFeatureNotSupportedException.class)
        @Override
        public void reader() throws Exception {
            update.bindNCharacterStream(":binding", expectedReader()).execute(db);
            super.reader();
        }

        @Test(expected = SQLFeatureNotSupportedException.class)
        @Override
        public void readerNull() throws Exception {
            update.bindNCharacterStream(":binding", null).execute(db);
            super.readerNull();
        }

        @Test(expected = SQLFeatureNotSupportedException.class)
        @Override
        public void readerLength() throws Exception {
            update.bindNCharacterStream(":binding", expectedReader(), expected.length()).execute(db);
            super.readerLength();
        }

        @Test(expected = SQLFeatureNotSupportedException.class)
        @Override
        public void readerNullLength() throws Exception {
            update.bindNCharacterStream(":binding", null, expected.length()).execute(db);
            super.readerNullLength();
        }

        @Override
        public SimpleBuilder builder() {
            return new SimpleBuilder(statement);
        }
    }


    public static class BindNClob extends BindNClobTest {

        @After
        public void tearDown() throws Exception {
            clearBindings();
        }

        @Override
        public DataSource db() {
            return db;
        }

        @Test(expected = SQLFeatureNotSupportedException.class)
        @Override
        public void value() throws Exception {
            try (Connection connection = db.getConnection()) {
                final NClob nClob = connection.createNClob();
                nClob.setString(1, expected);
                update.bindNClob(":binding", nClob).execute(connection);
            }
            super.value();
        }

        @Test(expected = SQLFeatureNotSupportedException.class)
        @Override
        public void valueNull() throws Exception {
            update.bindNClob(":binding", (NClob) null).execute(db);
            super.valueNull();
        }

        @Test(expected = SQLFeatureNotSupportedException.class)
        @Override
        public void reader() throws Exception {
            update.bindNClob(":binding", expectedReader()).execute(db);
            super.reader();
        }

        @Test(expected = SQLFeatureNotSupportedException.class)
        @Override
        public void readerNull() throws Exception {
            update.bindNClob(":binding", (Reader) null).execute(db);
            super.readerNull();
        }

        @Test(expected = SQLFeatureNotSupportedException.class)
        @Override
        public void readerLength() throws Exception {
            update.bindNClob(":binding", expectedReader(), expected.length()).execute(db);
            super.readerLength();
        }

        @Test(expected = SQLFeatureNotSupportedException.class)
        @Override
        public void readerNullLength() throws Exception {
            update.bindNClob(":binding", null, expected.length()).execute(db);
            super.readerNullLength();
        }

        @Override
        public SimpleBuilder builder() {
            return new SimpleBuilder(statement);
        }
    }

    public static class BindNString extends BindNStringTest {

        @After
        public void tearDown() throws Exception {
            clearBindings();
        }

        @Override
        public DataSource db() {
            return db;
        }

        @Test(expected = SQLFeatureNotSupportedException.class)
        @Override
        public void value() throws Exception {
            update.bindNString(":binding", expected).execute(db);
            super.value();
        }

        @Test(expected = SQLFeatureNotSupportedException.class)
        @Override
        public void Null() throws Exception {
            update.bindNString(":binding", null).execute(db);
            super.Null();
        }

        @Override
        public SimpleBuilder builder() {
            return new SimpleBuilder(statement);
        }
    }

    public static class BindNull extends BindNullTest {

        @After
        public void tearDown() throws Exception {
            clearBindings();
        }

        @Override
        public DataSource db() {
            return db;
        }

        @Test
        @Override
        public void type() throws Exception {
            update.bindNull(":binding", Types.VARCHAR).execute(db);
            super.type();
        }

        @Test
        @Override
        public void typeAndName() throws Exception {
            update.bindNull(":binding", Types.VARCHAR, "varchar").execute(db);
            super.typeAndName();
        }

        @Override
        public SimpleBuilder builder() {
            return new SimpleBuilder(statement);
        }
    }

    public static class BindObject extends BindObjectTest {

        @After
        public void tearDown() throws Exception {
            clearBindings();
        }

        @Override
        public DataSource db() {
            return db;
        }

        @Test
        @Override
        public void value() throws Exception {
            update.bindObject(":binding", expected).execute(db);
            super.value();
        }

        @Test
        @Override
        public void valueNull() throws Exception {
            update.bindObject(":binding", null).execute(db);
            super.valueNull();
        }

        @Test
        @Override
        public void valueType() throws Exception {
            update.bindObject(":binding", expected, Types.VARCHAR).execute(db);
            super.valueType();
        }

        @Test
        @Override
        public void valueNullType() throws Exception {
            update.bindObject(":binding", null, Types.VARCHAR).execute(db);
            super.valueNullType();
        }

        @Test
        @Override
        public void valueTypeLength() throws Exception {
            update.bindObject(":binding", expected, Types.VARCHAR, 5).execute(db);
            super.valueTypeLength();
        }

        @Test
        @Override
        public void valueNullTypeLength() throws Exception {
            update.bindObject(":binding", null, Types.VARCHAR, 5).execute(db);
            super.valueNullTypeLength();
        }

        @Test
        @Override
        public void valueSQLType() throws Exception {
            update.bindObject(":binding", expected, JDBCType.VARCHAR).execute(db);
            super.valueSQLType();
        }

        @Test
        @Override
        public void valueNullSQLType() throws Exception {
            update.bindObject(":binding", null, JDBCType.VARCHAR).execute(db);
            super.valueNullSQLType();
        }

        @Test
        @Override
        public void valueSQLTypeLength() throws Exception {
            update.bindObject(":binding", expected, JDBCType.VARCHAR, 5).execute(db);
            super.valueSQLTypeLength();
        }

        @Test
        @Override
        public void valueNullSQLTypeLength() throws Exception {
            update.bindObject(":binding", null, JDBCType.VARCHAR, 5).execute(db);
            super.valueNullSQLTypeLength();
        }

        @Override
        public SimpleBuilder builder() {
            return new SimpleBuilder(statement);
        }
    }

    public static class BindShort extends BindShortTest {

        @After
        public void tearDown() throws Exception {
            clearBindings();
        }

        @Override
        public DataSource db() {
            return db;
        }

        @Test
        @Override
        public void value() throws Exception {
            update.bindShort(":binding", expected).execute(db);
            super.value();
        }

        @Test
        @Override
        public void Null() throws Exception {
            update.bindShort(":binding", null).execute(db);
            super.Null();
        }

        @Test
        @Override
        public void primitive() throws Exception {
            update.bindShortPrimitive(":binding", expected).execute(db);
            super.primitive();
        }

        @Override
        public SimpleBuilder builder() {
            return new SimpleBuilder(statement);
        }
    }

    public static class BindSQLXML extends BindSQLXMLTest {

        @Override
        public DataSource db() {
            return db;
        }

        @Override
        @Test(expected = SQLFeatureNotSupportedException.class)
        public void value() throws Exception {
            try (Connection connection = db().getConnection()) {
                final SQLXML sqlxml = connection.createSQLXML();
                sqlxml.setString(expected);
                update.bindSQLXML(":binding", sqlxml).execute(db);
            }
            super.value();
        }

        @Override
        @Test(expected = SQLFeatureNotSupportedException.class)
        public void Null() throws Exception {
            update.bindSQLXML(":binding", null).execute(db);
            super.Null();
        }

        @Override
        public SimpleBuilder builder() {
            return new SimpleBuilder(statement);
        }
    }

    public static class BindString extends BindStringTest {

        @After
        public void tearDown() throws Exception {
            clearBindings();
        }

        @Override
        public DataSource db() {
            return db;
        }

        @Test
        @Override
        public void value() throws Exception {
            update.bindString(":binding", expected).execute(db);
            super.value();
        }

        @Test
        @Override
        public void valueNull() throws Exception {
            update.bindString(":binding", null).execute(db);
            super.valueNull();
        }

        @Override
        public SimpleBuilder builder() {
            return new SimpleBuilder(statement);
        }
    }

    public static class BindTime extends BindTimeTest {

        @After
        public void tearDown() throws Exception {
            clearBindings();
        }

        @Override
        public DataSource db() {
            return db;
        }

        @Test
        @Override
        public void value() throws Exception {
            update.bindTime(":binding", expectedTime).execute(db);
            super.value();
        }

        @Test
        @Override
        public void valueNull() throws Exception {
            update.bindTime(":binding", null).execute(db);
            super.valueNull();
        }

        @Test
        @Override
        public void valueCalendar() throws Exception {
            update.bindTime(":binding", expectedTime, GregorianCalendar.getInstance()).execute(db);
            super.valueCalendar();
        }

        @Test
        @Override
        public void valueCalendarNull() throws Exception {
            update.bindTime(":binding", null, GregorianCalendar.getInstance()).execute(db);
            super.valueCalendarNull();
        }

        @Override
        public SimpleBuilder builder() {
            return new SimpleBuilder(statement);
        }
    }

    public static class BindTimestamp extends BindTimestampTest {

        @Override
        public DataSource db() {
            return db;
        }

        @After
        public void tearDown() throws Exception {
            clearBindings();
        }

        @Test
        @Override
        public void value() throws Exception {
            update.bindTimestamp(":binding", expectedTimestamp).execute(db);
            super.value();
        }

        @Test
        @Override
        public void valueNull() throws Exception {
            update.bindTimestamp(":binding", null).execute(db);
            super.valueNull();
        }

        @Test
        @Override
        public void valueCalendar() throws Exception {
            update.bindTimestamp(":binding", new Timestamp(expectedTime), GregorianCalendar.getInstance()).execute(db);
            super.valueCalendar();
        }

        @Test
        @Override
        public void valueCalendarNull() throws Exception {
            update.bindTimestamp(":binding", null, GregorianCalendar.getInstance()).execute(db);
            super.valueCalendarNull();
        }

        @Override
        public SimpleBuilder builder() {
            return new SimpleBuilder(statement);
        }
    }

    public static class BindURL extends BindURLTest {

        @Rule
        public ExpectedException thrown = ExpectedException.none();

        @Override
        public DataSource db() {
            return db;
        }

        @After
        public void tearDown() throws Exception {
            clearBindings();
        }

        @Override
        @Test(expected = SQLFeatureNotSupportedException.class)
        public void value() throws Exception {
            update.bindURL(":binding", new URL("http", "google.com", 8080, "/")).execute(db());
            super.value();
        }

        @Override
        @Test(expected = SQLFeatureNotSupportedException.class)
        public void Null() throws Exception {
            update.bindURL(":binding", null).execute(db());
            super.Null();
        }

        @Override
        public SimpleBuilder builder() {
            return new SimpleBuilder(statement);
        }
    }
}
