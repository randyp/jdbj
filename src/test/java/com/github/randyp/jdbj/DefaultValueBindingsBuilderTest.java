package com.github.randyp.jdbj;

import com.github.randyp.jdbj.db.h2_1_4.H2Rule;
import com.github.randyp.jdbj.db.postgres_9_4.PGRule;
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
import java.util.Collections;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;

@RunWith(Enclosed.class)
public class DefaultValueBindingsBuilderTest {

    public static class BindDefault {
        @Test(expected = IllegalArgumentException.class)
        public void nullName() throws Exception {
            new SimpleBuilder().bindDefault(null, pc -> pc.setString("hi"));
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullBinding() throws Exception {
            new SimpleBuilder().bindDefault(":binding", null);
        }

        @Test(expected = IllegalArgumentException.class)
        public void bindingNotInStatement() throws Exception {
            new SimpleBuilder().bindDefault(":not_binding", pc->pc.setString("hi"));
        }
    }

    public static class BindArray {

        @ClassRule
        public static PGRule db = new PGRule();

        @Test
        public void bind() throws Exception {
            final String[] expected = {"a", "b", "c"};
            try (Connection connection = db.getConnection()) {
                final String[] selected = new SimpleBuilder("varchar[]")
                        .bindDefaultArray(":binding", connection.createArrayOf("varchar", expected))
                        .execute(connection, rs -> (String[]) rs.getSQLArray(1).getArray());
                assertArrayEquals(expected, selected);
            }
        }

        @Test
        public void bindNull() throws Exception {
            try (Connection connection = db.getConnection()) {
                final String[] selected = new SimpleBuilder("varchar[]")
                        .bindDefaultArray(":binding", null)
                        .execute(connection, rs -> {
                            final Array array = rs.getSQLArray(1);
                            return array == null ? null : (String[]) array.getArray();
                        });
                assertNull(selected);
            }
        }
    }

