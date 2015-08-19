package com.github.randyp.jdbj;

import com.github.randyp.jdbj.db.h2_1_4.H2Rule;
import com.github.randyp.jdbj.lambda.ResultSetMapper;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.sql.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Enclosed.class)
public class DefaultCollectionBindingsBuilderTest {

    @ClassRule
    public static final H2Rule db = new H2Rule();

    private static final NamedParameterStatement statement =
            NamedParameterStatement.make("SELECT :binding as bound");

    public static class DefaultBindList {
        @Test(expected = IllegalArgumentException.class)
        public void nullName() throws Exception {
            new TestBuilder().bindDefaultList(null, Collections.singletonList(pc -> pc.setInt(1)));
        }

        @Test(expected = IllegalArgumentException.class)
        public void nulList() throws Exception {
            new TestBuilder().bindDefaultList(":binding", null);
        }
    }

    public static class BindBooleans {
        @Test
        public void values() throws Exception {
            final Boolean[] expected = {Boolean.FALSE, Boolean.TRUE};
            try (Connection connection = db.getConnection()) {
                final Object[] selected = new TestBuilder()
                        .bindDefaultBooleans(":binding", Arrays.asList(expected))
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
                assertArrayEquals(expected, selected);
            }
        }

        @Test
        public void valuesInArray() throws Exception {
            try (Connection connection = db.getConnection()) {
                final Object[] selected = new TestBuilder()
                        .bindDefaultBooleans(":binding", true, false)
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
                assertArrayEquals(new Boolean[]{Boolean.TRUE, Boolean.FALSE}, selected);
            }
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullArray() throws Exception {
            new TestBuilder().bindDefaultBooleans(":binding", (boolean[]) null);
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullList() throws Exception {
            new TestBuilder().bindDefaultBooleans(":binding", (List<Boolean>) null);
        }
    }

    public static class BindBytes {
        @Test
        public void values() throws Exception {
            final Byte[] expected = {10, 11};
            try (Connection connection = db.getConnection()) {
                final Object[] selected = new TestBuilder()
                        .bindDefaultBytes(":binding", Arrays.asList(expected))
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
                assertArrayEquals(expected, selected);
            }
        }

        @Test
        public void valuesInArray() throws Exception {
            try (Connection connection = db.getConnection()) {
                final Object[] selected = new TestBuilder()
                        .bindDefaultBytes(":binding", (byte) 10, (byte) 12)
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
                assertArrayEquals(new Byte[]{10, 12}, selected);
            }
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullArray() throws Exception {
            new TestBuilder().bindDefaultBytes(":binding", (byte[]) null);
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullList() throws Exception {
            new TestBuilder().bindDefaultBytes(":binding", (List<Byte>) null);

        }
    }

    public static class BindDates extends HasExpectedTimeSinceEpoch {
        @Test
        public void values() throws Exception {
            final Date[] expected = {new Date(expectedTime), new Date(expectedTime)};
            try (Connection connection = db.getConnection()) {
                final Object[] selected = new TestBuilder()
                        .bindDefaultDates(":binding", Arrays.asList(expected))
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
                assertArrayEquals(expected, selected);
            }
        }

        @Test
        public void valuesAndCal() throws Exception {
            final Date[] expected = {new Date(expectedTime), new Date(expectedTime)};
            try (Connection connection = db.getConnection()) {
                final Object[] selected = new TestBuilder()
                        .bindDefaultDates(":binding", Arrays.asList(expected), GregorianCalendar.getInstance())
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
                assertArrayEquals(expected, selected);
            }
        }

        @Test(expected = IllegalArgumentException.class)
        public void Null() throws Exception {
            new TestBuilder().bindDefaultDates(":binding", null);
        }

        @Test(expected = IllegalArgumentException.class)
        public void NullAndCalendar() throws Exception {
            new TestBuilder().bindDefaultDates(":binding", null, GregorianCalendar.getInstance());

        }
    }

    public static class BindDoubles {
        @Test
        public void values() throws Exception {
            final Double[] expected = {10.0, 12.0};
            try (Connection connection = db.getConnection()) {
                final Object[] selected = new TestBuilder()
                        .bindDefaultDoubles(":binding", Arrays.asList(expected))
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
                assertArrayEquals(expected, selected);
            }
        }

        @Test
        public void valuesInArray() throws Exception {
            try (Connection connection = db.getConnection()) {
                final Object[] selected = new TestBuilder()
                        .bindDefaultDoubles(":binding", 10.0, 12.0)
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
                assertArrayEquals(new Double[]{10.0, 12.0}, selected);
            }
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullArray() throws Exception {
            new TestBuilder().bindDefaultDoubles(":binding", (double[]) null);
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullList() throws Exception {
            new TestBuilder().bindDefaultDoubles(":binding", (List<Double>) null);

        }
    }

    public static class BindFloats {
        @Test
        public void values() throws Exception {
            final Float[] expected = {10.0f, 12.0f};
            try (Connection connection = db.getConnection()) {
                final Object[] selected = new TestBuilder()
                        .bindDefaultFloats(":binding", Arrays.asList(expected))
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
                assertArrayEquals(expected, selected);
            }
        }

        @Test
        public void valuesInArray() throws Exception {
            try (Connection connection = db.getConnection()) {
                final Object[] selected = new TestBuilder()
                        .bindDefaultFloats(":binding", 10.0f, 12.0f)
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
                assertArrayEquals(new Float[]{10.0f, 12.0f}, selected);
            }
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullArray() throws Exception {
            new TestBuilder().bindDefaultFloats(":binding", (float[]) null);
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullList() throws Exception {
            new TestBuilder().bindDefaultFloats(":binding", (List<Float>) null);

        }
    }

    public static class BindIntegers {
        @Test
        public void values() throws Exception {
            final Integer[] expected = {10, 12};
            try (Connection connection = db.getConnection()) {
                final Object[] selected = new TestBuilder()
                        .bindDefaultIntegers(":binding", Arrays.asList(expected))
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
                assertArrayEquals(expected, selected);
            }
        }

        @Test
        public void valuesInArray() throws Exception {
            try (Connection connection = db.getConnection()) {
                final Object[] selected = new TestBuilder()
                        .bindDefaultIntegers(":binding", 10, 12)
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
                assertArrayEquals(new Integer[]{10, 12}, selected);
            }
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullArray() throws Exception {
            new TestBuilder().bindDefaultIntegers(":binding", (int[]) null);
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullList() throws Exception {
            new TestBuilder().bindDefaultIntegers(":binding", (List<Integer>) null);
        }
    }

    public static class BindLongs {

        @Test
        public void values() throws Exception {
            try (Connection connection = db.getConnection()) {
                final Object[] selected = new TestBuilder()
                        .bindDefaultLongs(":binding", Arrays.asList(152L, 51L))
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
                assertArrayEquals(new Long[]{152L, 51L}, selected);
            }
        }

        @Test
        public void valuesInArray() throws Exception {
            try (Connection connection = db.getConnection()) {
                final Object[] selected = new TestBuilder()
                        .bindDefaultLongs(":binding", 152L, 51L)
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
                assertArrayEquals(new Long[]{152L, 51L}, selected);
            }
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullArray() throws Exception {
            try (Connection connection = db.getConnection()) {
                new TestBuilder()
                        .bindDefaultLongs(":binding", (long[]) null)
                        .execute(connection, rs -> rs.getObject(1));
            }
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullList() throws Exception {
            try (Connection connection = db.getConnection()) {
                new TestBuilder()
                        .bindDefaultLongs(":binding", (List<Long>) null)
                        .execute(connection, rs -> rs.getObject(1));
            }
        }
    }

    public static class BindObjects {
        @Test
        public void values() throws Exception {
            final String[] expected = {"abc", "def"};
            try (Connection connection = db.getConnection()) {
                final Object[] selected = new TestBuilder()
                        .bindDefaultObjects(":binding", Arrays.asList(expected))
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
                assertArrayEquals(expected, selected);
            }
        }

        @Test
        public void valuesInArray() throws Exception {
            final String[] expected = {"abc", "def"};
            try (Connection connection = db.getConnection()) {
                final Object[] selected = new TestBuilder()
                        .bindDefaultObjects(":binding", expected)
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
                assertArrayEquals(expected, selected);
            }
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullArray() throws Exception {
            new TestBuilder().bindDefaultObjects(":binding", (Object[]) null);
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullList() throws Exception {
            new TestBuilder().bindDefaultObjects(":binding", (List<Object>) null);
        }
    }

    public static class BindShorts {
        @Test
        public void values() throws Exception {
            final Short[] expected = {10, 12};
            try (Connection connection = db.getConnection()) {
                final Object[] selected = new TestBuilder()
                        .bindDefaultShorts(":binding", Arrays.asList(expected))
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
                assertArrayEquals(expected, selected);
            }
        }

        @Test
        public void valuesInArray() throws Exception {
            try (Connection connection = db.getConnection()) {
                final Object[] selected = new TestBuilder()
                        .bindDefaultShorts(":binding", (short) 10, (short) 12)
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
                assertArrayEquals(new Short[]{10, 12}, selected);
            }
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullArray() throws Exception {
            new TestBuilder().bindDefaultShorts(":binding", (short[]) null);
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullList() throws Exception {
            new TestBuilder().bindDefaultShorts(":binding", (List<Short>) null);

        }
    }

    public static class BindStrings {
        @Test
        public void values() throws Exception {
            final String[] expected = {"abc", "def"};
            try (Connection connection = db.getConnection()) {
                final Object[] selected = new TestBuilder()
                        .bindDefaultStrings(":binding", Arrays.asList(expected))
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
                assertArrayEquals(expected, selected);
            }
        }

        @Test
        public void valuesInArray() throws Exception {
            final String[] expected = {"abc", "def"};
            try (Connection connection = db.getConnection()) {
                final Object[] selected = new TestBuilder()
                        .bindDefaultStrings(":binding", expected)
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
                assertArrayEquals(expected, selected);
            }
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullArray() throws Exception {
            new TestBuilder().bindDefaultStrings(":binding", (String[]) null);
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullList() throws Exception {
            new TestBuilder().bindDefaultStrings(":binding", (List<String>) null);
        }
    }

    public static class BindTimes extends HasExpectedTimeOfDay {
        @Test
        public void values() throws Exception {
            final Time[] expected = {new Time(expectedTime), new Time(expectedTime)};
            try (Connection connection = db.getConnection()) {
                final Object[] selected = new TestBuilder()
                        .bindDefaultTimes(":binding", Arrays.asList(expected))
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
                assertArrayEquals(expected, selected);
            }
        }

        @Test
        public void valuesAndCal() throws Exception {
            final Time[] expected = {new Time(expectedTime), new Time(expectedTime)};
            try (Connection connection = db.getConnection()) {
                final Object[] selected = new TestBuilder()
                        .bindDefaultTimes(":binding", Arrays.asList(expected), GregorianCalendar.getInstance())
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
                assertArrayEquals(expected, selected);
            }
        }

        @Test(expected = IllegalArgumentException.class)
        public void Null() throws Exception {
            new TestBuilder().bindDefaultTimes(":binding", null);
        }

        @Test(expected = IllegalArgumentException.class)
        public void NullAndCalendar() throws Exception {
            new TestBuilder().bindDefaultTimes(":binding", null, GregorianCalendar.getInstance());

        }
    }

    public static class BindTimestamps extends HasExpectedTimeSinceEpoch {
        @Test
        public void values() throws Exception {
            final Timestamp[] expected = {new Timestamp(expectedTime), new Timestamp(expectedTime)};
            try (Connection connection = db.getConnection()) {
                final Object[] selected = new TestBuilder()
                        .bindDefaultTimestamps(":binding", Arrays.asList(expected))
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
                assertArrayEquals(expected, selected);
            }
        }

        @Test
        public void valuesAndCal() throws Exception {
            final Timestamp[] expected = {new Timestamp(expectedTime), new Timestamp(expectedTime)};
            try (Connection connection = db.getConnection()) {
                final Object[] selected = new TestBuilder()
                        .bindDefaultTimestamps(":binding", Arrays.asList(expected), GregorianCalendar.getInstance())
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
                assertArrayEquals(expected, selected);
            }
        }

        @Test(expected = IllegalArgumentException.class)
        public void Null() throws Exception {
            new TestBuilder().bindDefaultTimestamps(":binding", null);
        }

        @Test(expected = IllegalArgumentException.class)
        public void NullAndCalendar() throws Exception {
            new TestBuilder().bindDefaultTimestamps(":binding", null, GregorianCalendar.getInstance());

        }
    }

    private static class TestBuilder extends PositionalBindingsBuilder<TestBuilder> {

        TestBuilder() {
            this(DefaultCollectionBindingsBuilderTest.statement, PositionalBindings.empty());
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