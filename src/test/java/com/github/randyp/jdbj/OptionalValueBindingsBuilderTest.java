package com.github.randyp.jdbj;

import com.github.randyp.jdbj.db.h2_1_4.H2Rule;
import com.github.randyp.jdbj.db.postgres_9_4.PGRule;
import com.github.randyp.jdbj.optional.OptionalBoolean;
import com.github.randyp.jdbj.optional.OptionalByte;
import com.github.randyp.jdbj.optional.OptionalFloat;
import com.github.randyp.jdbj.optional.OptionalShort;
import com.github.randyp.jdbj.test.SimpleBuilder;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.*;
import java.sql.Date;
import java.util.*;

import static org.junit.Assert.*;

@RunWith(Enclosed.class)
public class OptionalValueBindingsBuilderTest {

    public static class BindArray {

        @ClassRule
        public static final PGRule db = new PGRule();

        @Test
        public void bind() throws Exception {
            final String[] expected = {"a", "b", "c"};
            try (Connection connection = db.getConnection()) {
                final String[] selected = new SimpleBuilder("varchar[]")
                        .bindDefaultArray(":binding", null)
                        .bindOptionalArray(":binding", Optional.of(connection.createArrayOf("varchar", expected)))
                        .execute(connection, rs -> (String[]) rs.getSQLArray(1).getArray());
                assertArrayEquals(expected, selected);
            }
        }

        @Test
        public void bindEmpty() throws Exception {
            final String[] expected = {"a", "b", "c"};
            try (Connection connection = db.getConnection()) {
                final String[] selected = new SimpleBuilder("varchar[]")
                        .bindDefaultArray(":binding", connection.createArrayOf("varchar", expected))
                        .bindOptionalArray(":binding", Optional.empty())
                        .execute(connection, rs -> {
                            final Array array = rs.getSQLArray(1);
                            return array == null ? null : (String[]) array.getArray();
                        });
                assertArrayEquals(expected, selected);
            }
        }
    }

    public static class BindAsciiStream {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void inputStream() throws Exception {
            final String expected = "abcde";
            final String selected = new SimpleBuilder()
                    .bindDefaultAsciiStream(":binding", null)
                    .bindOptionalAsciiStream(":binding", Optional.of(new ByteArrayInputStream(expected.getBytes(Charset.forName("ascii")))))
                    .execute(db, rs -> rs.getString(1));
            assertEquals(expected, selected);
        }

        @Test
        public void inputStreamEmpty() throws Exception {
            final String expected = "abcde";
            final String selected = new SimpleBuilder()
                    .bindDefaultAsciiStream(":binding", new ByteArrayInputStream(expected.getBytes(Charset.forName("ascii"))))
                    .bindOptionalAsciiStream(":binding", Optional.empty())
                    .execute(db, rs -> rs.getString(1));
            assertEquals(expected, selected);
        }

        @Test
        public void inputStreamLength() throws Exception {
            final String expected = "abcde";
            final String selected = new SimpleBuilder()
                    .bindDefaultAsciiStream(":binding", null, expected.length())
                    .bindOptionalAsciiStream(":binding", Optional.of(new ByteArrayInputStream(expected.getBytes(Charset.forName("ascii")))), expected.length())
                    .execute(db, rs -> rs.getString(1));
            assertEquals(expected, selected);
        }

        @Test
        public void inputStreamLengthEmpty() throws Exception {
            final String expected = "abcde";
            final String selected = new SimpleBuilder()
                    .bindDefaultAsciiStream(":binding", new ByteArrayInputStream(expected.getBytes(Charset.forName("ascii"))), expected.length())
                    .bindOptionalAsciiStream(":binding", Optional.empty(), expected.length())
                    .execute(db, rs -> rs.getString(1));
            assertEquals(expected, selected);
        }

        @Test
        public void inputStreamLengthLong() throws Exception {
            final String expected = "abcde";
            final String selected = new SimpleBuilder()
                    .bindDefaultAsciiStream(":binding", null, (long) expected.length())
                    .bindOptionalAsciiStream(":binding", Optional.of(new ByteArrayInputStream(expected.getBytes(Charset.forName("ascii")))), (long) expected.length())
                    .execute(db, rs -> rs.getString(1));
            assertEquals(expected, selected);
        }

        @Test
        public void inputStreamLengthLongEmpty() throws Exception {
            final String expected = "abcde";
            final String selected = new SimpleBuilder()
                    .bindDefaultAsciiStream(":binding", new ByteArrayInputStream(expected.getBytes(Charset.forName("ascii"))), (long) expected.length())
                    .bindOptionalAsciiStream(":binding", Optional.empty(), (long) expected.length())
                    .execute(db, rs -> rs.getString(1));
            assertEquals(expected, selected);
        }
    }

    public static class BindBigDecimal {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void value() throws Exception {
            final BigDecimal expected = new BigDecimal("1.234");
            final BigDecimal selected = new SimpleBuilder()
                    .bindDefaultBigDecimal(":binding", null)
                    .bindOptionalBigDecimal(":binding", Optional.of(expected))
                    .execute(db, rs -> rs.getBigDecimal(1));
            assertEquals(expected, selected);
        }

        @Test
        public void nullValue() throws Exception {
            final BigDecimal expected = new BigDecimal("1.234");
            final BigDecimal selected = new SimpleBuilder()
                    .bindDefaultBigDecimal(":binding", expected)
                    .bindOptionalBigDecimal(":binding", Optional.empty())
                    .execute(db, rs -> rs.getBigDecimal(1));
            assertEquals(expected, selected);
        }
    }