    public static class BindAsciiStream {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void inputStream() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultAsciiStream(":binding", new ByteArrayInputStream(expected.getBytes(Charset.forName("ascii"))))
                        .execute(connection, rs -> rs.getString(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void inputStreamNull() throws Exception {
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultAsciiStream(":binding", null)
                        .execute(connection, rs -> rs.getString(1));
                assertNull(selected);
            }
        }

        @Test
        public void inputStreamLength() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultAsciiStream(":binding", new ByteArrayInputStream(expected.getBytes(Charset.forName("ascii"))), expected.length())
                        .execute(connection, rs -> rs.getString(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void inputStreamLengthNull() throws Exception {
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultAsciiStream(":binding", null, 5)
                        .execute(connection, rs -> rs.getString(1));
                assertNull(selected);
            }
        }

        @Test
        public void inputStreamLengthLong() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultAsciiStream(":binding", new ByteArrayInputStream(expected.getBytes(Charset.forName("ascii"))), (long) expected.length())
                        .execute(connection, rs -> rs.getString(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void inputStreamLengthLongNull() throws Exception {
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultAsciiStream(":binding", null, 5L)
                        .execute(connection, rs -> rs.getString(1));
                assertNull(selected);
            }
        }
    }

    public static class BindBigDecimal {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void value() throws Exception {
            final BigDecimal expected = new BigDecimal("1.234");
            try (Connection connection = db.getConnection()) {
                final BigDecimal selected = new SimpleBuilder()
                        .bindDefaultBigDecimal(":binding", expected)
                        .execute(connection, rs -> rs.getBigDecimal(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void nullValue() throws Exception {
            try (Connection connection = db.getConnection()) {
                final BigDecimal selected = new SimpleBuilder()
                        .bindDefaultBigDecimal(":binding", null)
                        .execute(connection, rs -> rs.getBigDecimal(1));
                assertNull(selected);
            }
        }
    }

    public static class BindBinaryStream {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void inputStream() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultBinaryStream(":binding", new ByteArrayInputStream(expected.getBytes()))
                        .execute(connection, rs -> new String(rs.getBytes(1)));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void inputStreamNull() throws Exception {
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultBinaryStream(":binding", null)
                        .execute(connection, rs -> {
                            final byte[] bytes = rs.getBytes(1);
                            return bytes == null ? null : new String(bytes);
                        });
                assertNull(selected);
            }
        }

        @Test
        public void inputLengthStream() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultBinaryStream(":binding", new ByteArrayInputStream(expected.getBytes()), 5)
                        .execute(connection, rs -> new String(rs.getBytes(1)));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void inputLengthStreamNull() throws Exception {
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultBinaryStream(":binding", null, 5)
                        .execute(connection, rs -> {
                            final byte[] bytes = rs.getBytes(1);
                            return bytes == null ? null : new String(bytes);
                        });
                assertNull(selected);
            }
        }

        @Test
        public void inputLengthLongStream() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultBinaryStream(":binding", new ByteArrayInputStream(expected.getBytes()), 5L)
                        .execute(connection, rs -> new String(rs.getBytes(1)));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void inputLengthLongStreamNull() throws Exception {
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultBinaryStream(":binding", null, 5L)
                        .execute(connection, rs -> {
                            final byte[] bytes = rs.getBytes(1);
                            return bytes == null ? null : new String(bytes);
                        });
                assertNull(selected);
            }
        }
    }

    public static class BindBlob {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void value() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final Blob blob = connection.createBlob();
                blob.setBytes(1, expected.getBytes());
                final String selected = new SimpleBuilder()
                        .bindDefaultBlob(":binding", blob)
                        .execute(connection, rs -> new String(rs.getBytes(1)));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void valueNull() throws Exception {
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultBlob(":binding", (Blob) null)
                        .execute(connection, rs -> {
                            final byte[] bytes = rs.getBytes(1);
                            return bytes == null ? null : new String(bytes);
                        });
                assertNull(selected);
            }
        }

        @Test
        public void inputStream() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultBlob(":binding", new ByteArrayInputStream(expected.getBytes()))
                        .execute(connection, rs -> new String(rs.getBytes(1)));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void inputStreamNull() throws Exception {
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultBlob(":binding", (InputStream) null)
                        .execute(connection, rs -> {
                            final byte[] bytes = rs.getBytes(1);
                            return bytes == null ? null : new String(bytes);
                        });
                assertNull(selected);
            }
        }

        @Test
        public void inputStreamLength() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultBlob(":binding", new ByteArrayInputStream(expected.getBytes()), (long) expected.length())
                        .execute(connection, rs -> new String(rs.getBytes(1)));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void inputStreamNullLength() throws Exception {
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultBlob(":binding", null, 5L)
                        .execute(connection, rs -> {
                            final byte[] bytes = rs.getBytes(1);
                            return bytes == null ? null : new String(bytes);
                        });
                assertNull(selected);
            }
        }
    }

    public static class BindBoolean {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void False() throws Exception {
            try (Connection connection = db.getConnection()) {
                final Boolean selected = new SimpleBuilder()
                        .bindDefaultBoolean(":binding", Boolean.FALSE)
                        .execute(connection, rs -> rs.getBoolean(1));
                assertFalse(selected);
            }
        }

        @Test
        public void True() throws Exception {
            try (Connection connection = db.getConnection()) {
                final Boolean selected = new SimpleBuilder()
                        .bindDefaultBoolean(":binding", true)
                        .execute(connection, rs -> rs.getBoolean(1));
                assertTrue(selected);
            }
        }

        @Test
        public void Null() throws Exception {
            try (Connection connection = db.getConnection()) {
                final Boolean selected = new SimpleBuilder()
                        .bindDefaultBoolean(":binding", null)
                        .execute(connection, rs -> rs.getBoolean(1));
                assertNull(selected);
            }
        }
    }

    public static class BindBooleanPrimitive {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void False() throws Exception {
            try (Connection connection = db.getConnection()) {
                final boolean selected = new SimpleBuilder()
                        .bindDefaultBooleanPrimitive(":binding", false)
                        .execute(connection, rs -> rs.getBooleanPrimitive(1));
                assertFalse(selected);
            }
        }

