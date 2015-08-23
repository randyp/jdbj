package com.github.randyp.jdbj.db.mysql_5_1;

import com.github.randyp.jdbj.SimpleBuilder;
import com.github.randyp.jdbj.test.binding.value.*;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(Enclosed.class)
public class ValueBindingBuilderTest {

    @ClassRule
    public static final MySqlRule db = new MySqlRule();

    public static class BindArray extends BindArrayTest {
        @Override
        public DataSource db() {
            return db;
        }

        @Test(expected = SQLFeatureNotSupportedException.class)
        @Override
        public void value() throws Exception {
            super.value();
        }

        @Test(expected = SQLFeatureNotSupportedException.class)
        @Override
        public void Null() throws Exception {
            super.Null();
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
        public SimpleBuilder builder() {
            return new SimpleBuilder("char(500)");
        }
    }

    public static class BindBigDecimal extends BindBigDecimalTest {
        @Override
        public DataSource db() {
            return db;
        }

        @Override
        public SimpleBuilder builder() {
            return new SimpleBuilder("char(500)");
        }
    }

    public static class BindBinaryStream extends BindBinaryStreamTest {
        @Override
        public DataSource db() {
            return db;
        }

        @Override
        public SimpleBuilder builder() {
            return new SimpleBuilder("char(500)");
        }
    }

    public static class BindBlob extends BindBlobTest {
        @Override
        public DataSource db() {
            return db;
        }

        @Test
        @Override
        public void valueNull() throws Exception {
            try {
                super.valueNull();
                fail("Supposed to except because of driver bug");
            } catch (SQLException e) {
                assertTrue(e.getCause() instanceof NullPointerException);
            }
        }

        @Override
        public SimpleBuilder builder() {
            return new SimpleBuilder("char(500)");
        }
    }

    public static class BindBoolean extends BindBooleanTest{
        @Override
        public DataSource db() {
            return db;
        }

        @Override
        public SimpleBuilder builder() {
            return new SimpleBuilder("char(500)");
        }
    }

    public static class BindByte extends BindByteTest{
        @Override
        public DataSource db() {
            return db;
        }

        @Override
        public SimpleBuilder builder() {
            return new SimpleBuilder("char(500)");
        }
    }

    public static class BindByteArray extends BindByteArrayTest {
        @Override
        public DataSource db() {
            return db;
        }

        @Override
        public SimpleBuilder builder() {
            return new SimpleBuilder("char(500)");
        }
    }

    public static class BindCharacterStream extends BindCharacterStreamTest {
        @Override
        public DataSource db() {
            return db;
        }

        @Override
        public SimpleBuilder builder() {
            return new SimpleBuilder("char(500)");
        }
    }

    public static class BindClob extends BindClobTest {
        @Override
        public DataSource db() {
            return db;
        }

        @Override
        public SimpleBuilder builder() {
            return new SimpleBuilder("char(500)");
        }
    }

    public static class BindDate extends BindDateTest{
        @Override
        public DataSource db() {
            return db;
        }

        @Override
        public SimpleBuilder builder() {
            return new SimpleBuilder("char(500)");
        }
    }

    public static class BindDouble extends BindDoubleTest {

        @Override
        public DataSource db() {
            return db;
        }

        @Override
        public SimpleBuilder builder() {
            return new SimpleBuilder("char(500)");
        }
    }

    public static class BindFloat extends BindFloatTest {

        @Override
        public DataSource db() {
            return db;
        }

        @Override
        public SimpleBuilder builder() {
            return new SimpleBuilder("char(500)");
        }
    }

    public static class BindInteger extends BindIntegerTest {

        @Override
        public DataSource db() {
            return db;
        }

        @Override
        public SimpleBuilder builder() {
            return new SimpleBuilder("char(500)");
        }
    }

    public static class BindLong extends BindLongTest {

        @Override
        public DataSource db() {
            return db;
        }

        @Override
        public SimpleBuilder builder() {
            return new SimpleBuilder("char(500)");
        }
    }

    public static class BindNCharacterStream extends BindNCharacterStreamTest {

        @Override
        public DataSource db() {
            return db;
        }

        @Override
        public SimpleBuilder builder() {
            return new SimpleBuilder("char(500) CHARACTER SET utf8");
        }
    }

    public static class BindNClob extends BindNClobTest {

        @Override
        public DataSource db() {
            return db;
        }

        @Override
        public SimpleBuilder builder() {
            return new SimpleBuilder("char(500) CHARACTER SET utf8");
        }
    }

    public static class BindNString extends BindNStringTest {

        @Override
        public DataSource db() {
            return db;
        }

        @Override
        public SimpleBuilder builder() {
            return new SimpleBuilder("char(500) CHARACTER SET utf8");
        }
    }

    public static class BindNull extends BindNullTest {

        @Override
        public DataSource db() {
            return db;
        }

        @Override
        public SimpleBuilder builder() {
            return new SimpleBuilder("char(500)");
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

        @Override
        public SimpleBuilder builder() {
            return new SimpleBuilder("char(500)");
        }
    }

    public static class BindShort extends BindShortTest {

        @Override
        public DataSource db() {
            return db;
        }

        @Override
        public SimpleBuilder builder() {
            return new SimpleBuilder("char(500)");
        }
    }

    public static class BindSQLXML extends BindSQLXMLTest {

        @Override
        public DataSource db() {
            return db;
        }

        @Test(expected = NullPointerException.class) //appears to be bug in driver \"return this.owningResultSet.getCharacterStream(this.columnIndexOfXml);\"
        @Override
        public void value() throws Exception {
            super.value();
        }

        @Override
        public SimpleBuilder builder() {
            return new SimpleBuilder("char(500)");
        }
    }

    public static class BindString extends BindStringTest {

        @Override
        public DataSource db() {
            return db;
        }

        @Override
        public SimpleBuilder builder() {
            return new SimpleBuilder("char(500)");
        }
    }

    public static class BindTime extends BindTimeTest {

        @Override
        public DataSource db() {
            return db;
        }

        @Override
        public SimpleBuilder builder() {
            return new SimpleBuilder("char(500)");
        }
    }

    public static class BindTimestamp extends BindTimestampTest {

        @Override
        public DataSource db() {
            return db;
        }

        @Override
        public SimpleBuilder builder() {
            return new SimpleBuilder("char(500)");
        }
    }

    public static class BindURL extends BindURLTest {

        @Override
        public DataSource db() {
            return db;
        }

        @Override
        public SimpleBuilder builder() {
            return new SimpleBuilder("char(500)");
        }
    }

}
