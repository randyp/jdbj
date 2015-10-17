package io.codemonastery.jdbj.jdbj;

import io.codemonastery.jdbj.jdbj.db.h2_1_4.H2Rule;
import io.codemonastery.jdbj.jdbj.db.postgres_9_4.PGRule;
import io.codemonastery.jdbj.jdbj.lambda.Binding;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import javax.sql.DataSource;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.*;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(Enclosed.class)
public class SmartResultSetTest {

    public static class GetArray {

        @ClassRule
        public static final PGRule db = new PGRule();

        @Test
        public void index() throws Exception {
            final Long[] expected = new Long[]{1L, 2L, 3L};
            final Binding binding = pc -> pc.setArray(pc.createArrayOf("bigint", expected));
            final ResultSetAssertions assertions = rs -> {
                final Long[] actual = rs.getArray(1);
                assertArrayEquals(expected, actual);
            };
            assertResults(binding, assertions, "bigint[]", db);
        }

        @Test
        public void label() throws Exception {
            final Long[] expected = new Long[]{1L, 2L, 3L};
            final Binding binding = pc -> pc.setArray(pc.createArrayOf("bigint", expected));
            final ResultSetAssertions assertions = rs -> {
                final Long[] actual = rs.getArray("bound");
                assertArrayEquals(expected, actual);
            };
            assertResults(binding, assertions, "bigint[]", db);
        }

        @Test
        public void indexNull() throws Exception {
            final Binding binding = pc -> pc.setArray(null);
            final ResultSetAssertions assertions = rs -> {
                final Long[] actual = rs.getArray(1);
                assertNull(actual);
            };
            assertResults(binding, assertions, db);
        }

        @Test
        public void labelNull() throws Exception {
            final Binding binding = pc -> pc.setArray(null);
            final ResultSetAssertions assertions = rs -> {
                final Long[] actual = rs.getArray(1);
                assertNull(actual);
            };
            assertResults(binding, assertions, db);
        }
    }


    public static class GetSQLArray {

        @ClassRule
        public static final PGRule db = new PGRule();

        @Test
        public void index() throws Exception {
            final Long[] expected = new Long[]{1L, 2L, 3L};
            final Binding binding = pc -> pc.setArray(pc.createArrayOf("bigint", expected));
            final ResultSetAssertions assertions = rs -> {
                assertNotNull(rs.getSQLArray(1));
                assertArrayEquals(expected, (Object[]) rs.getSQLArray(1).getArray());
            };
            assertResults(binding, assertions, "bigint[]", db);
        }

        @Test
        public void label() throws Exception {
            final Long[] expected = new Long[]{1L, 2L, 3L};
            final Binding binding = pc -> pc.setArray(pc.createArrayOf("bigint", expected));
            final ResultSetAssertions assertions = rs -> {
                assertNotNull(rs.getSQLArray("bound"));
                assertArrayEquals(expected, (Object[]) rs.getSQLArray("bound").getArray());
            };
            assertResults(binding, assertions, "bigint[]", db);
        }
    }

    public static class GetAsciiStream {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void index() throws Exception {
            final String expected = "abcde";
            final Binding binding = pc -> pc.setAsciiStream(new ByteArrayInputStream(expected.getBytes(Charset.forName("ascii"))));
            final ResultSetAssertions assertions = rs -> {
                final InputStream in = rs.getAsciiStream(1);
                assertNotNull(in);
                final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                copyStream(in, baos);
                assertEquals(expected, new String(baos.toByteArray(), Charset.forName("ascii")));
            };
            assertResults(binding, assertions, db);
        }

        @Test
        public void label() throws Exception {
            final String expected = "abcde";
            final Binding binding = pc -> pc.setAsciiStream(new ByteArrayInputStream(expected.getBytes(Charset.forName("ascii"))));
            final ResultSetAssertions assertions = rs -> {
                final InputStream in = rs.getAsciiStream("bound");
                assertNotNull(in);
                final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                copyStream(in, baos);
                assertEquals(expected, new String(baos.toByteArray(), Charset.forName("ascii")));
            };
            assertResults(binding, assertions, db);
        }
    }

    public static class GetBigDecimal {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void index() throws Exception {
            final BigDecimal expected = new BigDecimal("1.2");
            final Binding binding = pc -> pc.setBigDecimal(expected);
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getBigDecimal(1));
            assertResults(binding, assertions, db);
        }