    public static class BindBinaryStream {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void inputStream() throws Exception {
            final byte[] expected = "abcde".getBytes();
            final byte[] selected = new SimpleBuilder()
                    .bindDefaultBinaryStream(":binding", null)
                    .bindOptionalBinaryStream(":binding", Optional.of(new ByteArrayInputStream(expected)))
                    .execute(db, rs -> rs.getBytes(1));
            assertArrayEquals(expected, selected);
        }

        @Test
        public void inputStreamEmpty() throws Exception {
            final byte[] expected = "abcde".getBytes();
            final byte[] selected = new SimpleBuilder()
                    .bindDefaultBinaryStream(":binding", new ByteArrayInputStream(expected))
                    .bindOptionalBinaryStream(":binding", Optional.empty())
                    .execute(db, rs -> rs.getBytes(1));
            assertArrayEquals(expected, selected);
        }

        @Test
        public void inputLengthStream() throws Exception {
            final byte[] expected = "abcde".getBytes();
            final byte[] selected = new SimpleBuilder()
                    .bindDefaultBinaryStream(":binding", null, expected.length)
                    .bindOptionalBinaryStream(":binding", Optional.of(new ByteArrayInputStream(expected)), expected.length)
                    .execute(db, rs -> rs.getBytes(1));
            assertArrayEquals(expected, selected);
        }

        @Test
        public void inputLengthStreamEmpty() throws Exception {
            final byte[] expected = "abcde".getBytes();
            final byte[] selected = new SimpleBuilder()
                    .bindDefaultBinaryStream(":binding", new ByteArrayInputStream(expected), expected.length)
                    .bindOptionalBinaryStream(":binding", Optional.empty(), expected.length)
                    .execute(db, rs -> rs.getBytes(1));
            assertArrayEquals(expected, selected);
        }

        @Test
        public void inputLengthLongStream() throws Exception {
            final byte[] expected = "abcde".getBytes();
            final byte[] selected = new SimpleBuilder()
                    .bindDefaultBinaryStream(":binding", null, (long) expected.length)
                    .bindOptionalBinaryStream(":binding", Optional.of(new ByteArrayInputStream(expected)), (long) expected.length)
                    .execute(db, rs -> rs.getBytes(1));
            assertArrayEquals(expected, selected);
        }

        @Test
        public void inputLengthLongStreamEmpty() throws Exception {
            final byte[] expected = "abcde".getBytes();
            final byte[] selected = new SimpleBuilder()
                    .bindDefaultBinaryStream(":binding", new ByteArrayInputStream(expected), (long) expected.length)
                    .bindOptionalBinaryStream(":binding", Optional.empty(), (long) expected.length)
                    .execute(db, rs -> rs.getBytes(1));
            assertArrayEquals(expected, selected);
        }
    }

    public static class BindBlob {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void inputStream() throws Exception {
            final byte[] expected = "abcde".getBytes();
            final byte[] selected = new SimpleBuilder()
                    .bindDefaultBlob(":binding", (InputStream) null)
                    .bindOptionalBlob(":binding", Optional.of(new ByteArrayInputStream(expected)))
                    .execute(db, rs -> rs.getBytes(1));
            assertArrayEquals(expected, selected);
        }

        @Test
        public void inputStreamEmpty() throws Exception {
            final byte[] expected = "abcde".getBytes();
            final byte[] selected = new SimpleBuilder()
                    .bindDefaultBlob(":binding", new ByteArrayInputStream(expected))
                    .bindOptionalBlob(":binding", Optional.empty())
                    .execute(db, rs -> rs.getBytes(1));
            assertArrayEquals(expected, selected);
        }

        @Test
        public void inputStreamLength() throws Exception {
            final byte[] expected = "abcde".getBytes();
            final byte[] selected = new SimpleBuilder()
                    .bindDefaultBlob(":binding", (InputStream) null)
                    .bindOptionalBlob(":binding", Optional.of(new ByteArrayInputStream(expected)), (long) expected.length)
                    .execute(db, rs -> rs.getBytes(1));
            assertArrayEquals(expected, selected);

        }

        @Test
        public void inputStreamNullLength() throws Exception {
            final byte[] expected = "abcde".getBytes();
            final byte[] selected = new SimpleBuilder()
                    .bindDefaultBlob(":binding", new ByteArrayInputStream(expected), (long) expected.length)
                    .bindOptionalBlob(":binding", Optional.empty(), (long) expected.length)
                    .execute(db, rs -> rs.getBytes(1));
            assertArrayEquals(expected, selected);
        }
    }

    public static class BindBoolean {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void False() throws Exception {
            final Boolean selected = new SimpleBuilder()
                    .bindDefaultBoolean(":binding", true)
                    .bindOptionalBoolean(":binding", Optional.of(false))
                    .execute(db, rs -> rs.getBoolean(1));
            assertFalse(selected);
        }

        @Test
        public void True() throws Exception {
            final Boolean selected = new SimpleBuilder()
                    .bindDefaultBoolean(":binding", false)
                    .bindOptionalBoolean(":binding", Optional.of(true))
                    .execute(db, rs -> rs.getBoolean(1));
            assertTrue(selected);
        }

        @Test
        public void notPresent() throws Exception {
            final Boolean selected = new SimpleBuilder()
                    .bindDefaultBoolean(":binding", true)
                    .bindOptionalBoolean(":binding", Optional.empty())
                    .execute(db, rs -> rs.getBoolean(1));
            assertTrue(selected);
        }

