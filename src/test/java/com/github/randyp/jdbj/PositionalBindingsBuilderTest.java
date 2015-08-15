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
import java.sql.Date;
import java.util.*;

import static org.junit.Assert.*;

@RunWith(Enclosed.class)
public class PositionalBindingsBuilderTest {

    @ClassRule
    public static final H2Rule db = new H2Rule();

    private static final NamedParameterStatement statement =
            NamedParameterStatement.make("SELECT :binding as bound");

    public static class BindingNotFound {
        @Test(expected = IllegalArgumentException.class)
        public void value() throws Exception {
            new TestBuilder().bind(":not_the_mama", pc -> pc.setInt(1));
        }

        @Test(expected = IllegalArgumentException.class)
        public void list() throws Exception {
            new TestBuilder().bindList(":not_the_mama", new ArrayList<>());
        }
    }

    public static class CheckBinding {
        @Test
        public void value() throws Exception {
            new TestBuilder().bindList(":binding", new ArrayList<>()).checkAllBindingsPresent();
        }

        @Test
        public void list() throws Exception {
            new TestBuilder().bindList(":binding", new ArrayList<>()).checkAllBindingsPresent();
        }

        @Test(expected = IllegalStateException.class)
        public void missing() throws Exception {
            new TestBuilder().checkAllBindingsPresent();
        }
    }

    public static class Bind {
        @Test
        public void bind() throws Exception {
            final String expected = "1";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bind(":binding", pc -> pc.setString(expected))
                        .execute(connection, rs -> rs.getString(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueNotSet() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bind(":binding", pc -> {
                        })
                        .execute(connection, rs -> rs.getString(1));
            }
            assertNull(selected);
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullBinding() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bind(":binding", null)
                        .execute(connection, rs -> rs.getString(1));
            }
            assertNull(selected);
        }
    }

