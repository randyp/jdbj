package com.github.randyp.jdbj;

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
public class CollectionBindingsBuilderTest {

    @ClassRule
    public static final H2Rule db = new H2Rule();

    private static final NamedParameterStatement statement =
            NamedParameterStatement.make("SELECT :binding as bound");

    public static class BindList {
        @Test(expected = IllegalArgumentException.class)
        public void nullName() throws Exception {
            new TestBuilder()
                    .bindCollection(null, Collections.singletonList(pc -> pc.setInt(1)));
        }

        @Test(expected = IllegalArgumentException.class)
        public void nulList() throws Exception {
            new TestBuilder()
                    .bindCollection(":binding", null);
        }
    }

    public static class BindBooleans {
        @Test
        public void values() throws Exception {
            final Boolean[] expected = {Boolean.FALSE, Boolean.TRUE};
            try (Connection connection = db.getConnection()) {
                final Object[] selected = new TestBuilder()
                        .bindBooleans(":binding", Arrays.asList(expected))
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
                assertArrayEquals(expected, selected);
            }
        }

        @Test
        public void valuesInArray() throws Exception {
            try (Connection connection = db.getConnection()) {
                final Object[] selected = new TestBuilder()
                        .bindBooleans(":binding", true, false)
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
                assertArrayEquals(new Boolean[]{Boolean.TRUE, Boolean.FALSE}, selected);
            }
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullArray() throws Exception {
            new TestBuilder().bindBooleans(":binding", (boolean[]) null);
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullList() throws Exception {
            new TestBuilder().bindBooleans(":binding", (List<Boolean>) null);
        }
    }

    public static class BindBytes {
        @Test
        public void values() throws Exception {
            final Byte[] expected = {10, 11};
            try (Connection connection = db.getConnection()) {
                final Object[] selected = new TestBuilder()
                        .bindBytes(":binding", Arrays.asList(expected))
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
                assertArrayEquals(expected, selected);
            }
        }

        @Test
        public void valuesInArray() throws Exception {
            try (Connection connection = db.getConnection()) {
                final Object[] selected = new TestBuilder()
                        .bindBytes(":binding", (byte) 10, (byte) 12)
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
                assertArrayEquals(new Byte[]{10, 12}, selected);
            }
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullArray() throws Exception {
            new TestBuilder().bindBytes(":binding", (byte[]) null);
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullList() throws Exception {
            new TestBuilder().bindBytes(":binding", (List<Byte>) null);

        }
    }

    public static class BindDates extends HasExpectedTimeSinceEpoch {
        @Test
        public void values() throws Exception {
            final Date[] expected = {new Date(expectedTime), new Date(expectedTime)};
            try (Connection connection = db.getConnection()) {
                final Object[] selected = new TestBuilder()
                        .bindDates(":binding", Arrays.asList(expected))
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
                assertArrayEquals(expected, selected);
            }
        }

        @Test
        public void valuesAndCal() throws Exception {
            final Date[] expected = {new Date(expectedTime), new Date(expectedTime)};
            try (Connection connection = db.getConnection()) {
                final Object[] selected = new TestBuilder()
                        .bindDates(":binding", Arrays.asList(expected), GregorianCalendar.getInstance())
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
                assertArrayEquals(expected, selected);
            }
        }

        @Test(expected = IllegalArgumentException.class)
        public void Null() throws Exception {
            new TestBuilder().bindDates(":binding", null);
        }

        @Test(expected = IllegalArgumentException.class)
        public void NullAndCalendar() throws Exception {
            new TestBuilder().bindDates(":binding", null, GregorianCalendar.getInstance());

        }
    }

    public static class BindDoubles {
        @Test
        public void values() throws Exception {
            final Double[] expected = {10.0, 12.0};
            try (Connection connection = db.getConnection()) {
                final Object[] selected = new TestBuilder()
                        .bindDoubles(":binding", Arrays.asList(expected))
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
                assertArrayEquals(expected, selected);
            }
        }

        @Test
        public void valuesInArray() throws Exception {
            try (Connection connection = db.getConnection()) {
                final Object[] selected = new TestBuilder()
                        .bindDoubles(":binding", 10.0, 12.0)
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
                assertArrayEquals(new Double[]{10.0, 12.0}, selected);
            }
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullArray() throws Exception {
            new TestBuilder().bindDoubles(":binding", (double[]) null);
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullList() throws Exception {
            new TestBuilder().bindDoubles(":binding", (List<Double>) null);

        }
    }

    public static class BindFloats {
        @Test
        public void values() throws Exception {
            final Float[] expected = {10.0f, 12.0f};
            try (Connection connection = db.getConnection()) {
                final Object[] selected = new TestBuilder()
                        .bindFloats(":binding", Arrays.asList(expected))
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
                assertArrayEquals(expected, selected);
            }
        }

        @Test
        public void valuesInArray() throws Exception {
            try (Connection connection = db.getConnection()) {
                final Object[] selected = new TestBuilder()
                        .bindFloats(":binding", 10.0f, 12.0f)
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
                assertArrayEquals(new Float[]{10.0f, 12.0f}, selected);
            }
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullArray() throws Exception {
            new TestBuilder().bindFloats(":binding", (float[]) null);
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullList() throws Exception {
            new TestBuilder().bindFloats(":binding", (List<Float>) null);

        }
    }

    public static class BindIntegers {
        @Test
        public void values() throws Exception {
            final Integer[] expected = {10, 12};
            try (Connection connection = db.getConnection()) {
                final Object[] selected = new TestBuilder()
                        .bindIntegers(":binding", Arrays.asList(expected))
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
                assertArrayEquals(expected, selected);
            }
        }

        @Test
        public void valuesInArray() throws Exception {
            try (Connection connection = db.getConnection()) {
                final Object[] selected = new TestBuilder()
                        .bindIntegers(":binding", 10, 12)
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
                assertArrayEquals(new Integer[]{10, 12}, selected);
            }
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullArray() throws Exception {
            new TestBuilder().bindIntegers(":binding", (int[]) null);
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullList() throws Exception {
            new TestBuilder().bindIntegers(":binding", (List<Integer>) null);

        }
    }

    public static class BindLongs {

        @Test
        public void values() throws Exception {
            try (Connection connection = db.getConnection()) {
                final Object[] selected = new TestBuilder()
                        .bindLongs(":binding", Arrays.asList(152L, 51L))
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
                assertArrayEquals(new Long[]{152L, 51L}, selected);
            }
        }

        @Test
        public void valuesInArray() throws Exception {
            try (Connection connection = db.getConnection()) {
                final Object[] selected = new TestBuilder()
                        .bindLongs(":binding", 152L, 51L)
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
                assertArrayEquals(new Long[]{152L, 51L}, selected);
            }
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

    public static class BindObjects {
        @Test
        public void values() throws Exception {
            final String[] expected = {"abc", "def"};
            try (Connection connection = db.getConnection()) {
                final Object[] selected = new TestBuilder()
                        .bindObjects(":binding", Arrays.asList(expected))
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
                assertArrayEquals(expected, selected);
            }
        }

        @Test
        public void valuesInArray() throws Exception {
            final String[] expected = {"abc", "def"};
            try (Connection connection = db.getConnection()) {
                final Object[] selected = new TestBuilder()
                        .bindObjects(":binding", expected)
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
                assertArrayEquals(expected, selected);
            }
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullArray() throws Exception {
            new TestBuilder().bindObjects(":binding", (Object[]) null);
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullList() throws Exception {
            new TestBuilder().bindObjects(":binding", (List<Object>) null);
        }
    }

    public static class BindShorts {
        @Test
        public void values() throws Exception {
            final Short[] expected = {10, 12};
            try (Connection connection = db.getConnection()) {
                final Object[] selected = new TestBuilder()
                        .bindShorts(":binding", Arrays.asList(expected))
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
                assertArrayEquals(expected, selected);
            }
        }

        @Test
        public void valuesInArray() throws Exception {
            try (Connection connection = db.getConnection()) {
                final Object[] selected = new TestBuilder()
                        .bindShorts(":binding", (short) 10, (short) 12)
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
                assertArrayEquals(new Short[]{10, 12}, selected);
            }
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullArray() throws Exception {
            new TestBuilder().bindShorts(":binding", (short[]) null);
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullList() throws Exception {
            new TestBuilder().bindShorts(":binding", (List<Short>) null);

        }
    }

    public static class BindStrings {
        @Test
        public void values() throws Exception {
            final String[] expected = {"abc", "def"};
            try (Connection connection = db.getConnection()) {
                final Object[] selected = new TestBuilder()
                        .bindStrings(":binding", Arrays.asList(expected))
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
                assertArrayEquals(expected, selected);
            }
        }

        @Test
        public void valuesInArray() throws Exception {
            final String[] expected = {"abc", "def"};
            try (Connection connection = db.getConnection()) {
                final Object[] selected = new TestBuilder()
                        .bindStrings(":binding", expected)
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
                assertArrayEquals(expected, selected);
            }
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullArray() throws Exception {
            new TestBuilder().bindStrings(":binding", (String[]) null);
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullList() throws Exception {
            new TestBuilder().bindStrings(":binding", (List<String>) null);
        }
    }

    public static class BindTimes extends HasExpectedTimeOfDay {
        @Test
        public void values() throws Exception {
            final Time[] expected = {new Time(expectedTime), new Time(expectedTime)};
            try (Connection connection = db.getConnection()) {
                final Object[] selected = new TestBuilder()
                        .bindTimes(":binding", Arrays.asList(expected))
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
                assertArrayEquals(expected, selected);
            }
        }

        @Test
        public void valuesAndCal() throws Exception {
            final Time[] expected = {new Time(expectedTime), new Time(expectedTime)};
            try (Connection connection = db.getConnection()) {
                final Object[] selected = new TestBuilder()
                        .bindTimes(":binding", Arrays.asList(expected), GregorianCalendar.getInstance())
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
                assertArrayEquals(expected, selected);
            }
        }

        @Test(expected = IllegalArgumentException.class)
        public void Null() throws Exception {
            new TestBuilder().bindTimes(":binding", null);
        }

        @Test(expected = IllegalArgumentException.class)
        public void NullAndCalendar() throws Exception {
            new TestBuilder().bindTimes(":binding", null, GregorianCalendar.getInstance());

        }
    }

    public static class BindTimestamps extends HasExpectedTimeSinceEpoch {
        @Test
        public void values() throws Exception {
            final Timestamp[] expected = {new Timestamp(expectedTime), new Timestamp(expectedTime)};
            try (Connection connection = db.getConnection()) {
                final Object[] selected = new TestBuilder()
                        .bindTimestamps(":binding", Arrays.asList(expected))
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
                assertArrayEquals(expected, selected);
            }
        }

        @Test
        public void valuesAndCal() throws Exception {
            final Timestamp[] expected = {new Timestamp(expectedTime), new Timestamp(expectedTime)};
            try (Connection connection = db.getConnection()) {
                final Object[] selected = new TestBuilder()
                        .bindTimestamps(":binding", Arrays.asList(expected), GregorianCalendar.getInstance())
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
                assertArrayEquals(expected, selected);
            }
        }

        @Test(expected = IllegalArgumentException.class)
        public void Null() throws Exception {
            new TestBuilder().bindTimestamps(":binding", null);
        }

        @Test(expected = IllegalArgumentException.class)
        public void NullAndCalendar() throws Exception {
            new TestBuilder().bindTimestamps(":binding", null, GregorianCalendar.getInstance());

        }
    }

    private static class TestBuilder extends PositionalBindingsBuilder<TestBuilder> {

        TestBuilder() {
            this(CollectionBindingsBuilderTest.statement, PositionalBindings.empty());
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