        @Test
        public void primitiveFalse() throws Exception {
            final boolean selected = new SimpleBuilder()
                    .bindDefaultBooleanPrimitive(":binding", true)
                    .bindOptionalBoolean(":binding", OptionalBoolean.of(false))
                    .execute(db, rs -> rs.getBooleanPrimitive(1));
            assertFalse(selected);
        }

        @Test
        public void primitiveTrue() throws Exception {
            final boolean selected = new SimpleBuilder()
                    .bindDefaultBooleanPrimitive(":binding", false)
                    .bindOptionalBoolean(":binding", OptionalBoolean.of(true))
                    .execute(db, rs -> rs.getBooleanPrimitive(1));
            assertTrue(selected);
        }

        @Test
        public void primitiveNotPresent() throws Exception {
            final boolean selected = new SimpleBuilder()
                    .bindDefaultBooleanPrimitive(":binding", true)
                    .bindOptionalBoolean(":binding", OptionalBoolean.empty())
                    .execute(db, rs -> rs.getBooleanPrimitive(1));
            assertTrue(selected);
        }
    }

    public static class BindByte {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void present() throws Exception {
            final Byte expected = 6;
            final Byte selected = new SimpleBuilder()
                    .bindDefaultByte(":binding", (byte) 7)
                    .bindOptionalByte(":binding", Optional.of(expected))
                    .execute(db, rs -> rs.getByte(1));
            assertEquals(expected, selected, 0.0);
        }

        @Test
        public void notPresent() throws Exception {
            final Byte expected = 6;
            final Byte selected = new SimpleBuilder()
                    .bindDefaultByte(":binding", expected)
                    .bindOptionalByte(":binding", Optional.empty())
                    .execute(db, rs -> rs.getByte(1));
            assertEquals(expected, selected, 0.0);
        }

        @Test
        public void primitivePresent() throws Exception {
            final byte expected = 6;
            final byte selected = new SimpleBuilder()
                    .bindDefaultBytePrimtive(":binding", (byte) 7)
                    .bindOptionalByte(":binding", OptionalByte.of(expected))
                    .execute(db, rs -> rs.getBytePrimitive(1));
            assertEquals(expected, selected, 0.0);
        }

        @Test
        public void primitiveNotPresent() throws Exception {
            final byte expected = 6;
            final byte selected = new SimpleBuilder()
                    .bindDefaultBytePrimtive(":binding", expected)
                    .bindOptionalByte(":binding", OptionalByte.empty())
                    .execute(db, rs -> rs.getBytePrimitive(1));
            assertEquals(expected, selected, 0.0);
        }
    }

    public static class BindByteArray {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void value() throws Exception {
            final byte[] expected = "abcde".getBytes();
            final byte[] selected = new SimpleBuilder()
                    .bindDefaultByteArray(":binding", null)
                    .bindOptionalByteArray(":binding", Optional.of(expected))
                    .execute(db, rs -> rs.getBytes(1));
            assertArrayEquals(expected, selected);
        }

        @Test
        public void valueEmpty() throws Exception {
            final byte[] expected = "abcde".getBytes();
            final byte[] selected = new SimpleBuilder()
                    .bindDefaultByteArray(":binding", expected)
                    .bindOptionalBinaryStream(":binding", Optional.empty())
                    .execute(db, rs -> rs.getBytes(1));
            assertArrayEquals(expected, selected);
        }
    }

    public static class BindCharacterStream {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void reader() throws Exception {
            final String expected = "abcde";
            final String selected = new SimpleBuilder()
                    .bindDefaultCharacterStream(":binding", null)
                    .bindOptionalCharacterStream(":binding", Optional.of(new StringReader(expected)))
                    .execute(db, rs -> rs.getString(1));
            assertEquals(expected, selected);
        }

        @Test
        public void readerEmpty() throws Exception {
            final String expected = "abcde";
            final String selected = new SimpleBuilder()
                    .bindDefaultCharacterStream(":binding", new StringReader(expected))
                    .bindOptionalCharacterStream(":binding", Optional.empty())
                    .execute(db, rs -> rs.getString(1));
            assertEquals(expected, selected);
        }

        @Test
        public void readerLength() throws Exception {
            final String expected = "abcde";
            final String selected = new SimpleBuilder()
                    .bindDefaultCharacterStream(":binding", null, expected.length())
                    .bindOptionalCharacterStream(":binding", Optional.of(new StringReader(expected)), expected.length())
                    .execute(db, rs -> rs.getString(1));
            assertEquals(expected, selected);
        }

        @Test
        public void readerLengthEmpty() throws Exception {
            final String expected = "abcde";
            final String selected = new SimpleBuilder()
                    .bindDefaultCharacterStream(":binding", new StringReader(expected), expected.length())
                    .bindOptionalCharacterStream(":binding", Optional.empty(), expected.length())
                    .execute(db, rs -> rs.getString(1));
            assertEquals(expected, selected);
        }

        @Test
        public void readerLengthLong() throws Exception {
            final String expected = "abcde";
            final String selected = new SimpleBuilder()
                    .bindDefaultCharacterStream(":binding", null, (long) expected.length())
                    .bindOptionalCharacterStream(":binding", Optional.of(new StringReader(expected)), (long) expected.length())
                    .execute(db, rs -> rs.getString(1));
            assertEquals(expected, selected);
        }

