package com.github.randyp.jdbj.db.postgres_9_4;

import com.github.randyp.jdbj.test.SimpleBuilder;
import com.github.randyp.jdbj.test.binding.value.*;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import javax.sql.DataSource;
import java.sql.SQLFeatureNotSupportedException;

@RunWith(Enclosed.class)
public class ValueBindingBuilderTest {

    @ClassRule
    public static final PGRule db = new PGRule();

    public static class BindArray extends BindArrayTest {
        @Override
        public DataSource db() {
            return db;
        }

        @Override
        public SimpleBuilder builder() {
            return new SimpleBuilder("varchar[]");
        }
    }

    public static class BindAsciiStream extends BindAsciiStreamTest {
        @Override
        public DataSource db() {
            return db;
        }

        @Override
        @Test(expected = SQLFeatureNotSupportedException.class)
        public void stream() throws Exception {
            super.stream();
        }

        @Override
        @Test(expected = SQLFeatureNotSupportedException.class)
        public void streamNull() throws Exception {
            super.streamNull();
        }

        @Override
        @Test(expected = SQLFeatureNotSupportedException.class)
        public void streamLengthLong() throws Exception {
            super.streamLengthLong();
        }

        @Override
        @Test(expected = SQLFeatureNotSupportedException.class)
        public void streamNullLengthLong() throws Exception {
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
        @Override
        public DataSource db() {
            return db;
        }

        @Override
        @Test(expected = NullPointerException.class) //appears to be bug in driver "array.length" when array is null
        public void inputStreamNull() throws Exception {
            super.inputStreamNull();
        }

        @Override
        public SimpleBuilder builder() {
            return new SimpleBuilder("bytea");
        }
    }

    public static class BindBlob extends BindBlobTest {
        @Override
        public DataSource db() {
            return db;
        }

        @Override
        @Test(expected = SQLFeatureNotSupportedException.class)
        public void value() throws Exception {
            super.value();
        }

        @Override
        @Test(expected = SQLFeatureNotSupportedException.class)
        public void valueNull() throws Exception {
            super.valueNull();
        }
    }

    public static class BindBoolean extends BindBooleanTest{
        @Override
        public DataSource db() {
            return db;
        }
    }

    public static class BindByte extends BindByteTest{
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

        @Override
        public SimpleBuilder builder() {
            return new SimpleBuilder("bytea");
        }
    }

    public static class BindCharacterStream extends BindCharacterStreamTest {
        @Override
        public DataSource db() {
            return db;
        }

        @Override
        @Test(expected = SQLFeatureNotSupportedException.class)
        public void readerLengthLongNull() throws Exception {
            super.readerLengthLongNull();
        }

        @Override
        @Test(expected = SQLFeatureNotSupportedException.class)
        public void readerLengthLong() throws Exception {
            super.readerLengthLong();
        }

        @Override
        @Test(expected = SQLFeatureNotSupportedException.class)
        public void reader() throws Exception {
            super.reader();
        }

        @Override
        @Test(expected = SQLFeatureNotSupportedException.class)
        public void readerNull() throws Exception {
            super.readerNull();
        }
    }

    public static class BindClob extends BindClobTest {
        @Override
        public DataSource db() {
            return db;
        }

        @Override
        @Test(expected = SQLFeatureNotSupportedException.class)
        public void value() throws Exception {
            super.value();
        }

        @Override
        @Test(expected = SQLFeatureNotSupportedException.class)
        public void reader() throws Exception {
            super.reader();
        }

        @Override
        @Test(expected = SQLFeatureNotSupportedException.class)
        public void readerNull() throws Exception {
            super.readerNull();
        }

        @Override
        @Test(expected = SQLFeatureNotSupportedException.class)
        public void readerLength() throws Exception {
            super.readerLength();
        }

        @Override
        @Test(expected = SQLFeatureNotSupportedException.class)
        public void readerLengthNull() throws Exception {
            super.readerLengthNull();
        }
    }

    public static class BindDate extends BindDateTest{
        @Override
        public DataSource db() {
            return db;
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

        @Override
        public DataSource db() {
            return db;
        }

        @Override
        @Test(expected = SQLFeatureNotSupportedException.class)
        public void reader() throws Exception {
            super.reader();
        }

        @Override
        @Test(expected = SQLFeatureNotSupportedException.class)
        public void readerNull() throws Exception {
            super.readerNull();
        }

        @Override
        @Test(expected = SQLFeatureNotSupportedException.class)
        public void readerLength() throws Exception {
            super.readerLength();
        }

        @Override
        @Test(expected = SQLFeatureNotSupportedException.class)
        public void readerNullLength() throws Exception {
            super.readerNullLength();
        }
    }

    public static class BindNClob extends BindNClobTest {

        @Override
        public DataSource db() {
            return db;
        }

        @Override
        @Test(expected = SQLFeatureNotSupportedException.class)
        public void value() throws Exception {
            super.value();
        }

        @Override
        @Test(expected = SQLFeatureNotSupportedException.class)
        public void valueNull() throws Exception {
            super.valueNull();
        }

        @Override
        @Test(expected = SQLFeatureNotSupportedException.class)
        public void reader() throws Exception {
            super.reader();
        }

        @Override
        @Test(expected = SQLFeatureNotSupportedException.class)
        public void readerNull() throws Exception {
            super.readerNull();
        }

        @Override
        @Test(expected = SQLFeatureNotSupportedException.class)
        public void readerLength() throws Exception {
            super.readerLength();
        }

        @Override
        @Test(expected = SQLFeatureNotSupportedException.class)
        public void readerNullLength() throws Exception {
            super.readerNullLength();
        }
    }

    public static class BindNString extends BindNStringTest {

        @Override
        public DataSource db() {
            return db;
        }

        @Override
        @Test(expected = SQLFeatureNotSupportedException.class)
        public void value() throws Exception {
            super.value();
        }

        @Override
        @Test(expected = SQLFeatureNotSupportedException.class)
        public void Null() throws Exception {
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

        @Override
        public DataSource db() {
            return db;
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
    }

    public static class BindTimestamp extends BindTimestampTest {

        @Override
        public DataSource db() {
            return db;
        }
    }

    public static class BindURL extends BindURLTest {

        @Override
        public DataSource db() {
            return db;
        }

        @Override
        @Test(expected = SQLFeatureNotSupportedException.class)
        public void value() throws Exception {
            super.value();
        }

        @Override
        @Test(expected = SQLFeatureNotSupportedException.class)
        public void Null() throws Exception {
            super.Null();
        }
    }
}