    public static class BindList {
        @Test
        public void valueInListNotSet() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindList(":binding", Collections.singletonList(pc -> {
                        }))
                        .execute(connection, rs -> rs.getString(1));
            }
            assertNull(selected);
        }
    }

    @Ignore //not supported in h2
    public static class array { //lower case so we don't capture java.sql.Array class

        @Test
        public void bind() throws Exception {
            final String[] expected = {"a", "b", "c"};

            final String[] selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindArray(":binding", connection.createArrayOf("varchar", expected))
                        .execute(connection, rs -> (String[]) rs.getArray(1).getArray());
            }
            assertArrayEquals(expected, selected);
        }

        @Test
        public void bindNull() throws Exception {
            final String[] selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindArray(":binding", null)
                        .execute(connection, rs -> {
                            final Array array = rs.getArray(1);
                            return array == null ? null : (String[]) array.getArray();
                        });
            }
            assertNull(selected);
        }
    }

    public static class AsciiStream {
        @Test
        public void inputStream() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindAsciiStream(":binding", new ByteArrayInputStream(expected.getBytes(Charset.forName("ascii"))))
                        .execute(connection, rs -> rs.getString(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void inputStreamNull() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindAsciiStream(":binding", null)
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
                        .bindAsciiStream(":binding", new ByteArrayInputStream(expected.getBytes(Charset.forName("ascii"))), expected.length())
                        .execute(connection, rs -> rs.getString(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void inputStreamLengthNull() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindAsciiStream(":binding", null, 5)
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
                        .bindAsciiStream(":binding", new ByteArrayInputStream(expected.getBytes(Charset.forName("ascii"))), (long) expected.length())
                        .execute(connection, rs -> rs.getString(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void inputStreamLengthLongNull() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindAsciiStream(":binding", null, 5L)
                        .execute(connection, rs -> rs.getString(1));
            }
            assertNull(selected);
        }
    }

    public static class bigdecimal {
        @Test
        public void value() throws Exception {
            final BigDecimal expected = new BigDecimal("1.234");

            final BigDecimal selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindBigDecimal(":binding", expected)
                        .execute(connection, rs -> rs.getBigDecimal(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void nullValue() throws Exception {
            final BigDecimal selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindBigDecimal(":binding", null)
                        .execute(connection, rs -> rs.getBigDecimal(1));
            }
            assertNull(selected);
        }
    }

    public static class BinaryStream {

        @Test
        public void inputStream() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindBinaryStream(":binding", new ByteArrayInputStream(expected.getBytes()))
                        .execute(connection, rs -> new String(rs.getBytes(1)));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void inputStreamNull() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindBinaryStream(":binding", null)
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
                        .bindBinaryStream(":binding", new ByteArrayInputStream(expected.getBytes()), 5)
                        .execute(connection, rs -> new String(rs.getBytes(1)));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void inputLengthStreamNull() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindBinaryStream(":binding", null, 5)
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
                        .bindBinaryStream(":binding", new ByteArrayInputStream(expected.getBytes()), 5L)
                        .execute(connection, rs -> new String(rs.getBytes(1)));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void inputLengthLongStreamNull() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindBinaryStream(":binding", null, 5L)
                        .execute(connection, rs -> {
                            final byte[] bytes = rs.getBytes(1);
                            return bytes == null ? null : new String(bytes);
                        });
            }
            assertNull(selected);
        }
    }

    public static class blob {

        @Test
        public void value() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                final Blob blob = connection.createBlob();
                blob.setBytes(1, expected.getBytes());
                selected = new TestBuilder()
                        .bindBlob(":binding", blob)
                        .execute(connection, rs -> new String(rs.getBytes(1)));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueNull() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindBlob(":binding", (Blob) null)
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
                        .bindBlob(":binding", new ByteArrayInputStream(expected.getBytes()))
                        .execute(connection, rs -> new String(rs.getBytes(1)));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void inputStreamNull() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindBlob(":binding", (InputStream) null)
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
                        .bindBlob(":binding", new ByteArrayInputStream(expected.getBytes()), (long) expected.length())
                        .execute(connection, rs -> new String(rs.getBytes(1)));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void inputStreamNullLength() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindBlob(":binding", null, 5L)
                        .execute(connection, rs -> {
                            final byte[] bytes = rs.getBytes(1);
                            return bytes == null ? null : new String(bytes);
                        });
            }
            assertNull(selected);
        }
    }

    public static class Bool {

        @Test
        public void False() throws Exception {

            final boolean selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindBoolean(":binding", false)
                        .execute(connection, rs -> rs.getBoolean(1));
            }
            assertFalse(selected);
        }

        @Test
        public void True() throws Exception {
            final boolean selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindBoolean(":binding", true)
                        .execute(connection, rs -> rs.getBoolean(1));
            }
            assertTrue(selected);
        }
    }

    public static class Byte {

        @Test
        public void value() throws Exception {
            final byte expected = 6;
            final byte selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindByte(":binding", expected)
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
                        .bindBytes(":binding", expected.getBytes())
                        .execute(connection, rs -> new String(rs.getBytes(1)));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueNull() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindBinaryStream(":binding", null)
                        .execute(connection, rs -> {
                            final byte[] bytes = rs.getBytes(1);
                            return bytes == null ? null : new String(bytes);
                        });
            }
            assertNull(selected);
        }
    }

    public static class CharacterStream {
        @Test
        public void reader() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindCharacterStream(":binding", new StringReader(expected))
                        .execute(connection, rs -> rs.getString(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void readerNull() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindCharacterStream(":binding", null)
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
                        .bindCharacterStream(":binding", new StringReader(expected), 5)
                        .execute(connection, rs -> rs.getString(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void readerLengthNull() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindCharacterStream(":binding", null, 5)
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
                        .bindCharacterStream(":binding", new StringReader(expected), 5L)
                        .execute(connection, rs -> rs.getString(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void readerLengthLongNull() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindCharacterStream(":binding", null, 5L)
                        .execute(connection, rs -> rs.getString(1));
            }
            assertNull(selected);
        }
    }


    public static class clob {

        @Test
        public void value() throws Exception {
            final String expected = "abcd";

            final String selected;
            try (Connection connection = db.getConnection()) {
                final Clob clob = connection.createClob();
                clob.setString(1, expected);
                selected = new TestBuilder()
                        .bindClob(":binding", clob)
                        .execute(connection, rs -> rs.getClob(1).getSubString(1L, expected.length()));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueNull() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindClob(":binding", (Clob) null)
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
                        .bindClob(":binding", new StringReader(expected))
                        .execute(connection, rs -> rs.getClob(1).getSubString(1L, expected.length()));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void readerNull() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindClob(":binding", (Reader) null)
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
                        .bindClob(":binding", new StringReader(expected), (long) expected.length())
                        .execute(connection, rs -> rs.getClob(1).getSubString(1L, expected.length()));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void readerLengthNull() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindClob(":binding", null, 4L)
                        .execute(connection, rs -> {
                            final byte[] bytes = rs.getBytes(1);
                            return bytes == null ? null : new String(bytes);
                        });
            }
            assertNull(selected);
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
                        .bindDate(":binding", expected)
                        .execute(connection, rs -> rs.getDate(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueNull() throws Exception {
            final Date selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDate(":binding", null)
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
                        .bindDate(":binding", expected, GregorianCalendar.getInstance())
                        .execute(connection, rs -> rs.getDate(1, GregorianCalendar.getInstance()));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueCalendarNull() throws Exception {
            final Date selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDate(":binding", null, GregorianCalendar.getInstance())
                        .execute(connection, rs -> rs.getDate(1, GregorianCalendar.getInstance()));
            }
            assertNull(selected);
        }

    }

    public static class BindDouble {
        @Test
        public void value() throws Exception {
            final double expected = 1.2;

            final double selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDouble(":binding", expected)
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
                        .bindFloat(":binding", expected)
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
                        .bindInt(":binding", expected)
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
                        .bindLong(":binding", expected)
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
                        .bindNCharacterStream(":binding", new StringReader(expected))
                        .execute(connection, rs -> rs.getString(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void readerNull() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindNCharacterStream(":binding", null)
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
                        .bindNCharacterStream(":binding", new StringReader(expected), 5L)
                        .execute(connection, rs -> rs.getString(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void readerNullLength() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindNCharacterStream(":binding", null, 5L)
                        .execute(connection, rs -> rs.getString(1));
            }
            assertNull(selected);
        }
    }

    public static class nclob {

        @Test
        public void value() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                final NClob nClob = connection.createNClob();
                nClob.setString(1L, expected);
                selected = new TestBuilder()
                        .bindNClob(":binding", nClob)
                        .execute(connection, rs -> rs.getNClob(1).getSubString(1L, expected.length()));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueNull() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindNClob(":binding", (NClob) null)
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
                        .bindNClob(":binding", new StringReader(expected))
                        .execute(connection, rs -> rs.getString(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void readerNull() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindNClob(":binding", (Reader) null)
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
                        .bindNClob(":binding", new StringReader(expected), (long) expected.length())
                        .execute(connection, rs -> rs.getString(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void readerNullLength() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindNClob(":binding", null, 5L)
                        .execute(connection, rs -> rs.getString(1));
            }
            assertNull(selected);
        }
    }

    public static class nstring {

        @Test
        public void value() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindNString(":binding", expected)
                        .execute(connection, rs -> rs.getNString(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueNull() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindNString(":binding", null)
                        .execute(connection, rs -> rs.getString(1));
            }
            assertNull(selected);
        }
    }

    public static class Null {
        @Test
        public void type() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindNull(":binding", Types.VARCHAR)
                        .execute(connection, rs -> rs.getString(1));
            }
            assertNull(selected);
        }

        @Test
        public void typeAndName() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindNull(":binding", Types.VARCHAR, "varchar")
                        .execute(connection, rs -> rs.getString(1));
            }
            assertNull(selected);
        }
    }

    public static class object {

        @Test
        public void value() throws Exception {
            final String expected = "abcde";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindObject(":binding", expected)
                        .execute(connection, rs -> rs.getObject(1).toString());
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueNull() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindObject(":binding", null)
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
                        .bindObject(":binding", expected, Types.VARCHAR)
                        .execute(connection, rs -> rs.getObject(1).toString());
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueNullType() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindObject(":binding", null, Types.VARCHAR)
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
                        .bindObject(":binding", expected, Types.VARCHAR, expected.length())
                        .execute(connection, rs -> rs.getObject(1).toString());
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueNullTypeLength() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindObject(":binding", null, Types.VARCHAR, 5)
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
                        .bindObject(":binding", expected, JDBCType.VARCHAR)
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
                        .bindObject(":binding", null, JDBCType.VARCHAR)
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
                        .bindObject(":binding", expected, JDBCType.VARCHAR, expected.length())
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
                        .bindObject(":binding", null, JDBCType.VARCHAR, 5)
                        .execute(connection, rs -> rs.getString(1));
            }
            assertNull(selected);
        }
    }

    @Ignore //not supported in h2
    public static class ref {

        @Test
        public void value() throws Exception {
            try (Connection connection = db.getConnection()) {
                final Ref expected = JDBJ.string("SELECT (SELECT * FROM INFORMATION_SCHEMA.TABLES LIMIT 1) AS ref").query()
                        .map(rs -> rs.getRef(1))
                        .first()
                        .execute(connection);
                final Ref selected = new TestBuilder()
                        .bindRef(":binding", expected)
                        .execute(connection, rs -> rs.getRef(1));
                assertEquals(expected, selected);
            }
        }

        @Test
        public void valueNull() throws Exception {
            try (Connection connection = db.getConnection()) {
                final Ref selected = new TestBuilder()
                        .bindRef(":binding", null)
                        .execute(connection, rs -> rs.getRef(1));
                assertNull(selected);
            }
        }
    }

    public static class Short {
        @Test
        public void value() throws Exception {
            final short expected = 12;

            final short selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindShort(":binding", expected)
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
                        .bindString(":binding", expected)
                        .execute(connection, rs -> rs.getString(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueNull() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindString(":binding", null)
                        .execute(connection, rs -> rs.getString(1));
            }
            assertNull(selected);
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
                        .bindSQLXML(":binding", xml)
                        .execute(connection, rs -> rs.getSQLXML(1).getString());
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueNull() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindSQLXML(":binding", null)
                        .execute(connection, rs -> {
                            final SQLXML xml = rs.getSQLXML(1);
                            return xml == null ? null : xml.getString();
                        });
            }
            assertNull(selected);
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
                        .bindTime(":binding", expected)
                        .execute(connection, rs -> rs.getTime(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueNull() throws Exception {
            final Time selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDate(":binding", null)
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
                        .bindTime(":binding", expected, GregorianCalendar.getInstance())
                        .execute(connection, rs -> rs.getTime(1, GregorianCalendar.getInstance()));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueCalendarNull() throws Exception {
            final Time selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDate(":binding", null, GregorianCalendar.getInstance())
                        .execute(connection, rs -> rs.getTime(1, GregorianCalendar.getInstance()));
            }
            assertNull(selected);
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
                        .bindTimestamp(":binding", expected)
                        .execute(connection, rs -> rs.getTimestamp(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueNull() throws Exception {
            final Timestamp selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindTimestamp(":binding", null)
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
                        .bindTimestamp(":binding", expected, GregorianCalendar.getInstance())
                        .execute(connection, rs -> rs.getTimestamp(1, GregorianCalendar.getInstance()));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueCalendarNull() throws Exception {
            final Timestamp selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindTimestamp(":binding", null, GregorianCalendar.getInstance())
                        .execute(connection, rs -> rs.getTimestamp(1, GregorianCalendar.getInstance()));
            }
            assertNull(selected);
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
                        .bindURL(":binding", expected)
                        .execute(connection, rs -> rs.getURL(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueNull() throws Exception {
            final URL selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindURL(":binding", null)
                        .execute(connection, rs -> rs.getURL(1));
            }
            assertNull(selected);
        }
    }

    public static class strings {
        @Test
        public void values() throws Exception {
            final String[] expected = {"abc", "def"};

            final Object[] selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindStrings(":binding", Arrays.asList(expected))
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
            }
            assertArrayEquals(expected, selected);
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullArray() throws Exception {
            try (Connection connection = db.getConnection()) {
                new TestBuilder()
                        .bindStrings(":binding", (String[]) null)
                        .execute(connection, rs -> rs.getObject(1));
            }
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullList() throws Exception {
            try (Connection connection = db.getConnection()) {
                new TestBuilder()
                        .bindStrings(":binding", (List<String>) null)
                        .execute(connection, rs -> rs.getObject(1));
            }
        }
    }

    public static class longs {
        @Test
        public void values() throws Exception {
            final long[] input = {152L, 51L};
            final Long[] expected = {152L, 51L};

            final Object[] selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindLongs(":binding", input)
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
            }
            assertArrayEquals(expected, selected);
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullArray() throws Exception {
            try (Connection connection = db.getConnection()) {
                new TestBuilder()
                        .bindLongs(":binding", (long[]) null)
                        .execute(connection, rs -> rs.getObject(1));
            }
        }
    }

    private static class TestBuilder extends PositionalBindingsBuilder<TestBuilder> {

        TestBuilder() {
            this(PositionalBindingsBuilderTest.statement, PositionalBindings.empty());
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