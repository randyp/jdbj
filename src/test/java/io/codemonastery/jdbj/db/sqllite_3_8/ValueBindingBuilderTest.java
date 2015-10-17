package io.codemonastery.jdbj.db.sqllite_3_8;

import io.codemonastery.jdbj.test.binding.value.*;
import io.codemonastery.jdbj.SimpleBuilder;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

@RunWith(Enclosed.class)
public class ValueBindingBuilderTest {

    @ClassRule
    public static final SqlLiteRule db = new SqlLiteRule();

    public static class BindArray extends BindArrayTest {

        @Rule
        public ExpectedException thrown = ExpectedException.none();

        @Override
        public DataSource db() {
            return db;
        }

        @Test
        @Override
        public void value() throws Exception {
            thrown.expect(SQLException.class);
            thrown.expectMessage("not implemented by SQLite JDBC driver");
            super.value();
        }

        @Test
        @Override
        public void Null() throws Exception {
            thrown.expect(SQLException.class);
            thrown.expectMessage("not implemented by SQLite JDBC driver");
            super.Null();
        }
    }

    public static class BindAsciiStream extends BindAsciiStreamTest {

        @Rule
        public ExpectedException thrown = ExpectedException.none();

        @Override
        public DataSource db() {
            return db;
        }

        @Test
        @Override
        public void stream() throws Exception {
            thrown.expect(SQLException.class);
            thrown.expectMessage("Values not bound to statement");
            super.stream();
        }

        @Test
        @Override
        public void streamNull() throws Exception {
            thrown.expect(SQLException.class);
            thrown.expectMessage("Values not bound to statement");
            super.streamNull();
        }

        @Test
        @Override
        public void streamNullLength() throws Exception {
            thrown.expect(NullPointerException.class);
            super.streamNullLength();
        }

        @Test
        @Override
        public void streamLengthLong() throws Exception {
            thrown.expect(SQLException.class);
            thrown.expectMessage("Values not bound to statement");
            super.streamLengthLong();
        }

        @Test
        @Override
        public void streamNullLengthLong() throws Exception {
            thrown.expect(SQLException.class);
            thrown.expectMessage("Values not bound to statement");
            super.streamNullLengthLong();
        }
    }

    public static class BindBigDecimal extends BindBigDecimalTest {
        @Override
        public DataSource db() {
            return db;
        }
    }


    public static class BindBinaryStream extends BindBinaryStreamTest {

        @Rule
        public ExpectedException thrown = ExpectedException.none();

        @Override
        public DataSource db() {
            return db;
        }

        @Test
        @Override
        public void inputStream() throws Exception {
            thrown.expect(SQLException.class);
            thrown.expectMessage("Values not bound to statement");
            super.inputStream();
        }

        @Test
        @Override
        public void inputStreamNull() throws Exception {
            thrown.expect(SQLException.class);
            thrown.expectMessage("Values not bound to statement");
            super.inputStreamNull();
        }

        @Test
        @Override
        public void inputLengthLongStream() throws Exception {
            thrown.expect(SQLException.class);
            thrown.expectMessage("Values not bound to statement");
            super.inputLengthLongStream();
        }

        @Test
        @Override
        public void inputLengthStreamNull() throws Exception {
            thrown.expect(NullPointerException.class); //appears to be driver bug, not null safe
            super.inputLengthStreamNull();
        }
    }

    public static class BindBlob extends BindBlobTest {

        @Rule
        public ExpectedException thrown = ExpectedException.none();

        @Override
        public DataSource db() {
            return db;
        }

        @Test
        @Override
        public void value() throws Exception {
            thrown.expect(AssertionError.class);
            thrown.expectMessage("Driver created null blob");
            super.value();
        }

        @Test
        @Override
        public void valueNull() throws Exception {
            thrown.expect(AssertionError.class);
            thrown.expectMessage("Driver created null blob");
            super.valueNull();
        }

        @Test
        @Override
        public void inputStream() throws Exception {
            thrown.expect(SQLException.class);
            thrown.expectMessage("Values not bound to statement");
            super.inputStream();
        }

        @Test
        @Override
        public void inputStreamNull() throws Exception {
            thrown.expect(SQLException.class);
            thrown.expectMessage("Values not bound to statement");
            super.inputStreamNull();
        }

        @Test
        @Override
        public void inputStreamLength() throws Exception {
            thrown.expect(SQLException.class);
            thrown.expectMessage("Values not bound to statement");
            super.inputStreamLength();
        }