        @Test
        public void readerLengthLongEmpty() throws Exception {
            final String expected = "abcde";
            final String selected = new SimpleBuilder()
                    .bindDefaultCharacterStream(":binding", new StringReader(expected), (long) expected.length())
                    .bindOptionalCharacterStream(":binding", Optional.empty(), (long) expected.length())
                    .execute(db, rs -> rs.getString(1));
            assertEquals(expected, selected);
        }
    }

    public static class BindClob {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void reader() throws Exception {
            final String expected = "abcd";
            final String selected = new SimpleBuilder()
                    .bindDefaultClob(":binding", (Reader) null)
                    .bindOptionalClob(":binding", Optional.of(new StringReader(expected)))
                    .execute(db, rs -> rs.getClob(1).getSubString(1L, expected.length()));
            assertEquals(expected, selected);
        }

        @Test
        public void readerEmpty() throws Exception {
            final String expected = "abcd";
            final String selected = new SimpleBuilder()
                    .bindDefaultClob(":binding", new StringReader(expected))
                    .bindOptionalClob(":binding", Optional.empty())
                    .execute(db, rs -> {
                        final Clob clob = rs.getClob(1);
                        return clob == null ? null : clob.getSubString(1L, expected.length());
                    });
            assertEquals(expected, selected);
        }

        @Test
        public void readerLength() throws Exception {
            final String expected = "abcd";
            final String selected = new SimpleBuilder()
                    .bindDefaultClob(":binding", (Reader) null)
                    .bindOptionalClob(":binding", Optional.of(new StringReader(expected)), (long) expected.length())
                    .execute(db, rs -> rs.getClob(1).getSubString(1L, expected.length()));
            assertEquals(expected, selected);
        }

        @Test
        public void readerLengthEmpty() throws Exception {
            final String expected = "abcd";
            final String selected = new SimpleBuilder()
                    .bindDefaultClob(":binding", new StringReader(expected), (long) expected.length())
                    .bindOptionalClob(":binding", Optional.empty(), (long) expected.length())
                    .execute(db, rs -> {
                        final Clob clob = rs.getClob(1);
                        return clob == null ? null : clob.getSubString(1L, expected.length());
                    });
            assertEquals(expected, selected);
        }
    }

    public static class BindDate extends HasExpectedTimeSinceEpoch {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void value() throws Exception {
            final Date expected = new Date(expectedTime);
            final Date selected = new SimpleBuilder()
                    .bindDefaultDate(":binding", null)
                    .bindOptionalDate(":binding", Optional.of(expected))
                    .execute(db, rs -> rs.getDate(1));
            assertEquals(expected, selected);
        }

        @Test
        public void valueEmpty() throws Exception {
            final Date expected = new Date(expectedTime);
            final Date selected = new SimpleBuilder()
                    .bindDefaultDate(":binding", expected)
                    .bindOptionalDate(":binding", Optional.empty())
                    .execute(db, rs -> rs.getDate(1));
            assertEquals(expected, selected);
        }

        @Test
        public void valueCalendar() throws Exception {
            final Date expected = new Date(expectedTime);
            final Date selected = new SimpleBuilder()
                    .bindDefaultDate(":binding", null, GregorianCalendar.getInstance())
                    .bindOptionalDate(":binding", Optional.of(expected), GregorianCalendar.getInstance())
                    .execute(db, rs -> rs.getDate(1, GregorianCalendar.getInstance()));
            assertEquals(expected, selected);
        }

        @Test
        public void valueCalendarEmpty() throws Exception {
            final Date expected = new Date(expectedTime);
            final Date selected = new SimpleBuilder()
                    .bindDefaultDate(":binding", expected, GregorianCalendar.getInstance())
                    .bindOptionalDate(":binding", Optional.empty(), GregorianCalendar.getInstance())
                    .execute(db, rs -> rs.getDate(1, GregorianCalendar.getInstance()));
            assertEquals(expected, selected);
        }

    }

    public static class BindDouble {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void value() throws Exception {
            final Double expected = 1.2;
            final Double selected = new SimpleBuilder()
                    .bindDefaultDouble(":binding", Double.MIN_VALUE)
                    .bindOptionalDouble(":binding", Optional.of(expected))
                    .execute(db, rs -> rs.getDouble(1));
            assertEquals(expected, selected, 0.0);
        }

        @Test
        public void notPresent() throws Exception {
            final Double expected = 1.2;
            final Double selected = new SimpleBuilder()
                    .bindDefaultDouble(":binding", expected)
                    .bindOptionalDouble(":binding", Optional.empty())
                    .execute(db, rs -> rs.getDouble(1));
            assertEquals(expected, selected, 0.0);
        }

        @Test
        public void primitive() throws Exception {
            final double expected = 1.2;
            final double selected = new SimpleBuilder()
                    .bindDefaultDoublePrimitive(":binding", Double.MIN_VALUE)
                    .bindOptionalDouble(":binding", OptionalDouble.of(expected))
                    .execute(db, rs -> rs.getDoublePrimitive(1));
            assertEquals(expected, selected, 0.0);
        }

        @Test
        public void primitiveNotPresent() throws Exception {
            final double expected = 1.2;
            final double selected = new SimpleBuilder()
                    .bindDefaultDoublePrimitive(":binding", expected)
                    .bindOptionalDouble(":binding", OptionalDouble.empty())
                    .execute(db, rs -> rs.getDoublePrimitive(1));
            assertEquals(expected, selected, 0.0);
        }
    }

