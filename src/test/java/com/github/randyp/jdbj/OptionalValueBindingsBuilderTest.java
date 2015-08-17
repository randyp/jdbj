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
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.*;
import java.sql.Date;
import java.util.*;

import static org.junit.Assert.*;

@RunWith(Enclosed.class)
public class OptionalValueBindingsBuilderTest {

    @ClassRule
    public static final H2Rule db = new H2Rule();

    private static final NamedParameterStatement statement =
            NamedParameterStatement.make("SELECT :binding as bound");

    @Ignore //not supported in h2
    public static class array { //lower case so we don't capture java.sql.Array class

        @Test
        public void bind() throws Exception {
            final String[] expected = {"a", "b", "c"};

            final String[] selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindArray(":binding", null)
                        .bindOptionalArray(":binding", Optional.of(connection.createArrayOf("varchar", expected)))
                        .execute(connection, rs -> (String[]) rs.getArray(1).getArray());
            }
            assertArrayEquals(expected, selected);
        }

        @Test
        public void bindEmpty() throws Exception {
            final String[] expected = {"a", "b", "c"};

            final String[] selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultArray(":binding", connection.createArrayOf("varchar", expected))
                        .bindOptionalArray(":binding", Optional.empty())
                        .execute(connection, rs -> {
                            final Array array = rs.getArray(1);
                            return array == null ? null : (String[]) array.getArray();
                        });
            }
            assertArrayEquals(expected, selected);
        }
    }

    public static class AsciiStream {
        @Test
        public void inputStream() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultAsciiStream(":binding", null)
                        .bindOptionalAsciiStream(":binding", Optional.of(new ByteArrayInputStream(expected.getBytes(Charset.forName("ascii")))))
                        .execute(connection, rs -> rs.getString(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void inputStreamEmpty() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultAsciiStream(":binding", new ByteArrayInputStream(expected.getBytes(Charset.forName("ascii"))))
                        .bindOptionalAsciiStream(":binding", Optional.empty())
                        .execute(connection, rs -> rs.getString(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void inputStreamLength() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultAsciiStream(":binding", null, expected.length())
                        .bindOptionalAsciiStream(":binding", Optional.of(new ByteArrayInputStream(expected.getBytes(Charset.forName("ascii")))), expected.length())
                        .execute(connection, rs -> rs.getString(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void inputStreamLengthEmpty() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultAsciiStream(":binding", new ByteArrayInputStream(expected.getBytes(Charset.forName("ascii"))), expected.length())
                        .bindOptionalAsciiStream(":binding", Optional.empty(), expected.length())
                        .execute(connection, rs -> rs.getString(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void inputStreamLengthLong() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultAsciiStream(":binding", null, (long) expected.length())
                        .bindOptionalAsciiStream(":binding", Optional.of(new ByteArrayInputStream(expected.getBytes(Charset.forName("ascii")))), (long) expected.length())
                        .execute(connection, rs -> rs.getString(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void inputStreamLengthLongEmpty() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultAsciiStream(":binding", new ByteArrayInputStream(expected.getBytes(Charset.forName("ascii"))), (long) expected.length())
                        .bindOptionalAsciiStream(":binding", Optional.empty(), (long) expected.length())
                        .execute(connection, rs -> rs.getString(1));
            }
            assertEquals(expected, selected);
        }
    }

    public static class Big_Decimal {
        @Test
        public void value() throws Exception {
            final BigDecimal expected = new BigDecimal("1.234");

            final BigDecimal selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultBigDecimal(":binding", null)
                        .bindOptionalBigDecimal(":binding", Optional.of(expected))
                        .execute(connection, rs -> rs.getBigDecimal(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void nullValue() throws Exception {
            final BigDecimal expected = new BigDecimal("1.234");

            final BigDecimal selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultBigDecimal(":binding", expected)
                        .bindOptionalBigDecimal(":binding", Optional.empty())
                        .execute(connection, rs -> rs.getBigDecimal(1));
            }
            assertEquals(expected, selected);
        }
    }

    public static class BinaryStream {

        @Test
        public void inputStream() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultBinaryStream(":binding", null)
                        .bindOptionalBinaryStream(":binding", Optional.of(new ByteArrayInputStream(expected.getBytes())))
                        .execute(connection, rs -> new String(rs.getBytes(1)));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void inputStreamEmpty() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultBinaryStream(":binding", new ByteArrayInputStream(expected.getBytes()))
                        .bindOptionalBinaryStream(":binding", Optional.empty())
                        .execute(connection, rs -> {
                            final byte[] bytes = rs.getBytes(1);
                            return bytes == null ? null : new String(bytes);
                        });
            }
            assertEquals(expected, selected);
        }

        @Test
        public void inputLengthStream() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultBinaryStream(":binding", null, expected.length())
                        .bindOptionalBinaryStream(":binding", Optional.of(new ByteArrayInputStream(expected.getBytes())), expected.length())
                        .execute(connection, rs -> new String(rs.getBytes(1)));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void inputLengthStreamEmpty() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultBinaryStream(":binding", new ByteArrayInputStream(expected.getBytes()), expected.length())
                        .bindOptionalBinaryStream(":binding", Optional.empty(), expected.length())
                        .execute(connection, rs -> {
                            final byte[] bytes = rs.getBytes(1);
                            return bytes == null ? null : new String(bytes);
                        });
            }
            assertEquals(expected, selected);
        }

        @Test
        public void inputLengthLongStream() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultBinaryStream(":binding", null, (long) expected.length())
                        .bindOptionalBinaryStream(":binding", Optional.of(new ByteArrayInputStream(expected.getBytes())), (long) expected.length())
                        .execute(connection, rs -> new String(rs.getBytes(1)));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void inputLengthLongStreamEmpty() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultBinaryStream(":binding", new ByteArrayInputStream(expected.getBytes()), (long) expected.length())
                        .bindOptionalBinaryStream(":binding", Optional.empty(), (long) expected.length())
                        .execute(connection, rs -> {
                            final byte[] bytes = rs.getBytes(1);
                            return bytes == null ? null : new String(bytes);
                        });
            }
            assertEquals(expected, selected);
        }
    }

    public static class blob {
        @Test
        public void inputStream() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultBlob(":binding", (InputStream) null)
                        .bindOptionalBlob(":binding", Optional.of(new ByteArrayInputStream(expected.getBytes())))
                        .execute(connection, rs -> new String(rs.getBytes(1)));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void inputStreamEmpty() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultBlob(":binding", new ByteArrayInputStream(expected.getBytes()))
                        .bindOptionalBlob(":binding", Optional.empty())
                        .execute(connection, rs -> {
                            final byte[] bytes = rs.getBytes(1);
                            return bytes == null ? null : new String(bytes);
                        });
            }
            assertEquals(expected, selected);
        }

        @Test
        public void inputStreamLength() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultBlob(":binding", (InputStream) null)
                        .bindOptionalBlob(":binding", Optional.of(new ByteArrayInputStream(expected.getBytes())), (long) expected.length())
                        .execute(connection, rs -> new String(rs.getBytes(1)));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void inputStreamNullLength() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultBlob(":binding", new ByteArrayInputStream(expected.getBytes()), (long) expected.length())
                        .bindOptionalBlob(":binding", Optional.empty(), (long) expected.length())
                        .execute(connection, rs -> {
                            final byte[] bytes = rs.getBytes(1);
                            return bytes == null ? null : new String(bytes);
                        });
            }
            assertEquals(expected, selected);
        }
    }

    public static class Bool {

        @Test
        public void False() throws Exception {

            final boolean selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultBoolean(":binding", true)
                        .bindOptionalBoolean(":binding", OptionalBoolean.of(false))
                        .execute(connection, rs -> rs.getBoolean(1));
            }
            assertFalse(selected);
        }

        @Test
        public void True() throws Exception {
            final boolean selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultBoolean(":binding", false)
                        .bindOptionalBoolean(":binding", OptionalBoolean.of(true))
                        .execute(connection, rs -> rs.getBoolean(1));
            }
            assertTrue(selected);
        }

        @Test
        public void notPresent() throws Exception {
            final boolean selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultBoolean(":binding", true)
                        .bindOptionalBoolean(":binding", OptionalBoolean.empty())
                        .execute(connection, rs -> rs.getBoolean(1));
            }
            assertTrue(selected);
        }
    }

    public static class Byte {

        @Test
        public void present() throws Exception {
            final byte expected = 6;
            final byte selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultByte(":binding", (byte) 7)
                        .bindOptionalByte(":binding", OptionalByte.of(expected))
                        .execute(connection, rs -> rs.getByte(1));
            }
            assertEquals(expected, selected, 0.0);
        }

        @Test
        public void notPresent() throws Exception {
            final byte expected = 6;
            final byte selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultByte(":binding", expected)
                        .bindOptionalByte(":binding", OptionalByte.empty())
                        .execute(connection, rs -> rs.getByte(1));
            }
            assertEquals(expected, selected, 0.0);
        }
    }

    public static class Bytes {

        @Test
        public void value() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultBytes(":binding", null)
                        .bindOptionalBytes(":binding", Optional.of(expected.getBytes()))
                        .execute(connection, rs -> new String(rs.getBytes(1)));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueEmpty() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultBytes(":binding", expected.getBytes())
                        .bindOptionalBinaryStream(":binding", Optional.empty())
                        .execute(connection, rs -> {
                            final byte[] bytes = rs.getBytes(1);
                            return bytes == null ? null : new String(bytes);
                        });
            }
            assertEquals(expected, selected);
        }
    }

    public static class CharacterStream {
        @Test
        public void reader() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultCharacterStream(":binding", null)
                        .bindOptionalCharacterStream(":binding", Optional.of(new StringReader(expected)))
                        .execute(connection, rs -> rs.getString(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void readerEmpty() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultCharacterStream(":binding", new StringReader(expected))
                        .bindOptionalCharacterStream(":binding", Optional.empty())
                        .execute(connection, rs -> rs.getString(1));
            }
            assertEquals(expected, selected);
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
            }
            assertEquals(expected, selected);
        }

        @Test
        public void readerLengthEmpty() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultCharacterStream(":binding", new StringReader(expected), expected.length())
                        .bindOptionalCharacterStream(":binding", Optional.empty(), expected.length())
                        .execute(connection, rs -> rs.getString(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void readerLengthLong() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultCharacterStream(":binding", null, (long) expected.length())
                        .bindOptionalCharacterStream(":binding", Optional.of(new StringReader(expected)), (long) expected.length())
                        .execute(connection, rs -> rs.getString(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void readerLengthLongEmpty() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultCharacterStream(":binding", new StringReader(expected), (long) expected.length())
                        .bindOptionalCharacterStream(":binding", Optional.empty(), (long) expected.length())
                        .execute(connection, rs -> rs.getString(1));
            }
            assertEquals(expected, selected);
        }
    }

    public static class clob {

        @Test
        public void reader() throws Exception {
            final String expected = "abcd";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultClob(":binding", (Reader) null)
                        .bindOptionalClob(":binding", Optional.of(new StringReader(expected)))
                        .execute(connection, rs -> rs.getClob(1).getSubString(1L, expected.length()));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void readerEmpty() throws Exception {
            final String expected = "abcd";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultClob(":binding", new StringReader(expected))
                        .bindOptionalClob(":binding", Optional.empty())
                        .execute(connection, rs -> {
                            final Clob clob = rs.getClob(1);
                            return clob == null ? null : clob.getSubString(1L, expected.length());
                        });
            }
            assertEquals(expected, selected);
        }

        @Test
        public void readerLength() throws Exception {
            final String expected = "abcd";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultClob(":binding", (Reader) null)
                        .bindOptionalClob(":binding", Optional.of(new StringReader(expected)), (long) expected.length())
                        .execute(connection, rs -> rs.getClob(1).getSubString(1L, expected.length()));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void readerLengthEmpty() throws Exception {
            final String expected = "abcd";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultClob(":binding", new StringReader(expected), (long) expected.length())
                        .bindOptionalClob(":binding", Optional.empty(), (long) expected.length())
                        .execute(connection, rs -> {
                            final Clob clob = rs.getClob(1);
                            return clob == null ? null : clob.getSubString(1L, expected.length());
                        });
            }
            assertEquals(expected, selected);
        }
    }

    public static class date {

        private final long expectedTime;

        public date() {
            final Calendar instance = GregorianCalendar.getInstance();
            instance.set(2015, Calendar.JANUARY, 1, 0, 0, 0);
            this.expectedTime = 1000 * (instance.getTimeInMillis() / 1000);
        }

        @Test
        public void value() throws Exception {
            final Date expected = new Date(expectedTime);

            final Date selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultDate(":binding", null)
                        .bindOptionalDate(":binding", Optional.of(expected))
                        .execute(connection, rs -> rs.getDate(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueEmpty() throws Exception {
            final Date expected = new Date(expectedTime);

            final Date selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultDate(":binding", expected)
                        .bindOptionalDate(":binding", Optional.empty())
                        .execute(connection, rs -> rs.getDate(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueCalendar() throws Exception {
            final Date expected = new Date(expectedTime);

            final Date selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultDate(":binding", null, GregorianCalendar.getInstance())
                        .bindOptionalDate(":binding", Optional.of(expected), GregorianCalendar.getInstance())
                        .execute(connection, rs -> rs.getDate(1, GregorianCalendar.getInstance()));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueCalendarEmpty() throws Exception {
            final Date expected = new Date(expectedTime);

            final Date selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultDate(":binding", expected, GregorianCalendar.getInstance())
                        .bindOptionalDate(":binding", Optional.empty(), GregorianCalendar.getInstance())
                        .execute(connection, rs -> rs.getDate(1, GregorianCalendar.getInstance()));
            }
            assertEquals(expected, selected);
        }

    }

    public static class BindDouble {
        @Test
        public void value() throws Exception {
            final double expected = 1.2;

            final double selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultDouble(":binding", Double.MIN_VALUE)
                        .bindOptionalDouble(":binding", OptionalDouble.of(expected))
                        .execute(connection, rs -> rs.getDouble(1));
            }
            assertEquals(expected, selected, 0.0);
        }

        @Test
        public void notPresent() throws Exception {
            final double expected = 1.2;

            final double selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultDouble(":binding", expected)
                        .bindOptionalDouble(":binding", OptionalDouble.empty())
                        .execute(connection, rs -> rs.getDouble(1));
            }
            assertEquals(expected, selected, 0.0);
        }
    }

    public static class BindFloat {
        @Test
        public void value() throws Exception {
            final float expected = 1.2f;

            final float selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultFloat(":binding", Float.MIN_VALUE)
                        .bindOptionalFloat(":binding", OptionalFloat.of(expected))
                        .execute(connection, rs -> rs.getFloat(1));
            }
            assertEquals(expected, selected, 0.0);
        }

        @Test
        public void notPresent() throws Exception {
            final float expected = 1.2f;

            final float selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultFloat(":binding", expected)
                        .bindOptionalFloat(":binding", OptionalFloat.empty())
                        .execute(connection, rs -> rs.getFloat(1));
            }
            assertEquals(expected, selected, 0.0);
        }
    }

    public static class BindInt {
        @Test
        public void value() throws Exception {
            final int expected = 12;

            final int selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultInt(":binding", Integer.MIN_VALUE)
                        .bindOptionalInt(":binding", OptionalInt.of(expected))
                        .execute(connection, rs -> rs.getInt(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void notPresent() throws Exception {
            final int expected = 12;

            final int selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultInt(":binding", expected)
                        .bindOptionalInt(":binding", OptionalInt.empty())
                        .execute(connection, rs -> rs.getInt(1));
            }
            assertEquals(expected, selected);
        }
    }

    public static class lng {
        @Test
        public void value() throws Exception {
            final long expected = 12L;

            final long selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultLong(":binding", Long.MIN_VALUE)
                        .bindOptionalLong(":binding", OptionalLong.of(expected))
                        .execute(connection, rs -> rs.getLong(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void notPresent() throws Exception {
            final long expected = 12L;

            final long selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultLong(":binding", expected)
                        .bindOptionalLong(":binding", OptionalLong.empty())
                        .execute(connection, rs -> rs.getLong(1));
            }
            assertEquals(expected, selected);
        }
    }

    public static class NCharacterStream {

        @Test
        public void reader() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultNCharacterStream(":binding", null)
                        .bindOptionalNCharacterStream(":binding", Optional.of(new StringReader(expected)))
                        .execute(connection, rs -> rs.getString(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void readerEmpty() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultNCharacterStream(":binding", new StringReader(expected))
                        .bindOptionalNCharacterStream(":binding", Optional.empty())
                        .execute(connection, rs -> rs.getString(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void readerLength() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultNCharacterStream(":binding", null, (long) expected.length())
                        .bindOptionalNCharacterStream(":binding", Optional.of(new StringReader(expected)), (long) expected.length())
                        .execute(connection, rs -> rs.getString(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void readerNullLength() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultNCharacterStream(":binding", new StringReader(expected), (long) expected.length())
                        .bindOptionalNCharacterStream(":binding", Optional.empty(), (long) expected.length())
                        .execute(connection, rs -> rs.getString(1));
            }
            assertEquals(expected, selected);
        }
    }

    public static class nclob {


        @Test
        public void reader() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultNClob(":binding", (Reader) null)
                        .bindOptionalNClob(":binding", Optional.of(new StringReader(expected)))
                        .execute(connection, rs -> rs.getString(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void readerEmpty() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultNClob(":binding", new StringReader(expected))
                        .bindOptionalNClob(":binding", Optional.empty())
                        .execute(connection, rs -> rs.getString(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void readerLength() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultNClob(":binding", null, (long) expected.length())
                        .bindOptionalNClob(":binding", Optional.of(new StringReader(expected)), (long) expected.length())
                        .execute(connection, rs -> rs.getString(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void readerNullLength() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultNClob(":binding", new StringReader(expected), (long) expected.length())
                        .bindOptionalNClob(":binding", Optional.empty(), (long) expected.length())
                        .execute(connection, rs -> rs.getString(1));
            }
            assertEquals(expected, selected);
        }
    }

    public static class nstring {

        @Test
        public void value() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultNString(":binding", null)
                        .bindOptionalNString(":binding", Optional.of(expected))
                        .execute(connection, rs -> rs.getNString(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueEmpty() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultNString(":binding", expected)
                        .bindOptionalNString(":binding", Optional.empty())
                        .execute(connection, rs -> rs.getString(1));
            }
            assertEquals(expected, selected);
        }
    }

    public static class object {

        @Test
        public void value() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultObject(":binding", null)
                        .bindOptionalObject(":binding", Optional.of(expected))
                        .execute(connection, rs -> rs.getObject(1).toString());
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueEmpty() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultObject(":binding", expected)
                        .bindOptionalObject(":binding", Optional.empty())
                        .execute(connection, rs -> rs.getString(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueType() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultObject(":binding", null, Types.VARCHAR)
                        .bindOptionalObject(":binding", Optional.of(expected), Types.VARCHAR)
                        .execute(connection, rs -> rs.getObject(1).toString());
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueNullType() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultObject(":binding", expected, Types.VARCHAR)
                        .bindOptionalObject(":binding", Optional.empty(), Types.VARCHAR)
                        .execute(connection, rs -> rs.getString(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueTypeLength() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultObject(":binding", null, Types.VARCHAR, expected.length())
                        .bindOptionalObject(":binding", Optional.of(expected), Types.VARCHAR, expected.length())
                        .execute(connection, rs -> rs.getObject(1).toString());
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueNullTypeLength() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultObject(":binding", expected, Types.VARCHAR, expected.length())
                        .bindOptionalObject(":binding", Optional.empty(), Types.VARCHAR, expected.length())
                        .execute(connection, rs -> rs.getString(1));
            }
            assertEquals(expected, selected);
        }

        @Ignore
        @Test
        public void valueSQLType() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultObject(":binding", null, JDBCType.VARCHAR)
                        .bindOptionalObject(":binding", Optional.of(expected), JDBCType.VARCHAR)
                        .execute(connection, rs -> rs.getObject(1).toString());
            }
            assertEquals(expected, selected);
        }

        @Ignore
        @Test
        public void valueNullSQLType() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultObject(":binding", expected, JDBCType.VARCHAR)
                        .bindOptionalObject(":binding", Optional.empty(), JDBCType.VARCHAR)
                        .execute(connection, rs -> rs.getString(1));
            }
            assertEquals(expected, selected);
        }

        @Ignore //not supported in h2
        @Test
        public void valueSQLTypeLength() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultObject(":binding", null, JDBCType.VARCHAR, expected.length())
                        .bindOptionalObject(":binding", Optional.of(expected), JDBCType.VARCHAR, expected.length())
                        .execute(connection, rs -> rs.getObject(1).toString());
            }
            assertEquals(expected, selected);
        }

        @Ignore //not supported in h2
        @Test
        public void valueNullSQLTypeLength() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultObject(":binding", expected, JDBCType.VARCHAR, expected.length())
                        .bindOptionalObject(":binding", Optional.empty(), JDBCType.VARCHAR, expected.length())
                        .execute(connection, rs -> rs.getString(1));
            }
            assertEquals(expected, selected);
        }
    }

    @Ignore //not supported in h2
    public static class ref {

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

    public static class Shrt {
        @Test
        public void value() throws Exception {
            final short expected = 12;

            final short selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultShort(":binding", Short.MIN_VALUE)
                        .bindOptionalShort(":binding", OptionalShort.of(expected))
                        .execute(connection, rs -> rs.getShort(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void notPresent() throws Exception {
            final short expected = 12;

            final short selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultShort(":binding", expected)
                        .bindOptionalShort(":binding", OptionalShort.empty())
                        .execute(connection, rs -> rs.getShort(1));
            }
            assertEquals(expected, selected);
        }
    }

    public static class string {

        @Test
        public void value() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultString(":binding", null)
                        .bindOptionalString(":binding", Optional.of(expected))
                        .execute(connection, rs -> rs.getString(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueEmpty() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultString(":binding", expected)
                        .bindOptionalString(":binding", Optional.empty())
                        .execute(connection, rs -> rs.getString(1));
            }
            assertEquals(expected, selected);
        }
    }

    @Ignore //not supported in h2
    public static class sqlxml {

        @Test
        public void value() throws Exception {
            final String expected = "<a></a>";

            final String selected;
            try (Connection connection = db.getConnection()) {
                final SQLXML xml = connection.createSQLXML();
                xml.setString(expected);
                selected = new TestBuilder()
                        .bindDefaultSQLXML(":binding", null)
                        .bindOptionalSQLXML(":binding", Optional.of(xml))
                        .execute(connection, rs -> rs.getSQLXML(1).getString());
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueEmpty() throws Exception {
            final String expected = "<a></a>";

            final String selected;
            try (Connection connection = db.getConnection()) {
                final SQLXML xml = connection.createSQLXML();
                xml.setString(expected);

                selected = new TestBuilder()
                        .bindDefaultSQLXML(":binding", xml)
                        .bindOptionalSQLXML(":binding", Optional.empty())
                        .execute(connection, rs -> rs.getSQLXML(1).getString());
            }
            assertEquals(expected, selected);
        }
    }

    public static class time {

        private final long expectedTime;

        public time() {
            final Calendar instance = GregorianCalendar.getInstance();
            instance.set(1970, Calendar.JANUARY, 1, 12, 11, 10);
            this.expectedTime = 1000 * (instance.getTimeInMillis() / 1000);
        }

        @Test
        public void value() throws Exception {
            final Time expected = new Time(expectedTime);

            final Time selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultTime(":binding", null)
                        .bindOptionalTime(":binding", Optional.of(expected))
                        .execute(connection, rs -> rs.getTime(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueEmpty() throws Exception {
            final Time expected = new Time(expectedTime);

            final Time selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultTime(":binding", expected)
                        .bindOptionalDate(":binding", Optional.empty())
                        .execute(connection, rs -> rs.getTime(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueCalendar() throws Exception {
            final Time expected = new Time(expectedTime);

            final Time selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultTime(":binding", null, GregorianCalendar.getInstance())
                        .bindOptionalTime(":binding", Optional.of(expected), GregorianCalendar.getInstance())
                        .execute(connection, rs -> rs.getTime(1, GregorianCalendar.getInstance()));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueCalendarEmpty() throws Exception {
            final Time expected = new Time(expectedTime);

            final Time selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultTime(":binding", expected, GregorianCalendar.getInstance())
                        .bindOptionalDate(":binding", Optional.empty(), GregorianCalendar.getInstance())
                        .execute(connection, rs -> rs.getTime(1, GregorianCalendar.getInstance()));
            }
            assertEquals(expected, selected);
        }

    }

    public static class timestamp {

        private final long expectedTime;

        public timestamp() {
            final Calendar instance = GregorianCalendar.getInstance();
            instance.set(2015, Calendar.JANUARY, 1, 0, 0, 0);
            this.expectedTime = 1000 * (instance.getTimeInMillis() / 1000);
        }

        @Test
        public void value() throws Exception {
            final Timestamp expected = new Timestamp(expectedTime);

            final Timestamp selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultTimestamp(":binding", null)
                        .bindOptionalTimestamp(":binding", Optional.of(expected))
                        .execute(connection, rs -> rs.getTimestamp(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueEmpty() throws Exception {
            final Timestamp expected = new Timestamp(expectedTime);

            final Timestamp selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultTimestamp(":binding", expected)
                        .bindOptionalTimestamp(":binding", Optional.empty())
                        .execute(connection, rs -> rs.getTimestamp(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueCalendar() throws Exception {
            final Timestamp expected = new Timestamp(expectedTime);

            final Timestamp selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultTimestamp(":binding", null, GregorianCalendar.getInstance())
                        .bindOptionalTimestamp(":binding", Optional.of(expected), GregorianCalendar.getInstance())
                        .execute(connection, rs -> rs.getTimestamp(1, GregorianCalendar.getInstance()));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueCalendarEmpty() throws Exception {
            final Timestamp expected = new Timestamp(expectedTime);

            final Timestamp selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultTimestamp(":binding", expected, GregorianCalendar.getInstance())
                        .bindOptionalTimestamp(":binding", Optional.empty(), GregorianCalendar.getInstance())
                        .execute(connection, rs -> rs.getTimestamp(1, GregorianCalendar.getInstance()));
            }
            assertEquals(expected, selected);
        }

    }

    @Ignore //not supported in h2
    public static class url {

        @Test
        public void value() throws Exception {
            final URL expected = new URL("http", "google.com", 8080, "/");

            final URL selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultURL(":binding", null)
                        .bindOptionalURL(":binding", Optional.of(expected))
                        .execute(connection, rs -> rs.getURL(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueEmpty() throws Exception {
            final URL expected = new URL("http", "google.com", 8080, "/");

            final URL selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultURL(":binding", expected)
                        .bindOptionalURL(":binding", Optional.empty())
                        .execute(connection, rs -> rs.getURL(1));
            }
            assertEquals(expected, selected);
        }
    }

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
                try (ResultSet rs = ps.executeQuery()) {
                    assertTrue(rs.next());
                    value = mapper.map(rs);
                    assertFalse(rs.next());
                }
            }
            return value;
        }
    }

}