package com.github.randyp.jdbj;

import com.github.randyp.jdbj.lambda.ResultSetMapper;
import com.github.randyp.jdbj.optional.OptionalBoolean;
import com.github.randyp.jdbj.optional.OptionalByte;
import com.github.randyp.jdbj.optional.OptionalFloat;
import com.github.randyp.jdbj.optional.OptionalShort;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.sql.*;
import java.sql.Date;
import java.util.*;

import static org.junit.Assert.*;

@RunWith(Enclosed.class)
public class OptionalValueBindingsBuilderTest {

    @ClassRule
    public static final H2Rule db = new H2Rule();

    @Ignore //not supported in h2
    public static class BindArray { //lower case so we don't capture java.sql.Array class

        @Test
        public void bind() throws Exception {
            final String[] expected = {"a", "b", "c"};
            try (Connection connection = db.getConnection()) {
                final String[] selected = new TestBuilder()
                        .bindArray(":binding", null)
                        .bindOptionalArray(":binding", Optional.of(connection.createArrayOf("varchar", expected)))
                        .execute(connection, rs -> (String[]) rs.getSQLArray(1).getArray());
                assertArrayEquals(expected, selected);
            }
        }

        @Test
        public void bindEmpty() throws Exception {
            final String[] expected = {"a", "b", "c"};
            try (Connection connection = db.getConnection()) {
                final String[] selected = new TestBuilder()
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
        @Test
        public void inputStream() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new TestBuilder()
                        .bindDefaultAsciiStream(":binding", null)
                        .bindOptionalAsciiStream(":binding", Optional.of(new ByteArrayInputStream(expected.getBytes(Charset.forName("ascii")))))
                        .execute(connection, rs -> rs.getString(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void inputStreamEmpty() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new TestBuilder()
                        .bindDefaultAsciiStream(":binding", new ByteArrayInputStream(expected.getBytes(Charset.forName("ascii"))))
                        .bindOptionalAsciiStream(":binding", Optional.empty())
                        .execute(connection, rs -> rs.getString(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void inputStreamLength() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new TestBuilder()
                        .bindDefaultAsciiStream(":binding", null, expected.length())
                        .bindOptionalAsciiStream(":binding", Optional.of(new ByteArrayInputStream(expected.getBytes(Charset.forName("ascii")))), expected.length())
                        .execute(connection, rs -> rs.getString(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void inputStreamLengthEmpty() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new TestBuilder()
                        .bindDefaultAsciiStream(":binding", new ByteArrayInputStream(expected.getBytes(Charset.forName("ascii"))), expected.length())
                        .bindOptionalAsciiStream(":binding", Optional.empty(), expected.length())
                        .execute(connection, rs -> rs.getString(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void inputStreamLengthLong() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new TestBuilder()
                        .bindDefaultAsciiStream(":binding", null, (long) expected.length())
                        .bindOptionalAsciiStream(":binding", Optional.of(new ByteArrayInputStream(expected.getBytes(Charset.forName("ascii")))), (long) expected.length())
                        .execute(connection, rs -> rs.getString(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void inputStreamLengthLongEmpty() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new TestBuilder()
                        .bindDefaultAsciiStream(":binding", new ByteArrayInputStream(expected.getBytes(Charset.forName("ascii"))), (long) expected.length())
                        .bindOptionalAsciiStream(":binding", Optional.empty(), (long) expected.length())
                        .execute(connection, rs -> rs.getString(1));
                assertEquals(expected, selected);
            }
        }
    }

    public static class BindBigDecimal {
        @Test
        public void value() throws Exception {
            final BigDecimal expected = new BigDecimal("1.234");
            try (Connection connection = db.getConnection()) {
                final BigDecimal selected = new TestBuilder()
                        .bindDefaultBigDecimal(":binding", null)
                        .bindOptionalBigDecimal(":binding", Optional.of(expected))
                        .execute(connection, rs -> rs.getBigDecimal(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void nullValue() throws Exception {
            final BigDecimal expected = new BigDecimal("1.234");
            try (Connection connection = db.getConnection()) {
                final BigDecimal selected = new TestBuilder()
                        .bindDefaultBigDecimal(":binding", expected)
                        .bindOptionalBigDecimal(":binding", Optional.empty())
                        .execute(connection, rs -> rs.getBigDecimal(1));
                assertEquals(expected, selected);
            }
        }
    }

    public static class BindBinaryStream {

        @Test
        public void inputStream() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new TestBuilder()
                        .bindDefaultBinaryStream(":binding", null)
                        .bindOptionalBinaryStream(":binding", Optional.of(new ByteArrayInputStream(expected.getBytes())))
                        .execute(connection, rs -> new String(rs.getBytes(1)));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void inputStreamEmpty() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new TestBuilder()
                        .bindDefaultBinaryStream(":binding", new ByteArrayInputStream(expected.getBytes()))
                        .bindOptionalBinaryStream(":binding", Optional.empty())
                        .execute(connection, rs -> {
                            final byte[] bytes = rs.getBytes(1);
                            return bytes == null ? null : new String(bytes);
                        });
                assertEquals(expected, selected);
            }
        }

        @Test
        public void inputLengthStream() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new TestBuilder()
                        .bindDefaultBinaryStream(":binding", null, expected.length())
                        .bindOptionalBinaryStream(":binding", Optional.of(new ByteArrayInputStream(expected.getBytes())), expected.length())
                        .execute(connection, rs -> new String(rs.getBytes(1)));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void inputLengthStreamEmpty() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new TestBuilder()
                        .bindDefaultBinaryStream(":binding", new ByteArrayInputStream(expected.getBytes()), expected.length())
                        .bindOptionalBinaryStream(":binding", Optional.empty(), expected.length())
                        .execute(connection, rs -> {
                            final byte[] bytes = rs.getBytes(1);
                            return bytes == null ? null : new String(bytes);
                        });
                assertEquals(expected, selected);
            }
        }

        @Test
        public void inputLengthLongStream() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new TestBuilder()
                        .bindDefaultBinaryStream(":binding", null, (long) expected.length())
                        .bindOptionalBinaryStream(":binding", Optional.of(new ByteArrayInputStream(expected.getBytes())), (long) expected.length())
                        .execute(connection, rs -> new String(rs.getBytes(1)));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void inputLengthLongStreamEmpty() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new TestBuilder()
                        .bindDefaultBinaryStream(":binding", new ByteArrayInputStream(expected.getBytes()), (long) expected.length())
                        .bindOptionalBinaryStream(":binding", Optional.empty(), (long) expected.length())
                        .execute(connection, rs -> {
                            final byte[] bytes = rs.getBytes(1);
                            return bytes == null ? null : new String(bytes);
                        });
                assertEquals(expected, selected);
            }
        }
    }

    public static class BindBlob {
        @Test
        public void inputStream() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new TestBuilder()
                        .bindDefaultBlob(":binding", (InputStream) null)
                        .bindOptionalBlob(":binding", Optional.of(new ByteArrayInputStream(expected.getBytes())))
                        .execute(connection, rs -> new String(rs.getBytes(1)));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void inputStreamEmpty() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new TestBuilder()
                        .bindDefaultBlob(":binding", new ByteArrayInputStream(expected.getBytes()))
                        .bindOptionalBlob(":binding", Optional.empty())
                        .execute(connection, rs -> {
                            final byte[] bytes = rs.getBytes(1);
                            return bytes == null ? null : new String(bytes);
                        });
                assertEquals(expected, selected);
            }
        }

        @Test
        public void inputStreamLength() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new TestBuilder()
                        .bindDefaultBlob(":binding", (InputStream) null)
                        .bindOptionalBlob(":binding", Optional.of(new ByteArrayInputStream(expected.getBytes())), (long) expected.length())
                        .execute(connection, rs -> new String(rs.getBytes(1)));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void inputStreamNullLength() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new TestBuilder()
                        .bindDefaultBlob(":binding", new ByteArrayInputStream(expected.getBytes()), (long) expected.length())
                        .bindOptionalBlob(":binding", Optional.empty(), (long) expected.length())
                        .execute(connection, rs -> {
                            final byte[] bytes = rs.getBytes(1);
                            return bytes == null ? null : new String(bytes);
                        });
                assertEquals(expected, selected);
            }
        }
    }

    public static class BindBoolean {

        @Test
        public void False() throws Exception {
            try (Connection connection = db.getConnection()) {
                final Boolean selected = new TestBuilder()
                        .bindDefaultBoolean(":binding", true)
                        .bindOptionalBoolean(":binding", Optional.of(false))
                        .execute(connection, rs -> rs.getBoolean(1));
                assertFalse(selected);
            }
        }

        @Test
        public void True() throws Exception {
            try (Connection connection = db.getConnection()) {
                final Boolean selected = new TestBuilder()
                        .bindDefaultBoolean(":binding", false)
                        .bindOptionalBoolean(":binding", Optional.of(true))
                        .execute(connection, rs -> rs.getBoolean(1));
                assertTrue(selected);
            }
        }

        @Test
        public void notPresent() throws Exception {
            try (Connection connection = db.getConnection()) {
                final Boolean selected = new TestBuilder()
                        .bindDefaultBoolean(":binding", true)
                        .bindOptionalBoolean(":binding", Optional.empty())
                        .execute(connection, rs -> rs.getBoolean(1));
                assertTrue(selected);
            }
        }
    }

    public static class BindBooleanPrimitive {

        @Test
        public void False() throws Exception {
            try (Connection connection = db.getConnection()) {
                final boolean selected = new TestBuilder()
                        .bindDefaultBooleanPrimitive(":binding", true)
                        .bindOptionalBoolean(":binding", OptionalBoolean.of(false))
                        .execute(connection, rs -> rs.getBooleanPrimitive(1));
                assertFalse(selected);
            }
        }

        @Test
        public void True() throws Exception {
            try (Connection connection = db.getConnection()) {
                final boolean selected = new TestBuilder()
                        .bindDefaultBooleanPrimitive(":binding", false)
                        .bindOptionalBoolean(":binding", OptionalBoolean.of(true))
                        .execute(connection, rs -> rs.getBooleanPrimitive(1));
                assertTrue(selected);
            }
        }

        @Test
        public void notPresent() throws Exception {
            try (Connection connection = db.getConnection()) {
                final boolean selected = new TestBuilder()
                        .bindDefaultBooleanPrimitive(":binding", true)
                        .bindOptionalBoolean(":binding", OptionalBoolean.empty())
                        .execute(connection, rs -> rs.getBooleanPrimitive(1));
                assertTrue(selected);
            }
        }
    }

    public static class BindByte {

        @Test
        public void present() throws Exception {
            final Byte expected = 6;
            try (Connection connection = db.getConnection()) {
                final Byte selected = new TestBuilder()
                        .bindDefaultByte(":binding", (byte) 7)
                        .bindOptionalByte(":binding", Optional.of(expected))
                        .execute(connection, rs -> rs.getByte(1));
                assertEquals(expected, selected, 0.0);
            }
        }

        @Test
        public void notPresent() throws Exception {
            final Byte expected = 6;
            try (Connection connection = db.getConnection()) {
                final Byte selected = new TestBuilder()
                        .bindDefaultByte(":binding", expected)
                        .bindOptionalByte(":binding", Optional.empty())
                        .execute(connection, rs -> rs.getByte(1));
                assertEquals(expected, selected, 0.0);
            }
        }
    }

    public static class BindBytePrimitive {

        @Test
        public void present() throws Exception {
            final byte expected = 6;
            try (Connection connection = db.getConnection()) {
                final byte selected = new TestBuilder()
                        .bindDefaultBytePrimtive(":binding", (byte) 7)
                        .bindOptionalByte(":binding", OptionalByte.of(expected))
                        .execute(connection, rs -> rs.getBytePrimitive(1));
                assertEquals(expected, selected, 0.0);
            }
        }

        @Test
        public void notPresent() throws Exception {
            final byte expected = 6;
            try (Connection connection = db.getConnection()) {
                final byte selected = new TestBuilder()
                        .bindDefaultBytePrimtive(":binding", expected)
                        .bindOptionalByte(":binding", OptionalByte.empty())
                        .execute(connection, rs -> rs.getBytePrimitive(1));
                assertEquals(expected, selected, 0.0);
            }
        }
    }

    public static class BindBytes {

        @Test
        public void value() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new TestBuilder()
                        .bindDefaultByteArray(":binding", null)
                        .bindOptionalByteArray(":binding", Optional.of(expected.getBytes()))
                        .execute(connection, rs -> new String(rs.getBytes(1)));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void valueEmpty() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new TestBuilder()
                        .bindDefaultByteArray(":binding", expected.getBytes())
                        .bindOptionalBinaryStream(":binding", Optional.empty())
                        .execute(connection, rs -> {
                            final byte[] bytes = rs.getBytes(1);
                            return bytes == null ? null : new String(bytes);
                        });
                assertEquals(expected, selected);
            }
        }
    }

    public static class BindCharacterStream {
        @Test
        public void reader() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new TestBuilder()
                        .bindDefaultCharacterStream(":binding", null)
                        .bindOptionalCharacterStream(":binding", Optional.of(new StringReader(expected)))
                        .execute(connection, rs -> rs.getString(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void readerEmpty() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new TestBuilder()
                        .bindDefaultCharacterStream(":binding", new StringReader(expected))
                        .bindOptionalCharacterStream(":binding", Optional.empty())
                        .execute(connection, rs -> rs.getString(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void readerLength() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultCharacterStream(":binding", null, expected.length())
                        .bindOptionalCharacterStream(":binding", Optional.of(new StringReader(expected)), expected.length())
                        .execute(connection, rs -> rs.getString(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void readerLengthEmpty() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new TestBuilder()
                        .bindDefaultCharacterStream(":binding", new StringReader(expected), expected.length())
                        .bindOptionalCharacterStream(":binding", Optional.empty(), expected.length())
                        .execute(connection, rs -> rs.getString(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void readerLengthLong() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new TestBuilder()
                        .bindDefaultCharacterStream(":binding", null, (long) expected.length())
                        .bindOptionalCharacterStream(":binding", Optional.of(new StringReader(expected)), (long) expected.length())
                        .execute(connection, rs -> rs.getString(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void readerLengthLongEmpty() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new TestBuilder()
                        .bindDefaultCharacterStream(":binding", new StringReader(expected), (long) expected.length())
                        .bindOptionalCharacterStream(":binding", Optional.empty(), (long) expected.length())
                        .execute(connection, rs -> rs.getString(1));
                assertEquals(expected, selected);
            }
        }
    }

    public static class BindClob {

        @Test
        public void reader() throws Exception {
            final String expected = "abcd";
            try (Connection connection = db.getConnection()) {
                final String selected = new TestBuilder()
                        .bindDefaultClob(":binding", (Reader) null)
                        .bindOptionalClob(":binding", Optional.of(new StringReader(expected)))
                        .execute(connection, rs -> rs.getClob(1).getSubString(1L, expected.length()));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void readerEmpty() throws Exception {
            final String expected = "abcd";
            try (Connection connection = db.getConnection()) {
                final String selected = new TestBuilder()
                        .bindDefaultClob(":binding", new StringReader(expected))
                        .bindOptionalClob(":binding", Optional.empty())
                        .execute(connection, rs -> {
                            final Clob clob = rs.getClob(1);
                            return clob == null ? null : clob.getSubString(1L, expected.length());
                        });
                assertEquals(expected, selected);
            }
        }

        @Test
        public void readerLength() throws Exception {
            final String expected = "abcd";
            try (Connection connection = db.getConnection()) {
                final String selected = new TestBuilder()
                        .bindDefaultClob(":binding", (Reader) null)
                        .bindOptionalClob(":binding", Optional.of(new StringReader(expected)), (long) expected.length())
                        .execute(connection, rs -> rs.getClob(1).getSubString(1L, expected.length()));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void readerLengthEmpty() throws Exception {
            final String expected = "abcd";
            try (Connection connection = db.getConnection()) {
                final String selected = new TestBuilder()
                        .bindDefaultClob(":binding", new StringReader(expected), (long) expected.length())
                        .bindOptionalClob(":binding", Optional.empty(), (long) expected.length())
                        .execute(connection, rs -> {
                            final Clob clob = rs.getClob(1);
                            return clob == null ? null : clob.getSubString(1L, expected.length());
                        });
                assertEquals(expected, selected);
            }
        }
    }

    public static class BindDate extends HasExpectedTimeSinceEpoch {

        @Test
        public void value() throws Exception {
            final Date expected = new Date(expectedTime);
            try (Connection connection = db.getConnection()) {
                final Date selected = new TestBuilder()
                        .bindDefaultDate(":binding", null)
                        .bindOptionalDate(":binding", Optional.of(expected))
                        .execute(connection, rs -> rs.getDate(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void valueEmpty() throws Exception {
            final Date expected = new Date(expectedTime);
            try (Connection connection = db.getConnection()) {
                final Date selected = new TestBuilder()
                        .bindDefaultDate(":binding", expected)
                        .bindOptionalDate(":binding", Optional.empty())
                        .execute(connection, rs -> rs.getDate(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void valueCalendar() throws Exception {
            final Date expected = new Date(expectedTime);
            try (Connection connection = db.getConnection()) {
                final Date selected = new TestBuilder()
                        .bindDefaultDate(":binding", null, GregorianCalendar.getInstance())
                        .bindOptionalDate(":binding", Optional.of(expected), GregorianCalendar.getInstance())
                        .execute(connection, rs -> rs.getDate(1, GregorianCalendar.getInstance()));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void valueCalendarEmpty() throws Exception {
            final Date expected = new Date(expectedTime);
            try (Connection connection = db.getConnection()) {
                final Date selected = new TestBuilder()
                        .bindDefaultDate(":binding", expected, GregorianCalendar.getInstance())
                        .bindOptionalDate(":binding", Optional.empty(), GregorianCalendar.getInstance())
                        .execute(connection, rs -> rs.getDate(1, GregorianCalendar.getInstance()));
                assertEquals(expected, selected);
            }
        }

    }

    public static class BindDouble {
        @Test
        public void value() throws Exception {
            final Double expected = 1.2;
            try (Connection connection = db.getConnection()) {
                final Double selected = new TestBuilder()
                        .bindDefaultDouble(":binding", Double.MIN_VALUE)
                        .bindOptionalDouble(":binding", Optional.of(expected))
                        .execute(connection, rs -> rs.getDouble(1));
                assertEquals(expected, selected, 0.0);
            }
        }

        @Test
        public void notPresent() throws Exception {
            final Double expected = 1.2;
            try (Connection connection = db.getConnection()) {
                final Double selected = new TestBuilder()
                        .bindDefaultDouble(":binding", expected)
                        .bindOptionalDouble(":binding", Optional.empty())
                        .execute(connection, rs -> rs.getDouble(1));
                assertEquals(expected, selected, 0.0);
            }
        }
    }

    public static class BindDoublePrimitive {
        @Test
        public void value() throws Exception {
            final double expected = 1.2;
            try (Connection connection = db.getConnection()) {
                final double selected = new TestBuilder()
                        .bindDefaultDoublePrimitive(":binding", Double.MIN_VALUE)
                        .bindOptionalDouble(":binding", OptionalDouble.of(expected))
                        .execute(connection, rs -> rs.getDoublePrimitive(1));
                assertEquals(expected, selected, 0.0);
            }
        }

        @Test
        public void notPresent() throws Exception {
            final double expected = 1.2;
            try (Connection connection = db.getConnection()) {
                final double selected = new TestBuilder()
                        .bindDefaultDoublePrimitive(":binding", expected)
                        .bindOptionalDouble(":binding", OptionalDouble.empty())
                        .execute(connection, rs -> rs.getDoublePrimitive(1));
                assertEquals(expected, selected, 0.0);
            }
        }
    }

    public static class BindFloat {
        @Test
        public void value() throws Exception {
            final Float expected = 1.2f;
            try (Connection connection = db.getConnection()) {
                final Float selected = new TestBuilder()
                        .bindDefaultFloat(":binding", Float.MIN_VALUE)
                        .bindOptionalFloat(":binding", Optional.of(expected))
                        .execute(connection, rs -> rs.getFloat(1));
                assertEquals(expected, selected, 0.0f);
            }
        }

        @Test
        public void notPresent() throws Exception {
            final Float expected = 1.2f;
            try (Connection connection = db.getConnection()) {
                final Float selected = new TestBuilder()
                        .bindDefaultFloat(":binding", expected)
                        .bindOptionalFloat(":binding", Optional.empty())
                        .execute(connection, rs -> rs.getFloat(1));
                assertEquals(expected, selected, 0.0f);
            }
        }
    }

    public static class BindFloatPrimitive {
        @Test
        public void value() throws Exception {
            final float expected = 1.2f;
            try (Connection connection = db.getConnection()) {
                final float selected = new TestBuilder()
                        .bindDefaultFloatPrimtitive(":binding", Float.MIN_VALUE)
                        .bindOptionalFloat(":binding", OptionalFloat.of(expected))
                        .execute(connection, rs -> rs.getFloatPrimitive(1));
                assertEquals(expected, selected, 0.0f);
            }
        }

        @Test
        public void notPresent() throws Exception {
            final float expected = 1.2f;
            try (Connection connection = db.getConnection()) {
                final float selected = new TestBuilder()
                        .bindDefaultFloatPrimtitive(":binding", expected)
                        .bindOptionalFloat(":binding", OptionalFloat.empty())
                        .execute(connection, rs -> rs.getFloatPrimitive(1));
                assertEquals(expected, selected, 0.0f);
            }
        }
    }

    public static class BindInteger {
        @Test
        public void value() throws Exception {
            final Integer expected = 12;
            try (Connection connection = db.getConnection()) {
                final Integer selected = new TestBuilder()
                        .bindDefaultInteger(":binding", Integer.MIN_VALUE)
                        .bindOptionalInteger(":binding", Optional.of(expected))
                        .execute(connection, rs -> rs.getInteger(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void notPresent() throws Exception {
            final Integer expected = 12;
            try (Connection connection = db.getConnection()) {
                final Integer selected = new TestBuilder()
                        .bindDefaultInteger(":binding", expected)
                        .bindOptionalInteger(":binding", Optional.empty())
                        .execute(connection, rs -> rs.getInteger(1));
                assertEquals(expected, selected);
            }
        }
    }

    public static class BindIntegerPrimitive {
        @Test
        public void value() throws Exception {
            final int expected = 12;
            try (Connection connection = db.getConnection()) {
                final int selected = new TestBuilder()
                        .bindDefaultIntegerPrimitive(":binding", Integer.MIN_VALUE)
                        .bindOptionalInteger(":binding", OptionalInt.of(expected))
                        .execute(connection, rs -> rs.getIntegerPrimitive(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void notPresent() throws Exception {
            final int expected = 12;
            try (Connection connection = db.getConnection()) {
                final int selected = new TestBuilder()
                        .bindDefaultIntegerPrimitive(":binding", expected)
                        .bindOptionalInteger(":binding", OptionalInt.empty())
                        .execute(connection, rs -> rs.getIntegerPrimitive(1));
                assertEquals(expected, selected);
            }
        }
    }

    public static class BindInt {
        @Test
        public void value() throws Exception {
            final Integer expected = 12;
            try (Connection connection = db.getConnection()) {
                final Integer selected = new TestBuilder()
                        .bindDefaultInt(":binding", Integer.MIN_VALUE)
                        .bindOptionalInt(":binding", Optional.of(expected))
                        .execute(connection, rs -> rs.getInteger(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void notPresent() throws Exception {
            final Integer expected = 12;
            try (Connection connection = db.getConnection()) {
                final Integer selected = new TestBuilder()
                        .bindDefaultInt(":binding", expected)
                        .bindOptionalInt(":binding", Optional.empty())
                        .execute(connection, rs -> rs.getInteger(1));
                assertEquals(expected, selected);
            }
        }
    }

    public static class BindIntPrimitive {
        @Test
        public void value() throws Exception {
            final int expected = 12;
            try (Connection connection = db.getConnection()) {
                final int selected = new TestBuilder()
                        .bindDefaultInt(":binding", Integer.MIN_VALUE)
                        .bindOptionalInt(":binding", OptionalInt.of(expected))
                        .execute(connection, rs -> rs.getInt(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void notPresent() throws Exception {
            final int expected = 12;
            try (Connection connection = db.getConnection()) {
                final int selected = new TestBuilder()
                        .bindDefaultInt(":binding", expected)
                        .bindOptionalInt(":binding", OptionalInt.empty())
                        .execute(connection, rs -> rs.getInt(1));
                assertEquals(expected, selected);
            }
        }
    }

    public static class BindLong {
        @Test
        public void value() throws Exception {
            final Long expected = 12L;
            try (Connection connection = db.getConnection()) {
                final Long selected = new TestBuilder()
                        .bindDefaultLong(":binding", Long.MIN_VALUE)
                        .bindOptionalLong(":binding", Optional.of(expected))
                        .execute(connection, rs -> rs.getLong(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void notPresent() throws Exception {
            final Long expected = 12L;
            try (Connection connection = db.getConnection()) {
                final Long selected = new TestBuilder()
                        .bindDefaultLong(":binding", expected)
                        .bindOptionalLong(":binding", Optional.empty())
                        .execute(connection, rs -> rs.getLong(1));
                assertEquals(expected, selected);
            }
        }
    }

    public static class BindLongPrimitive {
        @Test
        public void value() throws Exception {
            final long expected = 12L;
            try (Connection connection = db.getConnection()) {
                final long selected = new TestBuilder()
                        .bindDefaultLongPrimitive(":binding", Long.MIN_VALUE)
                        .bindOptionalLong(":binding", OptionalLong.of(expected))
                        .execute(connection, rs -> rs.getLongPrimitive(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void notPresent() throws Exception {
            final long expected = 12L;
            try (Connection connection = db.getConnection()) {
                final long selected = new TestBuilder()
                        .bindDefaultLongPrimitive(":binding", expected)
                        .bindOptionalLong(":binding", OptionalLong.empty())
                        .execute(connection, rs -> rs.getLongPrimitive(1));
                assertEquals(expected, selected);
            }
        }
    }

    public static class BindNCharacterStream {

        @Test
        public void reader() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new TestBuilder()
                        .bindDefaultNCharacterStream(":binding", null)
                        .bindOptionalNCharacterStream(":binding", Optional.of(new StringReader(expected)))
                        .execute(connection, rs -> rs.getString(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void readerEmpty() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new TestBuilder()
                        .bindDefaultNCharacterStream(":binding", new StringReader(expected))
                        .bindOptionalNCharacterStream(":binding", Optional.empty())
                        .execute(connection, rs -> rs.getString(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void readerLength() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new TestBuilder()
                        .bindDefaultNCharacterStream(":binding", null, (long) expected.length())
                        .bindOptionalNCharacterStream(":binding", Optional.of(new StringReader(expected)), (long) expected.length())
                        .execute(connection, rs -> rs.getString(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void readerNullLength() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new TestBuilder()
                        .bindDefaultNCharacterStream(":binding", new StringReader(expected), (long) expected.length())
                        .bindOptionalNCharacterStream(":binding", Optional.empty(), (long) expected.length())
                        .execute(connection, rs -> rs.getString(1));
                assertEquals(expected, selected);
            }
        }
    }

    public static class BindNClob {


        @Test
        public void reader() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new TestBuilder()
                        .bindDefaultNClob(":binding", (Reader) null)
                        .bindOptionalNClob(":binding", Optional.of(new StringReader(expected)))
                        .execute(connection, rs -> rs.getString(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void readerEmpty() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new TestBuilder()
                        .bindDefaultNClob(":binding", new StringReader(expected))
                        .bindOptionalNClob(":binding", Optional.empty())
                        .execute(connection, rs -> rs.getString(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void readerLength() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new TestBuilder()
                        .bindDefaultNClob(":binding", null, (long) expected.length())
                        .bindOptionalNClob(":binding", Optional.of(new StringReader(expected)), (long) expected.length())
                        .execute(connection, rs -> rs.getString(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void readerNullLength() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new TestBuilder()
                        .bindDefaultNClob(":binding", new StringReader(expected), (long) expected.length())
                        .bindOptionalNClob(":binding", Optional.empty(), (long) expected.length())
                        .execute(connection, rs -> rs.getString(1));
                assertEquals(expected, selected);
            }
        }
    }

    public static class BindNString {

        @Test
        public void value() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new TestBuilder()
                        .bindDefaultNString(":binding", null)
                        .bindOptionalNString(":binding", Optional.of(expected))
                        .execute(connection, rs -> rs.getNString(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void valueEmpty() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new TestBuilder()
                        .bindDefaultNString(":binding", expected)
                        .bindOptionalNString(":binding", Optional.empty())
                        .execute(connection, rs -> rs.getString(1));
                assertEquals(expected, selected);
            }
        }
    }

    public static class BindObject {

        @Test
        public void value() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new TestBuilder()
                        .bindDefaultObject(":binding", null)
                        .bindOptionalObject(":binding", Optional.of(expected))
                        .execute(connection, rs -> rs.getObject(1).toString());
                assertEquals(expected, selected);
            }
        }

        @Test
        public void valueEmpty() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new TestBuilder()
                        .bindDefaultObject(":binding", expected)
                        .bindOptionalObject(":binding", Optional.empty())
                        .execute(connection, rs -> rs.getString(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void valueType() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new TestBuilder()
                        .bindDefaultObject(":binding", null, Types.VARCHAR)
                        .bindOptionalObject(":binding", Optional.of(expected), Types.VARCHAR)
                        .execute(connection, rs -> rs.getObject(1).toString());
                assertEquals(expected, selected);
            }
        }

        @Test
        public void valueNullType() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new TestBuilder()
                        .bindDefaultObject(":binding", expected, Types.VARCHAR)
                        .bindOptionalObject(":binding", Optional.empty(), Types.VARCHAR)
                        .execute(connection, rs -> rs.getString(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void valueTypeLength() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new TestBuilder()
                        .bindDefaultObject(":binding", null, Types.VARCHAR, expected.length())
                        .bindOptionalObject(":binding", Optional.of(expected), Types.VARCHAR, expected.length())
                        .execute(connection, rs -> rs.getObject(1).toString());
                assertEquals(expected, selected);
            }
        }

        @Test
        public void valueNullTypeLength() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new TestBuilder()
                        .bindDefaultObject(":binding", expected, Types.VARCHAR, expected.length())
                        .bindOptionalObject(":binding", Optional.empty(), Types.VARCHAR, expected.length())
                        .execute(connection, rs -> rs.getString(1));
                assertEquals(expected, selected);
            }
        }

        /*
        @Test
        public void valueSQLType() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new TestBuilder()
                        .bindDefaultObject(":binding", null, JDBCType.VARCHAR)
                        .bindOptionalObject(":binding", Optional.of(expected), JDBCType.VARCHAR)
                        .execute(connection, rs -> rs.getObject(1).toString());
                assertEquals(expected, selected);
            }
        }
        */

        /*
        @Test
        public void valueNullSQLType() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new TestBuilder()
                        .bindDefaultObject(":binding", expected, JDBCType.VARCHAR)
                        .bindOptionalObject(":binding", Optional.empty(), JDBCType.VARCHAR)
                        .execute(connection, rs -> rs.getString(1));
                assertEquals(expected, selected);
            }
        }
        */

        /*
        @Test
        public void valueSQLTypeLength() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new TestBuilder()
                        .bindDefaultObject(":binding", null, JDBCType.VARCHAR, expected.length())
                        .bindOptionalObject(":binding", Optional.of(expected), JDBCType.VARCHAR, expected.length())
                        .execute(connection, rs -> rs.getObject(1).toString());
                assertEquals(expected, selected);
            }
        }
        */

        /*
        @Test
        public void valueNullSQLTypeLength() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new TestBuilder()
                        .bindDefaultObject(":binding", expected, JDBCType.VARCHAR, expected.length())
                        .bindOptionalObject(":binding", Optional.empty(), JDBCType.VARCHAR, expected.length())
                        .execute(connection, rs -> rs.getString(1));
                assertEquals(expected, selected);
            }
        }
        */
    }

    /*
    public static class BindRef {

        @Test
        public void value() throws Exception {
            try (Connection connection = db.getConnection()) {
                final Optional<Ref> expected = JDBJ.string("SELECT (SELECT * FROM INFORMATION_SCHEMA.TABLES LIMIT 1) AS ref").query()
                        .map(rs -> rs.getRef(1))
                        .first()
                        .execute(connection);
                assertTrue(expected.isPresent());
                final Ref selected = new TestBuilder()
                        .bindDefaultRef(":binding", null)
                        .bindOptionalRef(":binding", expected)
                        .execute(connection, rs -> rs.getRef(1));
                assertEquals(expected.get(), selected);
            }
        }

        @Test
        public void valueEmpty() throws Exception {
            try (Connection connection = db.getConnection()) {
                final Optional<Ref> expected = JDBJ.string("SELECT (SELECT * FROM INFORMATION_SCHEMA.TABLES LIMIT 1) AS ref").query()
                        .map(rs -> rs.getRef(1))
                        .first()
                        .execute(connection);
                assertTrue(expected.isPresent());
                final Ref selected = new TestBuilder()
                        .bindDefaultRef(":binding", expected.get())
                        .bindOptionalRef(":binding", Optional.empty())
                        .execute(connection, rs -> rs.getRef(1));
                assertEquals(expected.get(), selected);
            }
        }
    }
    */

    public static class BindShort {
        @Test
        public void value() throws Exception {
            final Short expected = 12;
            try (Connection connection = db.getConnection()) {
                final Short selected = new TestBuilder()
                        .bindDefaultShort(":binding", Short.MIN_VALUE)
                        .bindOptionalShort(":binding", Optional.of(expected))
                        .execute(connection, rs -> rs.getShort(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void notPresent() throws Exception {
            final Short expected = 12;
            try (Connection connection = db.getConnection()) {
                final Short selected = new TestBuilder()
                        .bindDefaultShort(":binding", expected)
                        .bindOptionalShort(":binding", Optional.empty())
                        .execute(connection, rs -> rs.getShort(1));
                assertEquals(expected, selected);
            }
        }
    }

    public static class BindShortPrimtiive {
        @Test
        public void value() throws Exception {
            final short expected = 12;
            try (Connection connection = db.getConnection()) {
                final short selected = new TestBuilder()
                        .bindDefaultShortPrimitive(":binding", Short.MIN_VALUE)
                        .bindOptionalShort(":binding", OptionalShort.of(expected))
                        .execute(connection, rs -> rs.getShortPrimitive(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void notPresent() throws Exception {
            final short expected = 12;
            try (Connection connection = db.getConnection()) {
                final short selected = new TestBuilder()
                        .bindDefaultShortPrimitive(":binding", expected)
                        .bindOptionalShort(":binding", OptionalShort.empty())
                        .execute(connection, rs -> rs.getShortPrimitive(1));
                assertEquals(expected, selected);
            }
        }
    }

    public static class BindString {

        @Test
        public void value() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new TestBuilder()
                        .bindDefaultString(":binding", null)
                        .bindOptionalString(":binding", Optional.of(expected))
                        .execute(connection, rs -> rs.getString(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void valueEmpty() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new TestBuilder()
                        .bindDefaultString(":binding", expected)
                        .bindOptionalString(":binding", Optional.empty())
                        .execute(connection, rs -> rs.getString(1));
                assertEquals(expected, selected);
            }
        }
    }

    /*
    public static class BindSQLXML {

        @Test
        public void value() throws Exception {
            final String expected = "<a></a>";
            try (Connection connection = db.getConnection()) {
                final SQLXML xml = connection.createSQLXML();
                xml.setString(expected);
                final String selected = new TestBuilder()
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

                final String selected = new TestBuilder()
                        .bindDefaultSQLXML(":binding", xml)
                        .bindOptionalSQLXML(":binding", Optional.empty())
                        .execute(connection, rs -> rs.getSQLXML(1).getString());
                assertEquals(expected, selected);
            }
        }
    }
    */

    public static class BindTime extends HasExpectedTimeOfDay {

        @Test
        public void value() throws Exception {
            final Time expected = new Time(expectedTime);
            try (Connection connection = db.getConnection()) {
                final Time selected = new TestBuilder()
                        .bindDefaultTime(":binding", null)
                        .bindOptionalTime(":binding", Optional.of(expected))
                        .execute(connection, rs -> rs.getTime(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void valueEmpty() throws Exception {
            final Time expected = new Time(expectedTime);
            try (Connection connection = db.getConnection()) {
                final Time selected = new TestBuilder()
                        .bindDefaultTime(":binding", expected)
                        .bindOptionalDate(":binding", Optional.empty())
                        .execute(connection, rs -> rs.getTime(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void valueCalendar() throws Exception {
            final Time expected = new Time(expectedTime);
            try (Connection connection = db.getConnection()) {
                final Time selected = new TestBuilder()
                        .bindDefaultTime(":binding", null, GregorianCalendar.getInstance())
                        .bindOptionalTime(":binding", Optional.of(expected), GregorianCalendar.getInstance())
                        .execute(connection, rs -> rs.getTime(1, GregorianCalendar.getInstance()));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void valueCalendarEmpty() throws Exception {
            final Time expected = new Time(expectedTime);
            try (Connection connection = db.getConnection()) {
                final Time selected = new TestBuilder()
                        .bindDefaultTime(":binding", expected, GregorianCalendar.getInstance())
                        .bindOptionalDate(":binding", Optional.empty(), GregorianCalendar.getInstance())
                        .execute(connection, rs -> rs.getTime(1, GregorianCalendar.getInstance()));
                assertEquals(expected, selected);
            }
        }

    }

    public static class BindTimestamp extends HasExpectedTimeSinceEpoch {

        @Test
        public void value() throws Exception {
            final Timestamp expected = new Timestamp(expectedTime);
            try (Connection connection = db.getConnection()) {
                final Timestamp selected = new TestBuilder()
                        .bindDefaultTimestamp(":binding", null)
                        .bindOptionalTimestamp(":binding", Optional.of(expected))
                        .execute(connection, rs -> rs.getTimestamp(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void valueEmpty() throws Exception {
            final Timestamp expected = new Timestamp(expectedTime);
            try (Connection connection = db.getConnection()) {
                final Timestamp selected = new TestBuilder()
                        .bindDefaultTimestamp(":binding", expected)
                        .bindOptionalTimestamp(":binding", Optional.empty())
                        .execute(connection, rs -> rs.getTimestamp(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void valueCalendar() throws Exception {
            final Timestamp expected = new Timestamp(expectedTime);
            try (Connection connection = db.getConnection()) {
                final Timestamp selected = new TestBuilder()
                        .bindDefaultTimestamp(":binding", null, GregorianCalendar.getInstance())
                        .bindOptionalTimestamp(":binding", Optional.of(expected), GregorianCalendar.getInstance())
                        .execute(connection, rs -> rs.getTimestamp(1, GregorianCalendar.getInstance()));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void valueCalendarEmpty() throws Exception {
            final Timestamp expected = new Timestamp(expectedTime);
            try (Connection connection = db.getConnection()) {
                final Timestamp selected = new TestBuilder()
                        .bindDefaultTimestamp(":binding", expected, GregorianCalendar.getInstance())
                        .bindOptionalTimestamp(":binding", Optional.empty(), GregorianCalendar.getInstance())
                        .execute(connection, rs -> rs.getTimestamp(1, GregorianCalendar.getInstance()));
                assertEquals(expected, selected);
            }
        }

    }

    /*
    public static class BindUrl {

        @Test
        public void value() throws Exception {
            final URL expected = new URL("http", "google.com", 8080, "/");
            try (Connection connection = db.getConnection()) {
                final URL selected = new TestBuilder()
                        .bindDefaultURL(":binding", null)
                        .bindOptionalURL(":binding", Optional.of(expected))
                        .execute(connection, rs -> rs.getURL(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void valueEmpty() throws Exception {
            final URL expected = new URL("http", "google.com", 8080, "/");
            try (Connection connection = db.getConnection()) {
                final URL selected = new TestBuilder()
                        .bindDefaultURL(":binding", expected)
                        .bindOptionalURL(":binding", Optional.empty())
                        .execute(connection, rs -> rs.getURL(1));
                assertEquals(expected, selected);
            }
        }
    }
    */

    private static final NamedParameterStatement statement =
            NamedParameterStatement.make("SELECT :binding as bound");

    private static class TestBuilder extends PositionalBindingsBuilder<TestBuilder> {

        TestBuilder() {
            this(OptionalValueBindingsBuilderTest.statement, PositionalBindings.empty());
        }

        TestBuilder(NamedParameterStatement statement, PositionalBindings bindings) {
            super(statement, bindings, TestBuilder::new);
        }

        <R> R execute(Connection connection, ResultSetMapper<R> mapper) throws SQLException {
            checkAllBindingsPresent();

            final R value;
            try (PreparedStatement ps = connection.prepareStatement(buildSql())) {
                bindToStatement(ps);
                try (SmartResultSet rs = new SmartResultSet(ps.executeQuery())) {
                    assertTrue(rs.next());
                    value = mapper.map(rs);
                    assertFalse(rs.next());
                }
            }
            return value;
        }
    }

}