    public static class BindFloat {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void value() throws Exception {
            final Float expected = 1.2f;
            final Float selected = new SimpleBuilder()
                    .bindDefaultFloat(":binding", Float.MIN_VALUE)
                    .bindOptionalFloat(":binding", Optional.of(expected))
                    .execute(db, rs -> rs.getFloat(1));
            assertEquals(expected, selected, 0.0f);
        }

        @Test
        public void notPresent() throws Exception {
            final Float expected = 1.2f;
            final Float selected = new SimpleBuilder()
                    .bindDefaultFloat(":binding", expected)
                    .bindOptionalFloat(":binding", Optional.empty())
                    .execute(db, rs -> rs.getFloat(1));
            assertEquals(expected, selected, 0.0f);
        }

        @Test
        public void primitiveValue() throws Exception {
            final float expected = 1.2f;
            final float selected = new SimpleBuilder()
                    .bindDefaultFloatPrimtitive(":binding", Float.MIN_VALUE)
                    .bindOptionalFloat(":binding", OptionalFloat.of(expected))
                    .execute(db, rs -> rs.getFloatPrimitive(1));
            assertEquals(expected, selected, 0.0f);
        }

        @Test
        public void primitiveNotPresent() throws Exception {
            final float expected = 1.2f;
            final float selected = new SimpleBuilder()
                    .bindDefaultFloatPrimtitive(":binding", expected)
                    .bindOptionalFloat(":binding", OptionalFloat.empty())
                    .execute(db, rs -> rs.getFloatPrimitive(1));
            assertEquals(expected, selected, 0.0f);
        }
    }

    public static class BindInteger {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void value() throws Exception {
            final Integer expected = 12;
            final Integer selected = new SimpleBuilder()
                    .bindDefaultInteger(":binding", Integer.MIN_VALUE)
                    .bindOptionalInteger(":binding", Optional.of(expected))
                    .execute(db, rs -> rs.getInteger(1));
            assertEquals(expected, selected);
        }

        @Test
        public void notPresent() throws Exception {
            final Integer expected = 12;
            final Integer selected = new SimpleBuilder()
                    .bindDefaultInteger(":binding", expected)
                    .bindOptionalInteger(":binding", Optional.empty())
                    .execute(db, rs -> rs.getInteger(1));
            assertEquals(expected, selected);
        }

        @Test
        public void primitiveValue() throws Exception {
            final int expected = 12;
            final int selected = new SimpleBuilder()
                    .bindDefaultIntegerPrimitive(":binding", Integer.MIN_VALUE)
                    .bindOptionalInteger(":binding", OptionalInt.of(expected))
                    .execute(db, rs -> rs.getIntegerPrimitive(1));
            assertEquals(expected, selected);
        }

        @Test
        public void primitiveNotPresent() throws Exception {
            final int expected = 12;
            final int selected = new SimpleBuilder()
                    .bindDefaultIntegerPrimitive(":binding", expected)
                    .bindOptionalInteger(":binding", OptionalInt.empty())
                    .execute(db, rs -> rs.getIntegerPrimitive(1));
            assertEquals(expected, selected);
        }

        @Test
        public void aliasValue() throws Exception {
            final Integer expected = 12;
            final Integer selected = new SimpleBuilder()
                    .bindDefaultInt(":binding", Integer.MIN_VALUE)
                    .bindOptionalInt(":binding", Optional.of(expected))
                    .execute(db, rs -> rs.getInteger(1));
            assertEquals(expected, selected);
        }

        @Test
        public void aliasNotPresent() throws Exception {
            final Integer expected = 12;
            final Integer selected = new SimpleBuilder()
                    .bindDefaultInt(":binding", expected)
                    .bindOptionalInt(":binding", Optional.empty())
                    .execute(db, rs -> rs.getInteger(1));
            assertEquals(expected, selected);
        }

        @Test
        public void primitiveAliasValue() throws Exception {
            final int expected = 12;
            final int selected = new SimpleBuilder()
                    .bindDefaultInt(":binding", Integer.MIN_VALUE)
                    .bindOptionalInt(":binding", OptionalInt.of(expected))
                    .execute(db, rs -> rs.getInt(1));
            assertEquals(expected, selected);
        }

        @Test
        public void primitiveAliasNotPresent() throws Exception {
            final int expected = 12;
            final int selected = new SimpleBuilder()
                    .bindDefaultInt(":binding", expected)
                    .bindOptionalInt(":binding", OptionalInt.empty())
                    .execute(db, rs -> rs.getInt(1));
            assertEquals(expected, selected);
        }
    }

    public static class BindLong {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void value() throws Exception {
            final Long expected = 12L;
            final Long selected = new SimpleBuilder()
                    .bindDefaultLong(":binding", Long.MIN_VALUE)
                    .bindOptionalLong(":binding", Optional.of(expected))
                    .execute(db, rs -> rs.getLong(1));
            assertEquals(expected, selected);
        }

        @Test
        public void notPresent() throws Exception {
            final Long expected = 12L;
            final Long selected = new SimpleBuilder()
                    .bindDefaultLong(":binding", expected)
                    .bindOptionalLong(":binding", Optional.empty())
                    .execute(db, rs -> rs.getLong(1));
            assertEquals(expected, selected);
        }

        @Test
        public void primitiveValue() throws Exception {
            final long expected = 12L;
            final long selected = new SimpleBuilder()
                    .bindDefaultLongPrimitive(":binding", Long.MIN_VALUE)
                    .bindOptionalLong(":binding", OptionalLong.of(expected))
                    .execute(db, rs -> rs.getLongPrimitive(1));
            assertEquals(expected, selected);
        }