        @Test
        public void True() throws Exception {
            try (Connection connection = db.getConnection()) {
                final boolean selected = new SimpleBuilder()
                        .bindDefaultBooleanPrimitive(":binding", true)
                        .execute(connection, rs -> rs.getBooleanPrimitive(1));
                assertTrue(selected);
            }
        }
    }

    public static class BindByte {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void value() throws Exception {
            final Byte expected = 6;
            try (Connection connection = db.getConnection()) {
                final Byte selected = new SimpleBuilder()
                        .bindDefaultByte(":binding", expected)
                        .execute(connection, rs -> rs.getByte(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void Null() throws Exception {
            try (Connection connection = db.getConnection()) {
                final Byte selected = new SimpleBuilder()
                        .bindDefaultByte(":binding", null)
                        .execute(connection, rs -> rs.getByte(1));
                assertNull(selected);
            }
        }
    }

    public static class BindBytePrimitive {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void value() throws Exception {
            final byte expected = 6;
            try (Connection connection = db.getConnection()) {
                final byte selected = new SimpleBuilder()
                        .bindDefaultBytePrimtive(":binding", expected)
                        .execute(connection, rs -> rs.getBytePrimitive(1));
                assertEquals(expected, selected);
            }
        }
    }

    public static class BindBytes {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void value() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultByteArray(":binding", expected.getBytes())
                        .execute(connection, rs -> new String(rs.getBytes(1)));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void valueNull() throws Exception {
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultBinaryStream(":binding", null)
                        .execute(connection, rs -> {
                            final byte[] bytes = rs.getBytes(1);
                            return bytes == null ? null : new String(bytes);
                        });
                assertNull(selected);
            }
        }
    }

    public static class BindCharacterStream {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void reader() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultCharacterStream(":binding", new StringReader(expected))
                        .execute(connection, rs -> rs.getString(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void readerNull() throws Exception {
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultCharacterStream(":binding", null)
                        .execute(connection, rs -> rs.getString(1));
                assertNull(selected);
            }
        }

        @Test
        public void readerLength() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultCharacterStream(":binding", new StringReader(expected), 5)
                        .execute(connection, rs -> rs.getString(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void readerLengthNull() throws Exception {
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultCharacterStream(":binding", null, 5)
                        .execute(connection, rs -> rs.getString(1));
                assertNull(selected);
            }
        }

        @Test
        public void readerLengthLong() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultCharacterStream(":binding", new StringReader(expected), 5L)
                        .execute(connection, rs -> rs.getString(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void readerLengthLongNull() throws Exception {
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultCharacterStream(":binding", null, 5L)
                        .execute(connection, rs -> rs.getString(1));
                assertNull(selected);
            }
        }
    }

    public static class BindClob {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void value() throws Exception {
            final String expected = "abcd";
            try (Connection connection = db.getConnection()) {
                final Clob clob = connection.createClob();
                clob.setString(1, expected);
                final String selected = new SimpleBuilder()
                        .bindDefaultClob(":binding", clob)
                        .execute(connection, rs -> rs.getClob(1).getSubString(1L, expected.length()));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void valueNull() throws Exception {
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultClob(":binding", (Clob) null)
                        .execute(connection, rs -> {
                            final byte[] bytes = rs.getBytes(1);
                            return bytes == null ? null : new String(bytes);
                        });
                assertNull(selected);
            }
        }

        @Test
        public void reader() throws Exception {
            final String expected = "abcd";
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultClob(":binding", new StringReader(expected))
                        .execute(connection, rs -> rs.getClob(1).getSubString(1L, expected.length()));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void readerNull() throws Exception {
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultClob(":binding", (Reader) null)
                        .execute(connection, rs -> {
                            final byte[] bytes = rs.getBytes(1);
                            return bytes == null ? null : new String(bytes);
                        });
                assertNull(selected);
            }
        }

        @Test
        public void readerLength() throws Exception {
            final String expected = "abcd";
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultClob(":binding", new StringReader(expected), (long) expected.length())
                        .execute(connection, rs -> rs.getClob(1).getSubString(1L, expected.length()));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void readerLengthNull() throws Exception {
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultClob(":binding", null, 4L)
                        .execute(connection, rs -> {
                            final byte[] bytes = rs.getBytes(1);
                            return bytes == null ? null : new String(bytes);
                        });
                assertNull(selected);
            }
        }
    }

    public static class BindDate extends HasExpectedTimeSinceEpoch {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void value() throws Exception {
            final Date expected = new Date(expectedTime);

            try (Connection connection = db.getConnection()) {
                final Date selected = new SimpleBuilder()
                        .bindDefaultDate(":binding", expected)
                        .execute(connection, rs -> rs.getDate(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void valueNull() throws Exception {
            try (Connection connection = db.getConnection()) {
                final Date selected = new SimpleBuilder()
                        .bindDefaultDate(":binding", null)
                        .execute(connection, rs -> rs.getDate(1));
                assertNull(selected);
            }
        }

        @Test
        public void valueCalendar() throws Exception {
            final Date expected = new Date(expectedTime);
            try (Connection connection = db.getConnection()) {
                final Date selected = new SimpleBuilder()
                        .bindDefaultDate(":binding", expected, GregorianCalendar.getInstance())
                        .execute(connection, rs -> rs.getDate(1, GregorianCalendar.getInstance()));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void valueCalendarNull() throws Exception {
            try (Connection connection = db.getConnection()) {
                final Date selected = new SimpleBuilder()
                        .bindDefaultDate(":binding", null, GregorianCalendar.getInstance())
                        .execute(connection, rs -> rs.getDate(1, GregorianCalendar.getInstance()));
                assertNull(selected);
            }
        }

    }

    public static class BindDouble {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void value() throws Exception {
            final Double expected = 1.2;
            try (Connection connection = db.getConnection()) {
                final Double selected = new SimpleBuilder()
                        .bindDefaultDouble(":binding", expected)
                        .execute(connection, rs -> rs.getDouble(1));
                assertEquals(expected, selected, 0.0);
            }
        }

        @Test
        public void Null() throws Exception {
            try (Connection connection = db.getConnection()) {
                final Double selected = new SimpleBuilder()
                        .bindDefaultDouble(":binding", null)
                        .execute(connection, rs -> rs.getDouble(1));
                assertNull(selected);
            }
        }
    }

    public static class BindDoublePrimitive {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void value() throws Exception {
            final double expected = 1.2;
            try (Connection connection = db.getConnection()) {
                final double selected = new SimpleBuilder()
                        .bindDefaultDoublePrimitive(":binding", expected)
                        .execute(connection, rs -> rs.getDoublePrimitive(1));
                assertEquals(expected, selected, 0.0);
            }
        }
    }

    public static class BindFloat {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void value() throws Exception {
            final Float expected = 1.2f;
            try (Connection connection = db.getConnection()) {
                final Float selected = new SimpleBuilder()
                        .bindDefaultFloat(":binding", expected)
                        .execute(connection, rs -> rs.getFloat(1));
                assertEquals(expected, selected, 0.0f);
            }
        }

        @Test
        public void Null() throws Exception {
            try (Connection connection = db.getConnection()) {
                final Float selected = new SimpleBuilder()
                        .bindDefaultFloat(":binding", null)
                        .execute(connection, rs -> rs.getFloat(1));
                assertNull(selected);
            }
        }
    }

    public static class BindFloatPrimitive {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void value() throws Exception {
            final float expected = 1.2f;
            try (Connection connection = db.getConnection()) {
                final float selected = new SimpleBuilder()
                        .bindDefaultFloatPrimtitive(":binding", expected)
                        .execute(connection, rs -> rs.getFloatPrimitive(1));
                assertEquals(expected, selected, 0.0f);
            }
        }
    }

    public static class BindInteger {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void value() throws Exception {
            final Integer expected = 12;
            try (Connection connection = db.getConnection()) {
                final Integer selected = new SimpleBuilder()
                        .bindDefaultInteger(":binding", expected)
                        .execute(connection, rs -> rs.getInteger(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void Null() throws Exception {
            try (Connection connection = db.getConnection()) {
                final Integer selected = new SimpleBuilder()
                        .bindDefaultInteger(":binding", null)
                        .execute(connection, rs -> rs.getInteger(1));
                assertNull(selected);
            }
        }
    }

    public static class BindIntegerPrimitive {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void value() throws Exception {
            final int expected = 12;
            try (Connection connection = db.getConnection()) {
                final int selected = new SimpleBuilder()
                        .bindDefaultIntegerPrimitive(":binding", expected)
                        .execute(connection, rs -> rs.getIntegerPrimitive(1));
                assertEquals(expected, selected);
            }
        }
    }

    public static class BindInt {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void value() throws Exception {
            final int expected = 12;
            try (Connection connection = db.getConnection()) {
                final int selected = new SimpleBuilder()
                        .bindDefaultInt(":binding", expected)
                        .execute(connection, rs -> rs.getInt(1));
                assertEquals(expected, selected);
            }
        }
    }

    public static class BindLong {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void value() throws Exception {
            final Long expected = 12L;
            try (Connection connection = db.getConnection()) {
                final Long selected = new SimpleBuilder()
                        .bindDefaultLong(":binding", expected)
                        .execute(connection, rs -> rs.getLong(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void Null() throws Exception {
            try (Connection connection = db.getConnection()) {
                final Long selected = new SimpleBuilder()
                        .bindDefaultLong(":binding", null)
                        .execute(connection, rs -> rs.getLong(1));
                assertNull(selected);
            }
        }
    }

    public static class BindLongPrimitive {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void value() throws Exception {
            final long expected = 12L;
            try (Connection connection = db.getConnection()) {
                final long selected = new SimpleBuilder()
                        .bindDefaultLongPrimitive(":binding", expected)
                        .execute(connection, rs -> rs.getLongPrimitive(1));
                assertEquals(expected, selected);
            }
        }
    }

    public static class BindNCharacterStream {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void reader() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultNCharacterStream(":binding", new StringReader(expected))
                        .execute(connection, rs -> rs.getString(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void readerNull() throws Exception {
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultNCharacterStream(":binding", null)
                        .execute(connection, rs -> rs.getString(1));
                assertNull(selected);
            }
        }

        @Test
        public void readerLength() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultNCharacterStream(":binding", new StringReader(expected), 5L)
                        .execute(connection, rs -> rs.getString(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void readerNullLength() throws Exception {
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultNCharacterStream(":binding", null, 5L)
                        .execute(connection, rs -> rs.getString(1));
                assertNull(selected);
            }
        }
    }

    public static class BindNClob {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void value() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final NClob nClob = connection.createNClob();
                nClob.setString(1L, expected);
                final String selected = new SimpleBuilder()
                        .bindDefaultNClob(":binding", nClob)
                        .execute(connection, rs -> rs.getNClob(1).getSubString(1L, expected.length()));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void valueNull() throws Exception {
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultNClob(":binding", (NClob) null)
                        .execute(connection, rs -> rs.getString(1));
                assertNull(selected);
            }
        }

        @Test
        public void reader() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultNClob(":binding", new StringReader(expected))
                        .execute(connection, rs -> rs.getString(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void readerNull() throws Exception {
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultNClob(":binding", (Reader) null)
                        .execute(connection, rs -> rs.getString(1));
                assertNull(selected);
            }
        }

        @Test
        public void readerLength() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultNClob(":binding", new StringReader(expected), (long) expected.length())
                        .execute(connection, rs -> rs.getString(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void readerNullLength() throws Exception {
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultNClob(":binding", null, 5L)
                        .execute(connection, rs -> rs.getString(1));
                assertNull(selected);
            }
        }
    }

    public static class BindNString {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void value() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultNString(":binding", expected)
                        .execute(connection, rs -> rs.getNString(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void valueNull() throws Exception {
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultNString(":binding", null)
                        .execute(connection, rs -> rs.getString(1));
                assertNull(selected);
            }
        }
    }

    public static class BindNull {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void type() throws Exception {
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultNull(":binding", Types.VARCHAR)
                        .execute(connection, rs -> rs.getString(1));
                assertNull(selected);
            }
        }

        @Test
        public void typeAndName() throws Exception {
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultNull(":binding", Types.VARCHAR, "varchar")
                        .execute(connection, rs -> rs.getString(1));
                assertNull(selected);
            }
        }
    }

    public static class BindObject {

        @ClassRule
        public static final PGRule db = new PGRule();

        @Test
        public void value() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultObject(":binding", expected)
                        .execute(connection, rs -> rs.getObject(1).toString());
                assertEquals(expected, selected);
            }
        }

        @Test
        public void valueNull() throws Exception {
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultObject(":binding", null)
                        .execute(connection, rs -> rs.getString(1));
                assertNull(selected);
            }
        }

        @Test
        public void valueType() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultObject(":binding", expected, Types.VARCHAR)
                        .execute(connection, rs -> rs.getObject(1).toString());
                assertEquals(expected, selected);
            }
        }

        @Test
        public void valueNullType() throws Exception {
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultObject(":binding", null, Types.VARCHAR)
                        .execute(connection, rs -> rs.getString(1));
                assertNull(selected);
            }
        }

        @Test
        public void valueTypeLength() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultObject(":binding", expected, Types.VARCHAR, expected.length())
                        .execute(connection, rs -> rs.getObject(1).toString());
                assertEquals(expected, selected);
            }
        }

        @Test
        public void valueNullTypeLength() throws Exception {
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultObject(":binding", null, Types.VARCHAR, 5)
                        .execute(connection, rs -> rs.getString(1));
                assertNull(selected);
            }
        }

        @Test(expected = SQLFeatureNotSupportedException.class)
        public void valueSQLType() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultObject(":binding", expected, JDBCType.VARCHAR)
                        .execute(connection, rs -> rs.getObject(1).toString());
                assertEquals(expected, selected);
            }
        }

        @Test(expected = SQLFeatureNotSupportedException.class)
        public void valueNullSQLType() throws Exception {
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultObject(":binding", null, JDBCType.VARCHAR)
                        .execute(connection, rs -> rs.getString(1));
                assertNull(selected);
            }
        }

        @Test(expected = SQLFeatureNotSupportedException.class)
        public void valueSQLTypeLength() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultObject(":binding", expected, JDBCType.VARCHAR, expected.length())
                        .execute(connection, rs -> rs.getObject(1).toString());
                assertEquals(expected, selected);
            }
        }

        @Test(expected = SQLFeatureNotSupportedException.class)
        public void valueNullSQLTypeLength() throws Exception {
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultObject(":binding", null, JDBCType.VARCHAR, 5)
                        .execute(connection, rs -> rs.getString(1));
                assertNull(selected);
            }
        }
    }

    public static class BindShort {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void value() throws Exception {
            final Short expected = 12;
            try (Connection connection = db.getConnection()) {
                final Short selected = new SimpleBuilder()
                        .bindDefaultShort(":binding", expected)
                        .execute(connection, rs -> rs.getShort(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void Null() throws Exception {
            try (Connection connection = db.getConnection()) {
                final Short selected = new SimpleBuilder()
                        .bindDefaultShort(":binding", null)
                        .execute(connection, rs -> rs.getShort(1));
                assertNull(selected);
            }
        }

        @Test
        public void primitive() throws Exception {
            final short expected = 12;
            try (Connection connection = db.getConnection()) {
                final short selected = new SimpleBuilder()
                        .bindDefaultShortPrimitive(":binding", expected)
                        .execute(connection, rs -> rs.getShortPrimitive(1));
                assertEquals(expected, selected);
            }
        }
    }

    public static class BindString {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void value() throws Exception {
            final String expected = "abcde";
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultString(":binding", expected)
                        .execute(connection, rs -> rs.getString(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void valueNull() throws Exception {
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultString(":binding", null)
                        .execute(connection, rs -> rs.getString(1));
                assertNull(selected);
            }
        }
    }

    public static class BindSQLXML {

        @ClassRule
        public static final PGRule db = new PGRule();

        @Test
        public void value() throws Exception {
            final String expected = "<a></a>";
            try (Connection connection = db.getConnection()) {
                final SQLXML xml = connection.createSQLXML();
                xml.setString(expected);
                final String selected = new SimpleBuilder()
                        .bindDefaultSQLXML(":binding", xml)
                        .execute(connection, rs -> rs.getSQLXML(1).getString());
                assertEquals(expected, selected);
            }
        }

        @Test
        public void valueNull() throws Exception {
            try (Connection connection = db.getConnection()) {
                final String selected = new SimpleBuilder()
                        .bindDefaultSQLXML(":binding", null)
                        .execute(connection, rs -> {
                            final SQLXML xml = rs.getSQLXML(1);
                            return xml == null ? null : xml.getString();
                        });
                assertNull(selected);
            }
        }
    }

    public static class BindTime extends HasExpectedTimeOfDay {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void value() throws Exception {
            final Time expected = new Time(expectedTime);
            try (Connection connection = db.getConnection()) {
                final Time selected = new SimpleBuilder()
                        .bindDefaultTime(":binding", expected)
                        .execute(connection, rs -> rs.getTime(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void valueNull() throws Exception {
            try (Connection connection = db.getConnection()) {
                final Time selected = new SimpleBuilder()
                        .bindDefaultDate(":binding", null)
                        .execute(connection, rs -> rs.getTime(1));
                assertNull(selected);
            }
        }

        @Test
        public void valueCalendar() throws Exception {
            final Time expected = new Time(expectedTime);
            try (Connection connection = db.getConnection()) {
                final Time selected = new SimpleBuilder()
                        .bindDefaultTime(":binding", expected, GregorianCalendar.getInstance())
                        .execute(connection, rs -> rs.getTime(1, GregorianCalendar.getInstance()));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void valueCalendarNull() throws Exception {
            try (Connection connection = db.getConnection()) {
                final Time selected = new SimpleBuilder()
                        .bindDefaultDate(":binding", null, GregorianCalendar.getInstance())
                        .execute(connection, rs -> rs.getTime(1, GregorianCalendar.getInstance()));
                assertNull(selected);
            }
        }

    }

    public static class BindTimestamp extends HasExpectedTimeSinceEpoch {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void value() throws Exception {
            final Timestamp expected = new Timestamp(expectedTime);
            try (Connection connection = db.getConnection()) {
                final Timestamp selected = new SimpleBuilder()
                        .bindDefaultTimestamp(":binding", expected)
                        .execute(connection, rs -> rs.getTimestamp(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void valueNull() throws Exception {
            try (Connection connection = db.getConnection()) {
                final Timestamp selected = new SimpleBuilder()
                        .bindDefaultTimestamp(":binding", null)
                        .execute(connection, rs -> rs.getTimestamp(1));
                assertNull(selected);
            }
        }

        @Test
        public void valueCalendar() throws Exception {
            final Timestamp expected = new Timestamp(expectedTime);
            try (Connection connection = db.getConnection()) {
                final Timestamp selected = new SimpleBuilder()
                        .bindDefaultTimestamp(":binding", expected, GregorianCalendar.getInstance())
                        .execute(connection, rs -> rs.getTimestamp(1, GregorianCalendar.getInstance()));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void valueCalendarNull() throws Exception {
            try (Connection connection = db.getConnection()) {
                final Timestamp selected = new SimpleBuilder()
                        .bindDefaultTimestamp(":binding", null, GregorianCalendar.getInstance())
                        .execute(connection, rs -> rs.getTimestamp(1, GregorianCalendar.getInstance()));
                assertNull(selected);
            }
        }

    }

    public static class BindURL {

        @ClassRule
        public static final PGRule db = new PGRule();

        @Test(expected = SQLFeatureNotSupportedException.class)
        public void value() throws Exception {
            final URL expected = new URL("http", "google.com", 8080, "/");
            try (Connection connection = db.getConnection()) {
                final URL selected = new SimpleBuilder()
                        .bindDefaultURL(":binding", expected)
                        .execute(connection, rs -> rs.getURL(1));
                assertEquals(expected, selected);
            }
        }

        @Test(expected = SQLFeatureNotSupportedException.class)
        public void valueNull() throws Exception {
            try (Connection connection = db.getConnection()) {
                final URL selected = new SimpleBuilder()
                        .bindDefaultURL(":binding", null)
                        .execute(connection, rs -> rs.getURL(1));
                assertNull(selected);
            }
        }
    }
}