        @Test
        @Override
        public void inputStreamNullLength() throws Exception {
            thrown.expect(SQLException.class);
            thrown.expectMessage("Values not bound to statement");
            super.inputStreamNullLength();
        }
    }

    public static class BindBoolean extends BindBooleanTest{
        @Override
        public DataSource db() {
            return db;
        }
    }

    public static class BindByte extends BindByteTest {
        @Override
        public DataSource db() {
            return db;
        }
    }


    public static class BindByteArray extends BindByteArrayTest {
        @Override
        public DataSource db() {
            return db;
        }
    }

    public static class BindCharacterStream extends BindCharacterStreamTest {

        @Rule
        public ExpectedException thrown = ExpectedException.none();

        @Override
        public DataSource db() {
            return db;
        }

        @Test
        @Override
        public void reader() throws Exception {
            thrown.expect(SQLException.class);
            thrown.expectMessage("Values not bound to statement");
            super.reader();
        }

        @Test
        @Override
        public void readerNull() throws Exception {
            thrown.expect(SQLException.class);
            thrown.expectMessage("Values not bound to statement");
            super.readerNull();
        }

        @Test
        @Override
        public void readerLengthNull() throws Exception {
            thrown.expect(NullPointerException.class); //appears to be driver bug
            super.readerLengthNull();
        }

        @Test
        @Override
        public void readerLengthLong() throws Exception {
            thrown.expect(SQLException.class);
            thrown.expectMessage("Values not bound to statement");
            super.readerLengthLong();
        }

        @Test
        @Override
        public void readerLengthLongNull() throws Exception {
            thrown.expect(SQLException.class);
            thrown.expectMessage("Values not bound to statement");
            super.readerLengthLongNull();
        }
    }

    public static class BindClob extends BindClobTest {

        @Rule
        public ExpectedException thrown = ExpectedException.none();

        @Override
        public DataSource db() {
            return db;
        }

        @Override
        public void value() throws Exception {
            thrown.expect(AssertionError.class);
            thrown.expectMessage("Driver created null clob");
            super.value();
        }

        @Override
        public void valueNull() throws Exception {
            thrown.expect(SQLException.class);
            thrown.expectMessage("not implemented by SQLite JDBC driver");
            super.valueNull();
        }

        @Override
        public void reader() throws Exception {
            thrown.expect(SQLException.class);
            thrown.expectMessage("Values not bound to statement");
            super.reader();
        }

        @Override
        public void readerNull() throws Exception {
            thrown.expect(SQLException.class);
            thrown.expectMessage("Values not bound to statement");
            super.readerNull();
        }

        @Override
        public void readerLength() throws Exception {
            thrown.expect(SQLException.class);
            thrown.expectMessage("Values not bound to statement");
            super.readerLength();
        }

        @Override
        public void readerLengthNull() throws Exception {
            thrown.expect(SQLException.class);
            thrown.expectMessage("Values not bound to statement");
            super.readerLengthNull();
        }
    }

    public static class BindDate extends BindDateTest{
        @Override
        public DataSource db() {
            return db;
        }

        @Override
        public SimpleBuilder builder() {
            return new SimpleBuilder("BIGINT");
        }
    }

    public static class BindDouble extends BindDoubleTest {

        @Override
        public DataSource db() {
            return db;
        }
    }

    public static class BindFloat extends BindFloatTest {

        @Override
        public DataSource db() {
            return db;
        }
    }

    public static class BindInteger extends BindIntegerTest {

        @Override
        public DataSource db() {
            return db;
        }
    }

    public static class BindLong extends BindLongTest {

        @Override
        public DataSource db() {
            return db;
        }
    }


    public static class BindNCharacterStream extends BindNCharacterStreamTest {

        @Rule
        public ExpectedException thrown = ExpectedException.none();

        @Override
        public DataSource db() {
            return db;
        }

        @Test
        @Override
        public void reader() throws Exception {
            thrown.expect(SQLException.class);
            thrown.expectMessage("Values not bound to statement");
            super.reader();
        }

        @Test
        @Override
        public void readerNull() throws Exception {
            thrown.expect(SQLException.class);
            thrown.expectMessage("Values not bound to statement");
            super.readerNull();
        }

        @Test
        @Override
        public void readerLength() throws Exception {
            thrown.expect(SQLException.class);
            thrown.expectMessage("Values not bound to statement");
            super.readerLength();
        }

        @Test
        @Override
        public void readerNullLength() throws Exception {
            thrown.expect(SQLException.class);
            thrown.expectMessage("Values not bound to statement");
            super.readerNullLength();
        }
    }