        @Test
        public void primitiveNotPresent() throws Exception {
            final long expected = 12L;
            final long selected = new SimpleBuilder()
                    .bindDefaultLongPrimitive(":binding", expected)
                    .bindOptionalLong(":binding", OptionalLong.empty())
                    .execute(db, rs -> rs.getLongPrimitive(1));
            assertEquals(expected, selected);
        }
    }

    public static class BindNCharacterStream {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void reader() throws Exception {
            final String expected = "abcde";
            final String selected = new SimpleBuilder()
                    .bindDefaultNCharacterStream(":binding", null)
                    .bindOptionalNCharacterStream(":binding", Optional.of(new StringReader(expected)))
                    .execute(db, rs -> rs.getString(1));
            assertEquals(expected, selected);
        }

        @Test
        public void readerEmpty() throws Exception {
            final String expected = "abcde";
            final String selected = new SimpleBuilder()
                    .bindDefaultNCharacterStream(":binding", new StringReader(expected))
                    .bindOptionalNCharacterStream(":binding", Optional.empty())
                    .execute(db, rs -> rs.getString(1));
            assertEquals(expected, selected);
        }

        @Test
        public void readerLength() throws Exception {
            final String expected = "abcde";
            final String selected = new SimpleBuilder()
                    .bindDefaultNCharacterStream(":binding", null, (long) expected.length())
                    .bindOptionalNCharacterStream(":binding", Optional.of(new StringReader(expected)), (long) expected.length())
                    .execute(db, rs -> rs.getString(1));
            assertEquals(expected, selected);
        }

        @Test
        public void readerNullLength() throws Exception {
            final String expected = "abcde";
            final String selected = new SimpleBuilder()
                    .bindDefaultNCharacterStream(":binding", new StringReader(expected), (long) expected.length())
                    .bindOptionalNCharacterStream(":binding", Optional.empty(), (long) expected.length())
                    .execute(db, rs -> rs.getString(1));
            assertEquals(expected, selected);
        }
    }

    public static class BindNClob {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void reader() throws Exception {
            final String expected = "abcde";
            final String selected = new SimpleBuilder()
                    .bindDefaultNClob(":binding", (Reader) null)
                    .bindOptionalNClob(":binding", Optional.of(new StringReader(expected)))
                    .execute(db, rs -> rs.getString(1));
            assertEquals(expected, selected);
        }

        @Test
        public void readerEmpty() throws Exception {
            final String expected = "abcde";
            final String selected = new SimpleBuilder()
                    .bindDefaultNClob(":binding", new StringReader(expected))
                    .bindOptionalNClob(":binding", Optional.empty())
                    .execute(db, rs -> rs.getString(1));
            assertEquals(expected, selected);
        }

        @Test
        public void readerLength() throws Exception {
            final String expected = "abcde";
            final String selected = new SimpleBuilder()
                    .bindDefaultNClob(":binding", null, (long) expected.length())
                    .bindOptionalNClob(":binding", Optional.of(new StringReader(expected)), (long) expected.length())
                    .execute(db, rs -> rs.getString(1));
            assertEquals(expected, selected);
        }

        @Test
        public void readerNullLength() throws Exception {
            final String expected = "abcde";
            final String selected = new SimpleBuilder()
                    .bindDefaultNClob(":binding", new StringReader(expected), (long) expected.length())
                    .bindOptionalNClob(":binding", Optional.empty(), (long) expected.length())
                    .execute(db, rs -> rs.getString(1));
            assertEquals(expected, selected);
        }
    }

    public static class BindNString {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void value() throws Exception {
            final String expected = "abcde";
            final String selected = new SimpleBuilder()
                    .bindDefaultNString(":binding", null)
                    .bindOptionalNString(":binding", Optional.of(expected))
                    .execute(db, rs -> rs.getNString(1));
            assertEquals(expected, selected);
        }

        @Test
        public void valueEmpty() throws Exception {
            final String expected = "abcde";
            final String selected = new SimpleBuilder()
                    .bindDefaultNString(":binding", expected)
                    .bindOptionalNString(":binding", Optional.empty())
                    .execute(db, rs -> rs.getString(1));
            assertEquals(expected, selected);
        }
    }

    public static class BindObject {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void value() throws Exception {
            final String expected = "abcde";
            final String selected = new SimpleBuilder()
                    .bindDefaultObject(":binding", null)
                    .bindOptionalObject(":binding", Optional.of(expected))
                    .execute(db, rs -> rs.getObject(1).toString());
            assertEquals(expected, selected);
        }

        @Test
        public void valueEmpty() throws Exception {
            final String expected = "abcde";
            final String selected = new SimpleBuilder()
                    .bindDefaultObject(":binding", expected)
                    .bindOptionalObject(":binding", Optional.empty())
                    .execute(db, rs -> rs.getString(1));
            assertEquals(expected, selected);
        }

        @Test
        public void valueType() throws Exception {
            final String expected = "abcde";
            final String selected = new SimpleBuilder()
                    .bindDefaultObject(":binding", null, Types.VARCHAR)
                    .bindOptionalObject(":binding", Optional.of(expected), Types.VARCHAR)
                    .execute(db, rs -> rs.getObject(1).toString());
            assertEquals(expected, selected);
        }

        @Test
        public void valueNullType() throws Exception {
            final String expected = "abcde";
            final String selected = new SimpleBuilder()
                    .bindDefaultObject(":binding", expected, Types.VARCHAR)
                    .bindOptionalObject(":binding", Optional.empty(), Types.VARCHAR)
                    .execute(db, rs -> rs.getString(1));
            assertEquals(expected, selected);
        }

