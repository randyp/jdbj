package com.github.randyp.jdbj;

import com.github.randyp.jdbj.lambda.ResultSetMapper;
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
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(Enclosed.class)
public class DefaultValueBindingsBuilderTest {

    @ClassRule
    public static final H2Rule db = new H2Rule();

    private static final NamedParameterStatement statement =
            NamedParameterStatement.make("SELECT :binding as bound");

    @Ignore //not supported in h2
    public static class BindArray { //lower case so we don't capture java.sql.Array class

        @Test
        public void bind() throws Exception {
            final String[] expected = {"a", "b", "c"};

            final String[] selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultArray(":binding", connection.createArrayOf("varchar", expected))
                        .execute(connection, rs -> (String[]) rs.getArray(1).getArray());
            }
            assertArrayEquals(expected, selected);
        }

        @Test
        public void bindNull() throws Exception {
            final String[] selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultArray(":binding", null)
                        .execute(connection, rs -> {
                            final Array array = rs.getArray(1);
                            return array == null ? null : (String[]) array.getArray();
                        });
            }
            assertNull(selected);
        }
    }

    public static class BindAsciiStream {
        @Test
        public void inputStream() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultAsciiStream(":binding", new ByteArrayInputStream(expected.getBytes(Charset.forName("ascii"))))
                        .execute(connection, rs -> rs.getString(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void inputStreamNull() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultAsciiStream(":binding", null)
                        .execute(connection, rs -> rs.getString(1));
            }
            assertNull(selected);
        }

        @Test
        public void inputStreamLength() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultAsciiStream(":binding", new ByteArrayInputStream(expected.getBytes(Charset.forName("ascii"))), expected.length())
                        .execute(connection, rs -> rs.getString(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void inputStreamLengthNull() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultAsciiStream(":binding", null, 5)
                        .execute(connection, rs -> rs.getString(1));
            }
            assertNull(selected);
        }

        @Test
        public void inputStreamLengthLong() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultAsciiStream(":binding", new ByteArrayInputStream(expected.getBytes(Charset.forName("ascii"))), (long) expected.length())
                        .execute(connection, rs -> rs.getString(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void inputStreamLengthLongNull() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultAsciiStream(":binding", null, 5L)
                        .execute(connection, rs -> rs.getString(1));
            }
            assertNull(selected);
        }
    }

    public static class BindBigDecimal {
        @Test
        public void value() throws Exception {
            final BigDecimal expected = new BigDecimal("1.234");

            final BigDecimal selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultBigDecimal(":binding", expected)
                        .execute(connection, rs -> rs.getBigDecimal(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void nullValue() throws Exception {
            final BigDecimal selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultBigDecimal(":binding", null)
                        .execute(connection, rs -> rs.getBigDecimal(1));
            }
            assertNull(selected);
        }
    }

    public static class BindBinaryStream {

        @Test
        public void inputStream() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultBinaryStream(":binding", new ByteArrayInputStream(expected.getBytes()))
                        .execute(connection, rs -> new String(rs.getBytes(1)));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void inputStreamNull() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultBinaryStream(":binding", null)
                        .execute(connection, rs -> {
                            final byte[] bytes = rs.getBytes(1);
                            return bytes == null ? null : new String(bytes);
                        });
            }
            assertNull(selected);
        }

        @Test
        public void inputLengthStream() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultBinaryStream(":binding", new ByteArrayInputStream(expected.getBytes()), 5)
                        .execute(connection, rs -> new String(rs.getBytes(1)));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void inputLengthStreamNull() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultBinaryStream(":binding", null, 5)
                        .execute(connection, rs -> {
                            final byte[] bytes = rs.getBytes(1);
                            return bytes == null ? null : new String(bytes);
                        });
            }
            assertNull(selected);
        }

        @Test
        public void inputLengthLongStream() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultBinaryStream(":binding", new ByteArrayInputStream(expected.getBytes()), 5L)
                        .execute(connection, rs -> new String(rs.getBytes(1)));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void inputLengthLongStreamNull() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultBinaryStream(":binding", null, 5L)
                        .execute(connection, rs -> {
                            final byte[] bytes = rs.getBytes(1);
                            return bytes == null ? null : new String(bytes);
                        });
            }
            assertNull(selected);
        }
    }

    public static class BindBlob {

        @Test
        public void value() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                final Blob blob = connection.createBlob();
                blob.setBytes(1, expected.getBytes());
                selected = new TestBuilder()
                        .bindDefaultBlob(":binding", blob)
                        .execute(connection, rs -> new String(rs.getBytes(1)));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueNull() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultBlob(":binding", (Blob) null)
                        .execute(connection, rs -> {
                            final byte[] bytes = rs.getBytes(1);
                            return bytes == null ? null : new String(bytes);
                        });
            }
            assertNull(selected);
        }

        @Test
        public void inputStream() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultBlob(":binding", new ByteArrayInputStream(expected.getBytes()))
                        .execute(connection, rs -> new String(rs.getBytes(1)));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void inputStreamNull() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultBlob(":binding", (InputStream) null)
                        .execute(connection, rs -> {
                            final byte[] bytes = rs.getBytes(1);
                            return bytes == null ? null : new String(bytes);
                        });
            }
            assertNull(selected);
        }

        @Test
        public void inputStreamLength() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultBlob(":binding", new ByteArrayInputStream(expected.getBytes()), (long) expected.length())
                        .execute(connection, rs -> new String(rs.getBytes(1)));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void inputStreamNullLength() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultBlob(":binding", null, 5L)
                        .execute(connection, rs -> {
                            final byte[] bytes = rs.getBytes(1);
                            return bytes == null ? null : new String(bytes);
                        });
            }
            assertNull(selected);
        }
    }

    public static class BindBoolean {

        @Test
        public void False() throws Exception {
            final Boolean selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultBoolean(":binding", Boolean.FALSE)
                        .execute(connection, rs -> rs.getBoolean(1));
            }
            assertFalse(selected);
        }

        @Test
        public void True() throws Exception {
            final Boolean selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultBoolean(":binding", true)
                        .execute(connection, rs -> rs.getBoolean(1));
            }
            assertTrue(selected);
        }

        @Test
        public void Null() throws Exception {
            final Boolean selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultBoolean(":binding", null)
                        .execute(connection, rs -> rs.getBoolean(1));
            }
            assertNull(selected);
        }
    }

    public static class BindBooleanPrimitive {

        @Test
        public void False() throws Exception {

            final boolean selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultBooleanPrimitive(":binding", false)
                        .execute(connection, rs -> rs.getBooleanPrimitive(1));
            }
            assertFalse(selected);
        }

        @Test
        public void True() throws Exception {
            final boolean selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultBooleanPrimitive(":binding", true)
                        .execute(connection, rs -> rs.getBooleanPrimitive(1));
            }
            assertTrue(selected);
        }
    }

    public static class BindByte {

        @Test
        public void value() throws Exception {
            final Byte expected = 6;
            final Byte selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultByte(":binding", expected)
                        .execute(connection, rs -> rs.getByte(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void Null() throws Exception {
            final Byte selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultByte(":binding", null)
                        .execute(connection, rs -> rs.getByte(1));
            }
            assertNull(selected);
        }
    }

    public static class BindBytePrimitive {

        @Test
        public void value() throws Exception {
            final byte expected = 6;
            final byte selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultBytePrimtive(":binding", expected)
                        .execute(connection, rs -> rs.getBytePrimitive(1));
            }
            assertEquals(expected, selected);
        }
    }

    public static class BindBytes {

        @Test
        public void value() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultByteArray(":binding", expected.getBytes())
                        .execute(connection, rs -> new String(rs.getBytes(1)));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueNull() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultBinaryStream(":binding", null)
                        .execute(connection, rs -> {
                            final byte[] bytes = rs.getBytes(1);
                            return bytes == null ? null : new String(bytes);
                        });
            }
            assertNull(selected);
        }
    }

    public static class BindCharacterStream {
        @Test
        public void reader() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultCharacterStream(":binding", new StringReader(expected))
                        .execute(connection, rs -> rs.getString(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void readerNull() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultCharacterStream(":binding", null)
                        .execute(connection, rs -> rs.getString(1));
            }
            assertNull(selected);
        }

        @Test
        public void readerLength() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultCharacterStream(":binding", new StringReader(expected), 5)
                        .execute(connection, rs -> rs.getString(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void readerLengthNull() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultCharacterStream(":binding", null, 5)
                        .execute(connection, rs -> rs.getString(1));
            }
            assertNull(selected);
        }

        @Test
        public void readerLengthLong() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultCharacterStream(":binding", new StringReader(expected), 5L)
                        .execute(connection, rs -> rs.getString(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void readerLengthLongNull() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultCharacterStream(":binding", null, 5L)
                        .execute(connection, rs -> rs.getString(1));
            }
            assertNull(selected);
        }
    }

    public static class BindClob {

        @Test
        public void value() throws Exception {
            final String expected = "abcd";

            final String selected;
            try (Connection connection = db.getConnection()) {
                final Clob clob = connection.createClob();
                clob.setString(1, expected);
                selected = new TestBuilder()
                        .bindDefaultClob(":binding", clob)
                        .execute(connection, rs -> rs.getClob(1).getSubString(1L, expected.length()));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueNull() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultClob(":binding", (Clob) null)
                        .execute(connection, rs -> {
                            final byte[] bytes = rs.getBytes(1);
                            return bytes == null ? null : new String(bytes);
                        });
            }
            assertNull(selected);
        }

        @Test
        public void reader() throws Exception {
            final String expected = "abcd";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultClob(":binding", new StringReader(expected))
                        .execute(connection, rs -> rs.getClob(1).getSubString(1L, expected.length()));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void readerNull() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultClob(":binding", (Reader) null)
                        .execute(connection, rs -> {
                            final byte[] bytes = rs.getBytes(1);
                            return bytes == null ? null : new String(bytes);
                        });
            }
            assertNull(selected);
        }

        @Test
        public void readerLength() throws Exception {
            final String expected = "abcd";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultClob(":binding", new StringReader(expected), (long) expected.length())
                        .execute(connection, rs -> rs.getClob(1).getSubString(1L, expected.length()));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void readerLengthNull() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultClob(":binding", null, 4L)
                        .execute(connection, rs -> {
                            final byte[] bytes = rs.getBytes(1);
                            return bytes == null ? null : new String(bytes);
                        });
            }
            assertNull(selected);
        }
    }

    public static class BindDate extends HasExpectedTime {

        @Test
        public void value() throws Exception {
            final Date expected = new Date(expectedTime);

            final Date selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultDate(":binding", expected)
                        .execute(connection, rs -> rs.getDate(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueNull() throws Exception {
            final Date selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultDate(":binding", null)
                        .execute(connection, rs -> rs.getDate(1));
            }
            assertNull(selected);
        }

        @Test
        public void valueCalendar() throws Exception {
            final Date expected = new Date(expectedTime);

            final Date selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultDate(":binding", expected, GregorianCalendar.getInstance())
                        .execute(connection, rs -> rs.getDate(1, GregorianCalendar.getInstance()));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueCalendarNull() throws Exception {
            final Date selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultDate(":binding", null, GregorianCalendar.getInstance())
                        .execute(connection, rs -> rs.getDate(1, GregorianCalendar.getInstance()));
            }
            assertNull(selected);
        }

    }

    public static class BindDouble {
        @Test
        public void value() throws Exception {
            final Double expected = 1.2;

            final Double selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultDouble(":binding", expected)
                        .execute(connection, rs -> rs.getDouble(1));
            }
            assertEquals(expected, selected, 0.0);
        }

        @Test
        public void Null() throws Exception {
            final Double selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultDouble(":binding", null)
                        .execute(connection, rs -> rs.getDouble(1));
            }
            assertNull(selected);
        }
    }

    public static class BindDoublePrimitive {
        @Test
        public void value() throws Exception {
            final double expected = 1.2;

            final double selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultDoublePrimitive(":binding", expected)
                        .execute(connection, rs -> rs.getDoublePrimitive(1));
            }
            assertEquals(expected, selected, 0.0);
        }
    }

    public static class BindFloat {
        @Test
        public void value() throws Exception {
            final Float expected = 1.2f;

            final Float selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultFloat(":binding", expected)
                        .execute(connection, rs -> rs.getFloat(1));
            }
            assertEquals(expected, selected, 0.0f);
        }

        @Test
        public void Null() throws Exception {
            final Float selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultFloat(":binding", null)
                        .execute(connection, rs -> rs.getFloat(1));
            }
            assertNull(selected);
        }
    }

    public static class BindFloatPrimitive {
        @Test
        public void value() throws Exception {
            final float expected = 1.2f;

            final float selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultFloatPrimtitive(":binding", expected)
                        .execute(connection, rs -> rs.getFloatPrimitive(1));
            }
            assertEquals(expected, selected, 0.0f);
        }
    }

    public static class BindInteger {
        @Test
        public void value() throws Exception {
            final Integer expected = 12;

            final Integer selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultInteger(":binding", expected)
                        .execute(connection, rs -> rs.getInteger(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void Null() throws Exception {
            final Integer selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultInteger(":binding", null)
                        .execute(connection, rs -> rs.getInteger(1));
            }
            assertNull(selected);
        }

    }

    public static class BindIntegerPrimitive {
        @Test
        public void value() throws Exception {
            final int expected = 12;

            final int selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultIntegerPrimitive(":binding", expected)
                        .execute(connection, rs -> rs.getIntegerPrimitive(1));
            }
            assertEquals(expected, selected);
        }
    }

    public static class BindInt {
        @Test
        public void value() throws Exception {
            final int expected = 12;

            final int selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultInt(":binding", expected)
                        .execute(connection, rs -> rs.getInt(1));
            }
            assertEquals(expected, selected);
        }
    }

    public static class BindLong {
        @Test
        public void value() throws Exception {
            final Long expected = 12L;

            final Long selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultLong(":binding", expected)
                        .execute(connection, rs -> rs.getLong(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void Null() throws Exception {
            final Long selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultLong(":binding", null)
                        .execute(connection, rs -> rs.getLong(1));
            }
            assertNull(selected);
        }
    }

    public static class BindLongPrimitive {
        @Test
        public void value() throws Exception {
            final long expected = 12L;

            final long selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultLongPrimitive(":binding", expected)
                        .execute(connection, rs -> rs.getLongPrimitive(1));
            }
            assertEquals(expected, selected);
        }
    }

    public static class BindNCharacterStream {

        @Test
        public void reader() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultNCharacterStream(":binding", new StringReader(expected))
                        .execute(connection, rs -> rs.getString(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void readerNull() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultNCharacterStream(":binding", null)
                        .execute(connection, rs -> rs.getString(1));
            }
            assertNull(selected);
        }

        @Test
        public void readerLength() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultNCharacterStream(":binding", new StringReader(expected), 5L)
                        .execute(connection, rs -> rs.getString(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void readerNullLength() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultNCharacterStream(":binding", null, 5L)
                        .execute(connection, rs -> rs.getString(1));
            }
            assertNull(selected);
        }
    }

    public static class BindNClob {

        @Test
        public void value() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                final NClob nClob = connection.createNClob();
                nClob.setString(1L, expected);
                selected = new TestBuilder()
                        .bindDefaultNClob(":binding", nClob)
                        .execute(connection, rs -> rs.getNClob(1).getSubString(1L, expected.length()));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueNull() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultNClob(":binding", (NClob) null)
                        .execute(connection, rs -> rs.getString(1));
            }
            assertNull(selected);
        }

        @Test
        public void reader() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultNClob(":binding", new StringReader(expected))
                        .execute(connection, rs -> rs.getString(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void readerNull() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultNClob(":binding", (Reader) null)
                        .execute(connection, rs -> rs.getString(1));
            }
            assertNull(selected);
        }

        @Test
        public void readerLength() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultNClob(":binding", new StringReader(expected), (long) expected.length())
                        .execute(connection, rs -> rs.getString(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void readerNullLength() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultNClob(":binding", null, 5L)
                        .execute(connection, rs -> rs.getString(1));
            }
            assertNull(selected);
        }
    }

    public static class BindNString {

        @Test
        public void value() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultNString(":binding", expected)
                        .execute(connection, rs -> rs.getNString(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueNull() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultNString(":binding", null)
                        .execute(connection, rs -> rs.getString(1));
            }
            assertNull(selected);
        }
    }

    public static class BindNull {
        @Test
        public void type() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultNull(":binding", Types.VARCHAR)
                        .execute(connection, rs -> rs.getString(1));
            }
            assertNull(selected);
        }

        @Test
        public void typeAndName() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultNull(":binding", Types.VARCHAR, "varchar")
                        .execute(connection, rs -> rs.getString(1));
            }
            assertNull(selected);
        }
    }

    public static class BindObject {

        @Test
        public void value() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultObject(":binding", expected)
                        .execute(connection, rs -> rs.getObject(1).toString());
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueNull() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultObject(":binding", null)
                        .execute(connection, rs -> rs.getString(1));
            }
            assertNull(selected);
        }

        @Test
        public void valueType() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultObject(":binding", expected, Types.VARCHAR)
                        .execute(connection, rs -> rs.getObject(1).toString());
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueNullType() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultObject(":binding", null, Types.VARCHAR)
                        .execute(connection, rs -> rs.getString(1));
            }
            assertNull(selected);
        }

        @Test
        public void valueTypeLength() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultObject(":binding", expected, Types.VARCHAR, expected.length())
                        .execute(connection, rs -> rs.getObject(1).toString());
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueNullTypeLength() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultObject(":binding", null, Types.VARCHAR, 5)
                        .execute(connection, rs -> rs.getString(1));
            }
            assertNull(selected);
        }

        @Ignore
        @Test
        public void valueSQLType() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultObject(":binding", expected, JDBCType.VARCHAR)
                        .execute(connection, rs -> rs.getObject(1).toString());
            }
            assertEquals(expected, selected);
        }

        @Ignore
        @Test
        public void valueNullSQLType() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultObject(":binding", null, JDBCType.VARCHAR)
                        .execute(connection, rs -> rs.getString(1));
            }
            assertNull(selected);
        }

        @Ignore //not supported in h2
        @Test
        public void valueSQLTypeLength() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultObject(":binding", expected, JDBCType.VARCHAR, expected.length())
                        .execute(connection, rs -> rs.getObject(1).toString());
            }
            assertEquals(expected, selected);
        }

        @Ignore //not supported in h2
        @Test
        public void valueNullSQLTypeLength() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultObject(":binding", null, JDBCType.VARCHAR, 5)
                        .execute(connection, rs -> rs.getString(1));
            }
            assertNull(selected);
        }
    }

    @Ignore //not supported in h2
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
                        .bindDefaultRef(":binding", expected.get())
                        .execute(connection, rs -> rs.getRef(1));
                assertEquals(expected.get(), selected);
            }
        }

        @Test
        public void valueNull() throws Exception {
            try (Connection connection = db.getConnection()) {
                final Ref selected = new TestBuilder()
                        .bindDefaultRef(":binding", null)
                        .execute(connection, rs -> rs.getRef(1));
                assertNull(selected);
            }
        }
    }

    public static class BindShort {
        @Test
        public void value() throws Exception {
            final Short expected = 12;

            final Short selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultShort(":binding", expected)
                        .execute(connection, rs -> rs.getShort(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void Null() throws Exception {
            final Short selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultShort(":binding", null)
                        .execute(connection, rs -> rs.getShort(1));
            }
            assertNull(selected);
        }
    }

    public static class BindShortPrimitive {
        @Test
        public void value() throws Exception {
            final short expected = 12;

            final short selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultShortPrimitive(":binding", expected)
                        .execute(connection, rs -> rs.getShortPrimitive(1));
            }
            assertEquals(expected, selected);
        }
    }

    public static class BindString {

        @Test
        public void value() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultString(":binding", expected)
                        .execute(connection, rs -> rs.getString(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueNull() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultString(":binding", null)
                        .execute(connection, rs -> rs.getString(1));
            }
            assertNull(selected);
        }
    }

    @Ignore //not supported in h2
    public static class BindSQLXML {

        @Test
        public void value() throws Exception {
            final String expected = "<a></a>";

            final String selected;
            try (Connection connection = db.getConnection()) {
                final SQLXML xml = connection.createSQLXML();
                xml.setString(expected);
                selected = new TestBuilder()
                        .bindDefaultSQLXML(":binding", xml)
                        .execute(connection, rs -> rs.getSQLXML(1).getString());
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueNull() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultSQLXML(":binding", null)
                        .execute(connection, rs -> {
                            final SQLXML xml = rs.getSQLXML(1);
                            return xml == null ? null : xml.getString();
                        });
            }
            assertNull(selected);
        }
    }

    public static class BindTime {

        private final long expectedTime;

        public BindTime() {
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
                        .bindDefaultTime(":binding", expected)
                        .execute(connection, rs -> rs.getTime(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueNull() throws Exception {
            final Time selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultDate(":binding", null)
                        .execute(connection, rs -> rs.getTime(1));
            }
            assertNull(selected);
        }

        @Test
        public void valueCalendar() throws Exception {
            final Time expected = new Time(expectedTime);

            final Time selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultTime(":binding", expected, GregorianCalendar.getInstance())
                        .execute(connection, rs -> rs.getTime(1, GregorianCalendar.getInstance()));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueCalendarNull() throws Exception {
            final Time selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultDate(":binding", null, GregorianCalendar.getInstance())
                        .execute(connection, rs -> rs.getTime(1, GregorianCalendar.getInstance()));
            }
            assertNull(selected);
        }

    }

    public static class BindTimestamp extends HasExpectedTime {

        @Test
        public void value() throws Exception {
            final Timestamp expected = new Timestamp(expectedTime);

            final Timestamp selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultTimestamp(":binding", expected)
                        .execute(connection, rs -> rs.getTimestamp(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueNull() throws Exception {
            final Timestamp selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultTimestamp(":binding", null)
                        .execute(connection, rs -> rs.getTimestamp(1));
            }
            assertNull(selected);
        }

        @Test
        public void valueCalendar() throws Exception {
            final Timestamp expected = new Timestamp(expectedTime);

            final Timestamp selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultTimestamp(":binding", expected, GregorianCalendar.getInstance())
                        .execute(connection, rs -> rs.getTimestamp(1, GregorianCalendar.getInstance()));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueCalendarNull() throws Exception {
            final Timestamp selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultTimestamp(":binding", null, GregorianCalendar.getInstance())
                        .execute(connection, rs -> rs.getTimestamp(1, GregorianCalendar.getInstance()));
            }
            assertNull(selected);
        }

    }

    @Ignore //not supported in h2
    public static class BindURL {

        @Test
        public void value() throws Exception {
            final URL expected = new URL("http", "google.com", 8080, "/");

            final URL selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultURL(":binding", expected)
                        .execute(connection, rs -> rs.getURL(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueNull() throws Exception {
            final URL selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultURL(":binding", null)
                        .execute(connection, rs -> rs.getURL(1));
            }
            assertNull(selected);
        }
    }

    private static class TestBuilder extends PositionalBindingsBuilder<TestBuilder> {

        TestBuilder() {
            this(DefaultValueBindingsBuilderTest.statement, PositionalBindings.empty());
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