package com.github.randyp.jdbj.db.h2_1_4;

import com.github.randyp.jdbj.test.binding.value.*;
import org.h2.jdbc.JdbcSQLException;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import javax.sql.DataSource;
import java.sql.SQLFeatureNotSupportedException;

@RunWith(Enclosed.class)
public class ValueBindingBuilderTest {

    @ClassRule
    public static final H2Rule db = new H2Rule();

    public static class BindArray extends BindArrayTest {

        @Rule
        public ExpectedException thrown = ExpectedException.none();

        @Override
        public DataSource db() {
            return db;
        }

        @Override
        public String castType() {
            return "varchar";
        }

        @Override
        public void value() throws Exception {
            thrown.expect(JdbcSQLException.class);
            thrown.expectMessage("Feature not supported: \"createArray\" [50100-187]");
            super.value();
        }

        @Override
        public void Null() throws Exception {
            thrown.expect(JdbcSQLException.class);
            thrown.expectMessage("Feature not supported: \"setArray\" [50100-187]");
            super.Null();
        }
    }

    public static class BindAsciiStream extends BindAsciiStreamTest {
        @Override
        public DataSource db() {
            return db;
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
    }

    public static class BindBlob extends BindBlobTest {

        @Rule
        public ExpectedException thrown = ExpectedException.none();

        @Override
        public DataSource db() {
            return db;
        }

        //appears to be bug in driver
        @Override
        @Test
        public void valueNull() throws Exception {
            thrown.expect(JdbcSQLException.class);
            thrown.expectMessage("General error: \"java.lang.NullPointerException\" [50000-187]");
            super.valueNull();
        }
    }

    public static class BindBoolean extends BindBooleanTest {
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
        @Override
        public DataSource db() {
            return db;
        }
    }

    public static class BindClob extends BindClobTest {
        @Override
        public DataSource db() {
            return db;
        }
    }

    public static class BindDate extends BindDateTest {
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
    }

    public static class BindNClob extends BindNClobTest {

        @Override
        public DataSource db() {
            return db;
        }
    }

    public static class BindNString extends BindNStringTest {

        @Override
        public DataSource db() {
            return db;
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

        @Override
        @Test(expected = JdbcSQLException.class)
        public void value() throws Exception {
            super.value();
        }

        @Override
        @Test(expected = JdbcSQLException.class)
        public void Null() throws Exception {
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
    }

    public static class BindTimestamp extends BindTimeTest {

        @Override
        public DataSource db() {
            return db;
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
            thrown.expect(JdbcSQLException.class);
            thrown.expectMessage("Feature not supported");
            super.value();
        }

        @Override
        @Test
        public void Null() throws Exception {
            thrown.expect(JdbcSQLException.class);
            thrown.expectMessage("Feature not supported");
            super.Null();
        }
    }
}