        @Test
        public void valueTypeLength() throws Exception {
            final String expected = "abcde";
            final String selected = new SimpleBuilder()
                    .bindDefaultObject(":binding", null, Types.VARCHAR, expected.length())
                    .bindOptionalObject(":binding", Optional.of(expected), Types.VARCHAR, expected.length())
                    .execute(db, rs -> rs.getObject(1).toString());
            assertEquals(expected, selected);
        }

        @Test
        public void valueNullTypeLength() throws Exception {
            final String expected = "abcde";
            final String selected = new SimpleBuilder()
                    .bindDefaultObject(":binding", expected, Types.VARCHAR, expected.length())
                    .bindOptionalObject(":binding", Optional.empty(), Types.VARCHAR, expected.length())
                    .execute(db, rs -> rs.getString(1));
            assertEquals(expected, selected);
        }

        @Test(expected = SQLFeatureNotSupportedException.class)
        public void valueSQLType() throws Exception {
            final String expected = "abcde";
            final String selected = new SimpleBuilder()
                    .bindDefaultObject(":binding", null, JDBCType.VARCHAR)
                    .bindOptionalObject(":binding", Optional.of(expected), JDBCType.VARCHAR)
                    .execute(db, rs -> rs.getObject(1).toString());
            assertEquals(expected, selected);
        }

        @Test(expected = SQLFeatureNotSupportedException.class)
        public void valueNullSQLType() throws Exception {
            final String expected = "abcde";
            final String selected = new SimpleBuilder()
                    .bindDefaultObject(":binding", expected, JDBCType.VARCHAR)
                    .bindOptionalObject(":binding", Optional.empty(), JDBCType.VARCHAR)
                    .execute(db, rs -> rs.getString(1));
            assertEquals(expected, selected);
        }

        @Test(expected = SQLFeatureNotSupportedException.class)
        public void valueSQLTypeLength() throws Exception {
            final String expected = "abcde";
            final String selected = new SimpleBuilder()
                    .bindDefaultObject(":binding", null, JDBCType.VARCHAR, expected.length())
                    .bindOptionalObject(":binding", Optional.of(expected), JDBCType.VARCHAR, expected.length())
                    .execute(db, rs -> rs.getObject(1).toString());
            assertEquals(expected, selected);
        }

        @Test(expected = SQLFeatureNotSupportedException.class)
        public void valueNullSQLTypeLength() throws Exception {
            final String expected = "abcde";
            final String selected = new SimpleBuilder()
                    .bindDefaultObject(":binding", expected, JDBCType.VARCHAR, expected.length())
                    .bindOptionalObject(":binding", Optional.empty(), JDBCType.VARCHAR, expected.length())
                    .execute(db, rs -> rs.getString(1));
            assertEquals(expected, selected);
        }
    }


    public static class BindShort {

        @ClassRule
        public static H2Rule db = new H2Rule();

        @Test
        public void value() throws Exception {
            final Short expected = 12;
            final Short selected = new SimpleBuilder()
                    .bindDefaultShort(":binding", Short.MIN_VALUE)
                    .bindOptionalShort(":binding", Optional.of(expected))
                    .execute(db, rs -> rs.getShort(1));
            assertEquals(expected, selected);
        }

        @Test
        public void notPresent() throws Exception {
            final Short expected = 12;
            final Short selected = new SimpleBuilder()
                    .bindDefaultShort(":binding", expected)
                    .bindOptionalShort(":binding", Optional.empty())
                    .execute(db, rs -> rs.getShort(1));
            assertEquals(expected, selected);
        }

        @Test
        public void primitiveValue() throws Exception {
            final short expected = 12;
            final short selected = new SimpleBuilder()
                    .bindDefaultShortPrimitive(":binding", Short.MIN_VALUE)
                    .bindOptionalShort(":binding", OptionalShort.of(expected))
                    .execute(db, rs -> rs.getShortPrimitive(1));
            assertEquals(expected, selected);
        }

        @Test
        public void primitiveNotPresent() throws Exception {
            final short expected = 12;
            final short selected = new SimpleBuilder()
                    .bindDefaultShortPrimitive(":binding", expected)
                    .bindOptionalShort(":binding", OptionalShort.empty())
                    .execute(db, rs -> rs.getShortPrimitive(1));
            assertEquals(expected, selected);
        }
    }

    public static class BindString {

        @ClassRule
        public static H2Rule db = new H2Rule();

        @Test
        public void value() throws Exception {
            final String expected = "abcde";
            final String selected = new SimpleBuilder()
                    .bindDefaultString(":binding", null)
                    .bindOptionalString(":binding", Optional.of(expected))
                    .execute(db, rs -> rs.getString(1));
            assertEquals(expected, selected);
        }

        @Test
        public void valueEmpty() throws Exception {
            final String expected = "abcde";
            final String selected = new SimpleBuilder()
                    .bindDefaultString(":binding", expected)
                    .bindOptionalString(":binding", Optional.empty())
                    .execute(db, rs -> rs.getString(1));
            assertEquals(expected, selected);
        }
    }

    public static class BindSQLXML {

        @ClassRule
        public static PGRule db = new PGRule();