    public static class BindNClob extends BindNClobTest {

        @Rule
        public ExpectedException thrown = ExpectedException.none();

        @Override
        public DataSource db() {
            return db;
        }

        @Override
        public void value() throws Exception {
            thrown.expect(AssertionError.class);
            thrown.expectMessage("Driver created null NClob");
            super.value();
        }

        @Override
        public void valueNull() throws Exception {
            thrown.expect(SQLException.class);
            thrown.expectMessage("Values not bound to statement");
            super.valueNull();
        }

        @Override
        public void reader() throws Exception {
            thrown.expect(SQLException.class);
            thrown.expectMessage("Values not bound to statement");
            super.reader();
        }

        @Override
        public void readerNull() throws Exception {
            thrown.expect(SQLException.class);
            thrown.expectMessage("Values not bound to statement");
            super.readerNull();
        }

        @Override
        public void readerLength() throws Exception {
            thrown.expect(SQLException.class);
            thrown.expectMessage("Values not bound to statement");
            super.readerLength();
        }

        @Override
        public void readerNullLength() throws Exception {
            thrown.expect(SQLException.class);
            thrown.expectMessage("Values not bound to statement");
            super.readerNullLength();
        }
    }

    public static class BindNString extends BindNStringTest {

        @Rule
        public ExpectedException thrown = ExpectedException.none();

        @Override
        public DataSource db() {
            return db;
        }

        @Override
        public void value() throws Exception {
            thrown.expect(SQLException.class);
            thrown.expectMessage("Values not bound to statement");
            super.value();
        }

        @Override
        public void Null() throws Exception {
            thrown.expect(SQLException.class);
            thrown.expectMessage("Values not bound to statement");
            super.Null();
        }
    }

    public static class BindNull extends BindNullTest {

        @Override
        public DataSource db() {
            return db;
        }

    }

    public static class BindObject extends BindObjectTest {

        @Override
        public DataSource db() {
            return db;
        }

        @Override
        @Test(expected = SQLFeatureNotSupportedException.class)
        public void valueSQLType() throws Exception {
            super.valueSQLType();
        }

        @Override
        @Test(expected = SQLFeatureNotSupportedException.class)
        public void valueNullSQLType() throws Exception {
            super.valueNullSQLType();
        }

        @Override
        @Test(expected = SQLFeatureNotSupportedException.class)
        public void valueSQLTypeLength() throws Exception {
            super.valueSQLTypeLength();
        }

        @Override
        @Test(expected = SQLFeatureNotSupportedException.class)
        public void valueNullSQLTypeLength() throws Exception {
            super.valueNullSQLTypeLength();
        }
    }

    public static class BindShort extends BindShortTest {

        @Override
        public DataSource db() {
            return db;
        }
    }

    public static class BindSQLXML extends BindSQLXMLTest {

        @Rule
        public ExpectedException thrown = ExpectedException.none();

        @Override
        public DataSource db() {
            return db;
        }

        @Override
        public void value() throws Exception {
            thrown.expect(AssertionError.class);
            thrown.expectMessage("Driver returned null xml");
            super.value();
        }

        @Override
        public void Null() throws Exception {
            thrown.expect(SQLException.class);
            thrown.expectMessage("Values not bound to statement");
            super.Null();
        }
    }

    public static class BindString extends BindStringTest {

        @Override
        public DataSource db() {
            return db;
        }
    }

    public static class BindTime extends BindTimeTest {

        @Override
        public DataSource db() {
            return db;
        }

        @Override
        public SimpleBuilder builder() {
            return new SimpleBuilder("BIGINT");
        }
    }

    public static class BindTimestamp extends BindTimestampTest {

        @Override
        public DataSource db() {
            return db;
        }

        @Override
        public SimpleBuilder builder() {
            return new SimpleBuilder("BIGINT");
        }
    }

    public static class BindURL extends BindURLTest {

        @Rule
        public ExpectedException thrown = ExpectedException.none();

        @Override
        public DataSource db() {
            return db;
        }

        @Override
        @Test
        public void value() throws Exception {
            thrown.expect(SQLException.class);
            thrown.expectMessage("not implemented by SQLite JDBC driver");
            super.value();
        }

        @Override
        @Test
        public void Null() throws Exception {
            thrown.expect(SQLException.class);
            thrown.expectMessage("not implemented by SQLite JDBC driver");
            super.Null();
        }
    }
}