        @Test
        public void label() throws Exception {
            final BigDecimal expected = new BigDecimal("1.2");
            final Binding binding = pc -> pc.setBigDecimal(expected);
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getBigDecimal("bound"));
            assertResults(binding, assertions, db);
        }
    }

    public static class GetBinaryStream {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void index() throws Exception {
            final String expected = "abcde";
            final Binding binding = pc -> pc.setBinaryStream(new ByteArrayInputStream(expected.getBytes(Charset.forName("ascii"))));
            final ResultSetAssertions assertions = rs -> {
                final InputStream in = rs.getBinaryStream(1);
                assertNotNull(in);
                final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                copyStream(in, baos);
                assertEquals(expected, new String(baos.toByteArray(), Charset.forName("ascii")));
            };
            assertResults(binding, assertions, db);
        }

        @Test
        public void label() throws Exception {
            final String expected = "abcde";
            final Binding binding = pc -> pc.setBinaryStream(new ByteArrayInputStream(expected.getBytes(Charset.forName("ascii"))));
            final ResultSetAssertions assertions = rs -> {
                final InputStream in = rs.getBinaryStream("bound");
                assertNotNull(in);
                final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                copyStream(in, baos);
                assertEquals(expected, new String(baos.toByteArray(), Charset.forName("ascii")));
            };
            assertResults(binding, assertions, db);
        }
    }

    public static class GetBlob {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void index() throws Exception {
            final String expected = "abcde";
            final Binding binding = pc -> pc.setBlob(new ByteArrayInputStream(expected.getBytes(Charset.forName("ascii"))));
            final ResultSetAssertions assertions = rs -> {
                final Blob blob = rs.getBlob(1);
                assertNotNull(blob);
                final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                copyStream(blob.getBinaryStream(), baos);
                assertEquals(expected, new String(baos.toByteArray(), Charset.forName("ascii")));
            };
            assertResults(binding, assertions, db);
        }

        @Test
        public void label() throws Exception {
            final String expected = "abcde";
            final Binding binding = pc -> pc.setBlob(new ByteArrayInputStream(expected.getBytes(Charset.forName("ascii"))));
            final ResultSetAssertions assertions = rs -> {
                final Blob blob = rs.getBlob("bound");
                assertNotNull(blob);
                final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                copyStream(blob.getBinaryStream(), baos);
                assertEquals(expected, new String(baos.toByteArray(), Charset.forName("ascii")));
            };
            assertResults(binding, assertions, db);
        }
    }

    @SuppressWarnings("ConstantConditions")
    public static class GetBoolean {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void index() throws Exception {
            final Boolean expected = Boolean.TRUE;
            final Binding binding = pc -> pc.setBoolean(expected);
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getBoolean(1));
            assertResults(binding, assertions, db);
        }

        @Test
        public void indexNull() throws Exception {
            final Binding binding = pc -> pc.setObject(null);
            final ResultSetAssertions assertions = rs -> assertNull(rs.getBoolean(1));
            assertResults(binding, assertions, db);
        }


        @Test
        public void label() throws Exception {
            final Boolean expected = Boolean.TRUE;
            final Binding binding = pc -> pc.setBoolean(expected);
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getBoolean("bound"));
            assertResults(binding, assertions, db);
        }

        @Test
        public void labelNull() throws Exception {
            final Binding binding = pc -> pc.setObject(null);
            final ResultSetAssertions assertions = rs -> assertNull(rs.getBoolean("bound"));
            assertResults(binding, assertions, db);
        }
    }

    @SuppressWarnings("ConstantConditions")
    public static class GetBooleanPrimitive {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void index() throws Exception {
            final boolean expected = true;
            final Binding binding = pc -> pc.setBoolean(expected);
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getBooleanPrimitive(1));
            assertResults(binding, assertions, db);
        }

        @Test
        public void indexNull() throws Exception {
            final Binding binding = pc -> pc.setObject(null);
            final ResultSetAssertions assertions = rs -> {
                try {
                    rs.getBooleanPrimitive(1);
                    fail("is null, supposed to except");
                } catch (SQLException e) {
                    assertEquals("tried to get primitive for column 1 but was null", e.getMessage());
                }
            };
            assertResults(binding, assertions, db);
        }


        @Test
        public void label() throws Exception {
            final Boolean expected = Boolean.TRUE;
            final Binding binding = pc -> pc.setBoolean(expected);
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getBooleanPrimitive("bound"));
            assertResults(binding, assertions, db);
        }

        @Test
        public void labelNull() throws Exception {
            final Binding binding = pc -> pc.setObject(null);
            final ResultSetAssertions assertions = rs -> {
                try {
                    rs.getBooleanPrimitive("bound");
                    fail("is null, supposed to except");
                } catch (SQLException e) {
                    assertEquals("tried to get primitive for column \"bound\" but was null", e.getMessage());
                }
            };
            assertResults(binding, assertions, db);
        }
    }

    public static class GetByte {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void index() throws Exception {
            final Byte expected = 12;
            final Binding binding = pc -> pc.setByte(expected);
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getByte(1));
            assertResults(binding, assertions, db);
        }

        @Test
        public void indexNull() throws Exception {
            final Binding binding = pc -> pc.setObject(null);
            final ResultSetAssertions assertions = rs -> assertNull(rs.getByte(1));
            assertResults(binding, assertions, db);
        }


        @Test
        public void label() throws Exception {
            final Byte expected = 12;
            final Binding binding = pc -> pc.setByte(expected);
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getByte("bound"));
            assertResults(binding, assertions, db);
        }

        @Test
        public void labelNull() throws Exception {
            final Binding binding = pc -> pc.setObject(null);
            final ResultSetAssertions assertions = rs -> assertNull(rs.getByte("bound"));
            assertResults(binding, assertions, db);
        }
    }

    public static class GetBytePrimitive {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void index() throws Exception {
            final byte expected = 12;
            final Binding binding = pc -> pc.setByte(expected);
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getBytePrimitive(1));
            assertResults(binding, assertions, db);
        }

        @Test
        public void indexNull() throws Exception {
            final Binding binding = pc -> pc.setObject(null);
            final ResultSetAssertions assertions = rs -> {
                try {
                    rs.getBytePrimitive(1);
                    fail("is null, supposed to except");
                } catch (SQLException e) {
                    assertEquals("tried to get primitive for column 1 but was null", e.getMessage());
                }
            };
            assertResults(binding, assertions, db);
        }


        @Test
        public void label() throws Exception {
            final byte expected = 12;
            final Binding binding = pc -> pc.setByte(expected);
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getBytePrimitive("bound"));
            assertResults(binding, assertions, db);
        }

        @Test
        public void labelNull() throws Exception {
            final Binding binding = pc -> pc.setObject(null);
            final ResultSetAssertions assertions = rs -> {
                try {
                    rs.getBytePrimitive("bound");
                    fail("is null, supposed to except");
                } catch (SQLException e) {
                    assertEquals("tried to get primitive for column \"bound\" but was null", e.getMessage());
                }
            };
            assertResults(binding, assertions, db);
        }
    }

    public static class GetBytes {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void index() throws Exception {
            final byte[] expected = {12};
            final Binding binding = pc -> pc.setBytes(expected);
            final ResultSetAssertions assertions = rs -> assertArrayEquals(expected, rs.getBytes(1));
            assertResults(binding, assertions, db);
        }

        @Test
        public void label() throws Exception {
            final byte[] expected = {12};
            final Binding binding = pc -> pc.setBytes(expected);
            final ResultSetAssertions assertions = rs -> assertArrayEquals(expected, rs.getBytes("bound"));
            assertResults(binding, assertions, db);
        }
    }

    public static class GetCharacterStream {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void index() throws Exception {
            final String expected = "abcde";
            final Binding binding = pc -> pc.setCharacterStream(new StringReader(expected));
            final ResultSetAssertions assertions = rs -> {
                final Reader reader = rs.getCharacterStream(1);
                assertNotNull(reader);
                final StringWriter w = new StringWriter();
                copyReader(reader, w);
                assertEquals(expected, w.toString());
            };
            assertResults(binding, assertions, db);
        }

        @Test
        public void label() throws Exception {
            final String expected = "abcde";
            final Binding binding = pc -> pc.setCharacterStream(new StringReader(expected));
            final ResultSetAssertions assertions = rs -> {
                final Reader reader = rs.getCharacterStream("bound");
                assertNotNull(reader);
                final StringWriter w = new StringWriter();
                copyReader(reader, w);
                assertEquals(expected, w.toString());
            };
            assertResults(binding, assertions, db);
        }
    }

    public static class GetClob {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void index() throws Exception {
            final String expected = "abcde";
            final Binding binding = pc -> pc.setClob(new StringReader(expected));
            final ResultSetAssertions assertions = rs -> {
                final Clob clob = rs.getClob(1);
                assertNotNull(clob);
                final StringWriter w = new StringWriter();
                copyReader(clob.getCharacterStream(), w);
                assertEquals(expected, w.toString());
            };
            assertResults(binding, assertions, db);
        }

        @Test
        public void label() throws Exception {
            final String expected = "abcde";
            final Binding binding = pc -> pc.setClob(new StringReader(expected));
            final ResultSetAssertions assertions = rs -> {
                final Clob clob = rs.getClob("bound");
                assertNotNull(clob);
                final StringWriter w = new StringWriter();
                copyReader(clob.getCharacterStream(), w);
                assertEquals(expected, w.toString());
            };
            assertResults(binding, assertions, db);
        }
    }

    public static class GetDate extends HasExpectedTimeSinceEpoch {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void index() throws Exception {
            final Date expected = new Date(expectedTime);
            final Binding binding = pc -> pc.setDate(expected);
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getDate(1));
            assertResults(binding, assertions, db);
        }

        @Test
        public void indexCalendar() throws Exception {
            final Date expected = new Date(expectedTime);
            final Binding binding = pc -> pc.setDate(expected, GregorianCalendar.getInstance());
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getDate(1, GregorianCalendar.getInstance()));
            assertResults(binding, assertions, db);
        }

        @Test
        public void label() throws Exception {
            final Date expected = new Date(expectedTime);
            final Binding binding = pc -> pc.setDate(expected);
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getDate("bound"));
            assertResults(binding, assertions, db);
        }

        @Test
        public void labelCalendar() throws Exception {
            final Date expected = new Date(expectedTime);
            final Binding binding = pc -> pc.setDate(expected, GregorianCalendar.getInstance());
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getDate("bound", GregorianCalendar.getInstance()));
            assertResults(binding, assertions, db);
        }
    }

    public static class GetDouble {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void index() throws Exception {
            final Double expected = 1.2;
            final Binding binding = pc -> pc.setDouble(expected);
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getDouble(1));
            assertResults(binding, assertions, db);
        }

        @Test
        public void indexNull() throws Exception {
            final Binding binding = pc -> pc.setObject(null);
            final ResultSetAssertions assertions = rs -> assertNull(rs.getDouble(1));
            assertResults(binding, assertions, db);
        }


        @Test
        public void label() throws Exception {
            final Double expected = 1.2;
            final Binding binding = pc -> pc.setDouble(expected);
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getDouble("bound"));
            assertResults(binding, assertions, db);
        }

        @Test
        public void labelNull() throws Exception {
            final Binding binding = pc -> pc.setObject(null);
            final ResultSetAssertions assertions = rs -> assertNull(rs.getDouble("bound"));
            assertResults(binding, assertions, db);
        }
    }

    public static class GetDoublePrimitive {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void index() throws Exception {
            final double expected = 1.2;
            final Binding binding = pc -> pc.setDouble(expected);
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getDoublePrimitive(1), 0.0);
            assertResults(binding, assertions, db);
        }

        @Test
        public void indexNull() throws Exception {
            final Binding binding = pc -> pc.setObject(null);
            final ResultSetAssertions assertions = rs -> {
                try {
                    rs.getDoublePrimitive(1);
                    fail("supposed to except");
                } catch (SQLException e) {
                    assertEquals("tried to get primitive for column 1 but was null", e.getMessage());
                }
            };
            assertResults(binding, assertions, db);
        }

        @Test
        public void label() throws Exception {
            final double expected = 1.2;
            final Binding binding = pc -> pc.setDouble(expected);
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getDoublePrimitive("bound"), 0.0);
            assertResults(binding, assertions, db);
        }

        @Test
        public void labelNull() throws Exception {
            final Binding binding = pc -> pc.setObject(null);
            final ResultSetAssertions assertions = rs -> {
                try {
                    rs.getDoublePrimitive("bound");
                    fail("supposed to except");
                } catch (SQLException e) {
                    assertEquals("tried to get primitive for column \"bound\" but was null", e.getMessage());
                }

            };
            assertResults(binding, assertions, db);
        }
    }

    public static class GetEnum {

        public enum Fish {
            RED, BLUE
        }

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void index() throws Exception {
            final Fish expected = Fish.BLUE;
            final Binding binding = pc -> pc.setString(expected.name());
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getEnum(1, Fish.class));
            assertResults(binding, assertions, db);
        }

        @Test
        public void label() throws Exception {
            final Fish expected = Fish.BLUE;
            final Binding binding = pc -> pc.setString(expected.name());
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getEnum("bound", Fish.class));
            assertResults(binding, assertions, db);
        }
    }

    public static class GetFloat {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void index() throws Exception {
            final Float expected = 1.2f;
            final Binding binding = pc -> pc.setFloat(expected);
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getFloat(1));
            assertResults(binding, assertions, db);
        }

        @Test
        public void indexNull() throws Exception {
            final Binding binding = pc -> pc.setObject(null);
            final ResultSetAssertions assertions = rs -> assertNull(rs.getFloat(1));
            assertResults(binding, assertions, db);
        }


        @Test
        public void label() throws Exception {
            final Float expected = 1.2f;
            final Binding binding = pc -> pc.setFloat(expected);
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getFloat("bound"));
            assertResults(binding, assertions, db);
        }

        @Test
        public void labelNull() throws Exception {
            final Binding binding = pc -> pc.setObject(null);
            final ResultSetAssertions assertions = rs -> assertNull(rs.getFloat("bound"));
            assertResults(binding, assertions, db);
        }
    }

    public static class GetFloatPrimitive {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void index() throws Exception {
            final float expected = 1.2f;
            final Binding binding = pc -> pc.setFloat(expected);
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getFloatPrimitive(1), 0.0f);
            assertResults(binding, assertions, db);
        }

        @Test
        public void indexNull() throws Exception {
            final Binding binding = pc -> pc.setObject(null);
            final ResultSetAssertions assertions = rs -> {
                try {
                    rs.getFloatPrimitive(1);
                    fail("supposed to except");
                } catch (SQLException e) {
                    assertEquals("tried to get primitive for column 1 but was null", e.getMessage());
                }
            };
            assertResults(binding, assertions, db);
        }

        @Test
        public void label() throws Exception {
            final float expected = 1.2f;
            final Binding binding = pc -> pc.setFloat(expected);
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getFloatPrimitive("bound"), 0.0f);
            assertResults(binding, assertions, db);
        }

        @Test
        public void labelNull() throws Exception {
            final Binding binding = pc -> pc.setObject(null);
            final ResultSetAssertions assertions = rs -> {
                try {
                    rs.getFloatPrimitive("bound");
                    fail("supposed to except");
                } catch (SQLException e) {
                    assertEquals("tried to get primitive for column \"bound\" but was null", e.getMessage());
                }

            };
            assertResults(binding, assertions, db);
        }
    }

    public static class GetInteger {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void index() throws Exception {
            final Integer expected = 12;
            final Binding binding = pc -> pc.setInt(expected);
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getInteger(1));
            assertResults(binding, assertions, db);
        }

        @Test
        public void indexNull() throws Exception {
            final Binding binding = pc -> pc.setObject(null);
            final ResultSetAssertions assertions = rs -> assertNull(rs.getInteger(1));
            assertResults(binding, assertions, db);
        }

        @Test
        public void label() throws Exception {
            final Integer expected = 12;
            final Binding binding = pc -> pc.setInt(expected);
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getInteger("bound"));
            assertResults(binding, assertions, db);
        }

        @Test
        public void labelNull() throws Exception {
            final Binding binding = pc -> pc.setObject(null);
            final ResultSetAssertions assertions = rs -> assertNull(rs.getInteger("bound"));
            assertResults(binding, assertions, db);
        }
    }

    public static class GetIntegerPrimitive {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void index() throws Exception {
            final int expected = 12;
            final Binding binding = pc -> pc.setInt(expected);
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getIntegerPrimitive(1));
            assertResults(binding, assertions, db);
        }

        @Test
        public void indexNull() throws Exception {
            final Binding binding = pc -> pc.setObject(null);
            final ResultSetAssertions assertions = rs -> {
                try {
                    rs.getIntegerPrimitive(1);
                    fail("supposed to except");
                } catch (SQLException e) {
                    assertEquals("tried to get primitive for column 1 but was null", e.getMessage());
                }
            };
            assertResults(binding, assertions, db);
        }

        @Test
        public void label() throws Exception {
            final int expected = 12;
            final Binding binding = pc -> pc.setInt(expected);
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getIntegerPrimitive("bound"));
            assertResults(binding, assertions, db);
        }

        @Test
        public void labelNull() throws Exception {
            final Binding binding = pc -> pc.setObject(null);
            final ResultSetAssertions assertions = rs -> {
                try {
                    rs.getIntegerPrimitive("bound");
                    fail("supposed to except");
                } catch (SQLException e) {
                    assertEquals("tried to get primitive for column \"bound\" but was null", e.getMessage());
                }
            };
            assertResults(binding, assertions, db);
        }
    }

    public static class GetLong {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void index() throws Exception {
            final Long expected = 12L;
            final Binding binding = pc -> pc.setLong(expected);
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getLong(1));
            assertResults(binding, assertions, db);
        }

        @Test
        public void indexNull() throws Exception {
            final Binding binding = pc -> pc.setObject(null);
            final ResultSetAssertions assertions = rs -> assertNull(rs.getLong(1));
            assertResults(binding, assertions, db);
        }

        @Test
        public void label() throws Exception {
            final Long expected = 12L;
            final Binding binding = pc -> pc.setLong(expected);
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getLong("bound"));
            assertResults(binding, assertions, db);
        }

        @Test
        public void labelNull() throws Exception {
            final Binding binding = pc -> pc.setObject(null);
            final ResultSetAssertions assertions = rs -> assertNull(rs.getLong("bound"));
            assertResults(binding, assertions, db);
        }
    }

    public static class GetLongPrimitive {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void index() throws Exception {
            final long expected = 12;
            final Binding binding = pc -> pc.setLong(expected);
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getLongPrimitive(1));
            assertResults(binding, assertions, db);
        }

        @Test
        public void indexNull() throws Exception {
            final Binding binding = pc -> pc.setObject(null);
            final ResultSetAssertions assertions = rs -> {
                try {
                    rs.getLongPrimitive(1);
                    fail("supposed to except");
                } catch (SQLException e) {
                    assertEquals("tried to get primitive for column 1 but was null", e.getMessage());
                }
            };
            assertResults(binding, assertions, db);
        }

        @Test
        public void label() throws Exception {
            final long expected = 12;
            final Binding binding = pc -> pc.setLong(expected);
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getLongPrimitive("bound"));
            assertResults(binding, assertions, db);
        }

        @Test
        public void labelNull() throws Exception {
            final Binding binding = pc -> pc.setObject(null);
            final ResultSetAssertions assertions = rs -> {
                try {
                    rs.getLongPrimitive("bound");
                    fail("supposed to except");
                } catch (SQLException e) {
                    assertEquals("tried to get primitive for column \"bound\" but was null", e.getMessage());
                }
            };
            assertResults(binding, assertions, db);
        }
    }

    public static class GetNCharacterStream {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void index() throws Exception {
            final String expected = "abcde";
            final Binding binding = pc -> pc.setNCharacterStream(new StringReader(expected));
            final ResultSetAssertions assertions = rs -> {
                final Reader reader = rs.getNCharacterStream(1);
                assertNotNull(reader);
                final StringWriter w = new StringWriter();
                copyReader(reader, w);
                assertEquals(expected, w.toString());
            };
            assertResults(binding, assertions, db);
        }

        @Test
        public void label() throws Exception {
            final String expected = "abcde";
            final Binding binding = pc -> pc.setNCharacterStream(new StringReader(expected));
            final ResultSetAssertions assertions = rs -> {
                final Reader reader = rs.getNCharacterStream("bound");
                assertNotNull(reader);
                final StringWriter w = new StringWriter();
                copyReader(reader, w);
                assertEquals(expected, w.toString());
            };
            assertResults(binding, assertions, db);
        }
    }

    public static class GetNClob {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void index() throws Exception {
            final String expected = "abcde";
            final Binding binding = pc -> pc.setNClob(new StringReader(expected));
            final ResultSetAssertions assertions = rs -> {
                final NClob clob = rs.getNClob(1);
                assertNotNull(clob);
                final StringWriter w = new StringWriter();
                copyReader(clob.getCharacterStream(), w);
                assertEquals(expected, w.toString());
            };
            assertResults(binding, assertions, db);
        }

        @Test
        public void label() throws Exception {
            final String expected = "abcde";
            final Binding binding = pc -> pc.setNClob(new StringReader(expected));
            final ResultSetAssertions assertions = rs -> {
                final NClob clob = rs.getNClob("bound");
                assertNotNull(clob);
                final StringWriter w = new StringWriter();
                copyReader(clob.getCharacterStream(), w);
                assertEquals(expected, w.toString());
            };
            assertResults(binding, assertions, db);
        }
    }

    public static class GetNString {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void index() throws Exception {
            final String expected = "abcde";
            final Binding binding = pc -> pc.setNString(expected);
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getNString(1));
            assertResults(binding, assertions, db);
        }

        @Test
        public void label() throws Exception {
            final String expected = "abcde";
            final Binding binding = pc -> pc.setNString(expected);
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getNString("bound"));
            assertResults(binding, assertions, db);
        }
    }

    public static class GetObject {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void index() throws Exception {
            final String expected = "abcde";
            final Binding binding = pc -> pc.setObject(expected);
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getObject(1));
            assertResults(binding, assertions, db);
        }

        @Test
        public void label() throws Exception {
            final String expected = "abcde";
            final Binding binding = pc -> pc.setObject(expected);
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getObject("bound"));
            assertResults(binding, assertions, db);
        }
    }

    public static class GetObjectByMap {

        @ClassRule
        public static final PGRule db = new PGRule();

        private final Map<String, Class<?>> typeMap = new HashMap<>();

        public GetObjectByMap() {
            typeMap.put(JDBCType.VARCHAR.getName(), String.class);
        }

        @Test(expected = SQLFeatureNotSupportedException.class)
        public void index() throws Exception {
            final String expected = "abcde";
            final Binding binding = pc -> pc.setObject(expected);
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getObject(1, typeMap));
            assertResults(binding, assertions, db);
        }

        @Test(expected = SQLFeatureNotSupportedException.class)
        public void label() throws Exception {
            final String expected = "abcde";
            final Binding binding = pc -> pc.setObject(expected);
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getObject("bound", typeMap));
            assertResults(binding, assertions, db);
        }
    }


    public static class GetObjectByClass {

        @ClassRule
        public static final PGRule db = new PGRule();

        @Test(expected = SQLFeatureNotSupportedException.class)
        public void index() throws Exception {
            final String expected = "abcde";
            final Binding binding = pc -> pc.setObject(expected);
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getObject(1, String.class));
            assertResults(binding, assertions, db);
        }

        @Test(expected = SQLFeatureNotSupportedException.class)
        public void label() throws Exception {
            final String expected = "abcde";
            final Binding binding = pc -> pc.setObject(expected);
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getObject("bound", String.class));
            assertResults(binding, assertions, db);
        }
    }

    public static class GetShort {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void index() throws Exception {
            final Short expected = 12;
            final Binding binding = pc -> pc.setShort(expected);
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getShort(1));
            assertResults(binding, assertions, db);
        }

        @Test
        public void indexNull() throws Exception {
            final Binding binding = pc -> pc.setObject(null);
            final ResultSetAssertions assertions = rs -> assertNull(rs.getShort(1));
            assertResults(binding, assertions, db);
        }

        @Test
        public void label() throws Exception {
            final Short expected = 12;
            final Binding binding = pc -> pc.setShort(expected);
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getShort("bound"));
            assertResults(binding, assertions, db);
        }

        @Test
        public void labelNull() throws Exception {
            final Binding binding = pc -> pc.setObject(null);
            final ResultSetAssertions assertions = rs -> assertNull(rs.getShort("bound"));
            assertResults(binding, assertions, db);
        }
    }

    public static class GetShortPrimitive {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void index() throws Exception {
            final short expected = 12;
            final Binding binding = pc -> pc.setShort(expected);
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getShortPrimitive(1));
            assertResults(binding, assertions, db);
        }

        @Test
        public void indexNull() throws Exception {
            final Binding binding = pc -> pc.setObject(null);
            final ResultSetAssertions assertions = rs -> {
                try {
                    rs.getShortPrimitive(1);
                    fail("supposed to except");
                } catch (SQLException e) {
                    assertEquals("tried to get primitive for column 1 but was null", e.getMessage());
                }
            };
            assertResults(binding, assertions, db);
        }

        @Test
        public void label() throws Exception {
            final short expected = 12;
            final Binding binding = pc -> pc.setShort(expected);
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getShortPrimitive("bound"));
            assertResults(binding, assertions, db);
        }

        @Test
        public void labelNull() throws Exception {
            final Binding binding = pc -> pc.setObject(null);
            final ResultSetAssertions assertions = rs -> {
                try {
                    rs.getShortPrimitive("bound");
                    fail("supposed to except");
                } catch (SQLException e) {
                    assertEquals("tried to get primitive for column \"bound\" but was null", e.getMessage());
                }
            };
            assertResults(binding, assertions, db);
        }
    }

    public static class GetSQLXml {

        @ClassRule
        public static final PGRule db = new PGRule();

        @Test
        public void value() throws Exception {
            final String expected = "<a></a>";
            final Binding binding = pc -> {
                final SQLXML sqlxml = pc.createSQLXML();
                sqlxml.setString(expected);
                pc.setSQLXML(sqlxml);
            };
            final ResultSetAssertions assertions = rs -> {
                final SQLXML sqlxml = rs.getSQLXML(1);
                assertNotNull(sqlxml);
                assertEquals(expected, sqlxml.getString());
            };
            assertResults(binding, assertions, db);
        }

        @Test
        public void valueNull() throws Exception {
            final String expected = "<a></a>";
            final Binding binding = pc -> {
                final SQLXML sqlxml = pc.createSQLXML();
                sqlxml.setString(expected);
                pc.setSQLXML(sqlxml);
            };
            final ResultSetAssertions assertions = rs -> {
                final SQLXML sqlxml = rs.getSQLXML("bound");
                assertNotNull(sqlxml);
                assertEquals(expected, sqlxml.getString());
            };
            assertResults(binding, assertions, db);
        }
    }

    public static class GetString {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void index() throws Exception {
            final String expected = "abcde";
            final Binding binding = pc -> pc.setString(expected);
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getString(1));
            assertResults(binding, assertions, db);
        }

        @Test
        public void label() throws Exception {
            final String expected = "abcde";
            final Binding binding = pc -> pc.setString(expected);
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getString("bound"));
            assertResults(binding, assertions, db);
        }
    }

    public static class GetTime extends HasExpectedTimeOfDay {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void index() throws Exception {
            final Time expected = new Time(expectedMillis);
            final Binding binding = pc -> pc.setTime(expected);
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getTime(1));
            assertResults(binding, assertions, db);
        }

        @Test
        public void indexCalendar() throws Exception {
            final Time expected = new Time(expectedMillis);
            final Binding binding = pc -> pc.setTime(expected, GregorianCalendar.getInstance());
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getTime(1, GregorianCalendar.getInstance()));
            assertResults(binding, assertions, db);
        }

        @Test
        public void label() throws Exception {
            final Time expected = new Time(expectedMillis);
            final Binding binding = pc -> pc.setTime(expected);
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getTime("bound"));
            assertResults(binding, assertions, db);
        }

        @Test
        public void labelCalendar() throws Exception {
            final Time expected = new Time(expectedMillis);
            final Binding binding = pc -> pc.setTime(expected, GregorianCalendar.getInstance());
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getTime("bound", GregorianCalendar.getInstance()));
            assertResults(binding, assertions, db);
        }
    }

    public static class GetTimestamp extends HasExpectedTimeSinceEpoch {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test
        public void index() throws Exception {
            final Timestamp expected = new Timestamp(expectedTime);
            final Binding binding = pc -> pc.setTimestamp(expected);
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getTimestamp(1));
            assertResults(binding, assertions, db);
        }

        @Test
        public void indexCalendar() throws Exception {
            final Timestamp expected = new Timestamp(expectedTime);
            final Binding binding = pc -> pc.setTimestamp(expected, GregorianCalendar.getInstance());
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getTimestamp(1, GregorianCalendar.getInstance()));
            assertResults(binding, assertions, db);
        }

        @Test
        public void label() throws Exception {
            final Timestamp expected = new Timestamp(expectedTime);
            final Binding binding = pc -> pc.setTimestamp(expected);
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getTimestamp("bound"));
            assertResults(binding, assertions, db);
        }

        @Test
        public void labelCalendar() throws Exception {
            final Timestamp expected = new Timestamp(expectedTime);
            final Binding binding = pc -> pc.setTimestamp(expected, GregorianCalendar.getInstance());
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getTimestamp("bound", GregorianCalendar.getInstance()));
            assertResults(binding, assertions, db);
        }
    }


    public static class GetURL {

        @ClassRule
        public static final PGRule db = new PGRule();

        @Test(expected = SQLFeatureNotSupportedException.class)
        public void index() throws Exception {
            final URL expected = new URL("http", "google.com", 8080, "/");
            assertEquals("http://google.com:8080/", expected.toString());

            final Binding binding = pc -> pc.setString(expected.toString());
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getURL(1));
            assertResults(binding, assertions, db);
        }

        @Test(expected = SQLFeatureNotSupportedException.class)
        public void label() throws Exception {
            final URL expected = new URL("http", "google.com", 8080, "/");
            assertEquals("http://google.com:8080/", expected.toString());

            final Binding binding = pc -> pc.setString(expected.toString());
            final ResultSetAssertions assertions = rs -> assertEquals(expected, rs.getURL("bound"));
            assertResults(binding, assertions, db);
        }
    }

    private static void assertResults(Binding binding, ResultSetAssertions assertions, DataSource db) throws SQLException {
        final String castType = "varchar";
        assertResults(binding, assertions, castType, db);
    }

    private static void assertResults(Binding binding, ResultSetAssertions assertions, String castType, DataSource db) throws SQLException {
        try (Connection connection = db.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT CAST(? as " + castType + ") as bound")) {
            binding.bind(new PreparedColumn(ps, 1));
            try (SmartResultSet rs = new SmartResultSet(ps.executeQuery())) {
                assertTrue(rs.next());
                assertions.runAssertions(rs);

                assertFalse(rs.next());
            }
        }
    }

    public interface ResultSetAssertions {

        void runAssertions(SmartResultSet rs) throws SQLException;

    }

    public static void copyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            while (true) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1) {
                    break;
                }
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void copyReader(Reader r, Writer w) {
        try {
            while (true) {
                char[] buffer = new char[1024];
                int size = r.read(buffer);
                if (size == -1) {
                    break;
                }
                w.write(buffer, 0, size);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