        @Test
        public void value() throws Exception {
            final String expected = "<a></a>";
            try (Connection connection = db.getConnection()) {
                final SQLXML xml = connection.createSQLXML();
                xml.setString(expected);
                final String selected = new SimpleBuilder()
                        .bindDefaultSQLXML(":binding", null)
                        .bindOptionalSQLXML(":binding", Optional.of(xml))
                        .execute(connection, rs -> rs.getSQLXML(1).getString());
                assertEquals(expected, selected);
            }
        }

        @Test
        public void valueEmpty() throws Exception {
            final String expected = "<a></a>";
            try (Connection connection = db.getConnection()) {
                final SQLXML xml = connection.createSQLXML();
                xml.setString(expected);

                final String selected = new SimpleBuilder()
                        .bindDefaultSQLXML(":binding", xml)
                        .bindOptionalSQLXML(":binding", Optional.empty())
                        .execute(connection, rs -> rs.getSQLXML(1).getString());
                assertEquals(expected, selected);
            }
        }
    }

    public static class BindTime extends HasExpectedTimeOfDay {

        @ClassRule
        public static H2Rule db = new H2Rule();

        @Test
        public void value() throws Exception {
            final Time expected = new Time(expectedMillis);
            final Time selected = new SimpleBuilder()
                    .bindDefaultTime(":binding", null)
                    .bindOptionalTime(":binding", Optional.of(expected))
                    .execute(db, rs -> rs.getTime(1));
            assertEquals(expected, selected);
        }

        @Test
        public void valueEmpty() throws Exception {
            final Time expected = new Time(expectedMillis);
            final Time selected = new SimpleBuilder()
                    .bindDefaultTime(":binding", expected)
                    .bindOptionalDate(":binding", Optional.empty())
                    .execute(db, rs -> rs.getTime(1));
            assertEquals(expected, selected);
        }

        @Test
        public void valueCalendar() throws Exception {
            final Time expected = new Time(expectedMillis);
            final Time selected = new SimpleBuilder()
                    .bindDefaultTime(":binding", null, GregorianCalendar.getInstance())
                    .bindOptionalTime(":binding", Optional.of(expected), GregorianCalendar.getInstance())
                    .execute(db, rs -> rs.getTime(1, GregorianCalendar.getInstance()));
            assertEquals(expected, selected);
        }

        @Test
        public void valueCalendarEmpty() throws Exception {
            final Time expected = new Time(expectedMillis);
            final Time selected = new SimpleBuilder()
                    .bindDefaultTime(":binding", expected, GregorianCalendar.getInstance())
                    .bindOptionalDate(":binding", Optional.empty(), GregorianCalendar.getInstance())
                    .execute(db, rs -> rs.getTime(1, GregorianCalendar.getInstance()));
            assertEquals(expected, selected);
        }

    }

    public static class BindTimestamp extends HasExpectedTimeSinceEpoch {

        @ClassRule
        public static H2Rule db = new H2Rule();

        @Test
        public void value() throws Exception {
            final Timestamp expected = new Timestamp(expectedTime);
            final Timestamp selected = new SimpleBuilder()
                    .bindDefaultTimestamp(":binding", null)
                    .bindOptionalTimestamp(":binding", Optional.of(expected))
                    .execute(db, rs -> rs.getTimestamp(1));
            assertEquals(expected, selected);
        }

        @Test
        public void valueEmpty() throws Exception {
            final Timestamp expected = new Timestamp(expectedTime);
            final Timestamp selected = new SimpleBuilder()
                    .bindDefaultTimestamp(":binding", expected)
                    .bindOptionalTimestamp(":binding", Optional.empty())
                    .execute(db, rs -> rs.getTimestamp(1));
            assertEquals(expected, selected);
        }

        @Test
        public void valueCalendar() throws Exception {
            final Timestamp expected = new Timestamp(expectedTime);
            final Timestamp selected = new SimpleBuilder()
                    .bindDefaultTimestamp(":binding", null, GregorianCalendar.getInstance())
                    .bindOptionalTimestamp(":binding", Optional.of(expected), GregorianCalendar.getInstance())
                    .execute(db, rs -> rs.getTimestamp(1, GregorianCalendar.getInstance()));
            assertEquals(expected, selected);
        }

        @Test
        public void valueCalendarEmpty() throws Exception {
            final Timestamp expected = new Timestamp(expectedTime);
            final Timestamp selected = new SimpleBuilder()
                    .bindDefaultTimestamp(":binding", expected, GregorianCalendar.getInstance())
                    .bindOptionalTimestamp(":binding", Optional.empty(), GregorianCalendar.getInstance())
                    .execute(db, rs -> rs.getTimestamp(1, GregorianCalendar.getInstance()));
            assertEquals(expected, selected);
        }

    }

    public static class BindUrl {

        @ClassRule
        public static PGRule db = new PGRule();

        @Test(expected = SQLFeatureNotSupportedException.class)
        public void value() throws Exception {
            final URL expected = new URL("http", "google.com", 8080, "/");
            final URL selected = new SimpleBuilder()
                    .bindDefaultURL(":binding", null)
                    .bindOptionalURL(":binding", Optional.of(expected))
                    .execute(db, rs -> rs.getURL(1));
            assertEquals(expected, selected);
        }

        @Test(expected = SQLFeatureNotSupportedException.class)
        public void valueEmpty() throws Exception {
            final URL expected = new URL("http", "google.com", 8080, "/");
            final URL selected = new SimpleBuilder()
                    .bindDefaultURL(":binding", expected)
                    .bindOptionalURL(":binding", Optional.empty())
                    .execute(db, rs -> rs.getURL(1));
            assertEquals(expected, selected);
        }
    }
}