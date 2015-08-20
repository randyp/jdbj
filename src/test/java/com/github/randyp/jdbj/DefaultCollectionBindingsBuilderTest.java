package com.github.randyp.jdbj;

import com.github.randyp.jdbj.db.h2_1_4.H2Rule;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;

@RunWith(Enclosed.class)
public class DefaultCollectionBindingsBuilderTest {

    @ClassRule
    public static final H2Rule db = new H2Rule();

    public static class DefaultBindList {
        @Test(expected = IllegalArgumentException.class)
        public void nullName() throws Exception {
            new SimpleBuilder().bindDefaultList(null, Collections.singletonList(pc -> pc.setInt(1)));
        }

        @Test(expected = IllegalArgumentException.class)
        public void nulList() throws Exception {
            new SimpleBuilder().bindDefaultList(":binding", null);
        }

        @Test(expected = IllegalArgumentException.class)
        public void bindingNotInStatement() throws Exception {
            new SimpleBuilder().bindDefaultList(":not_binding", null);
        }
    }

    public static class BindBooleans {
        @Test
        public void values() throws Exception {
            final Boolean[] expected = {Boolean.FALSE, Boolean.TRUE};
            final Object[] selected = new SimpleBuilder()
                    .bindDefaultBooleans(":binding", Arrays.asList(expected))
                    .execute(db, rs -> (Object[]) rs.getObject(1));
            assertArrayEquals(expected, selected);
        }

        @Test
        public void valuesInArray() throws Exception {
            final Object[] selected = new SimpleBuilder()
                    .bindDefaultBooleans(":binding", true, false)
                    .execute(db, rs -> (Object[]) rs.getObject(1));
            assertArrayEquals(new Boolean[]{Boolean.TRUE, Boolean.FALSE}, selected);
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullArray() throws Exception {
            new SimpleBuilder().bindDefaultBooleans(":binding", (boolean[]) null);
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullList() throws Exception {
            new SimpleBuilder().bindDefaultBooleans(":binding", (List<Boolean>) null);
        }
    }

    public static class BindBytes {
        @Test
        public void values() throws Exception {
            final Byte[] expected = {10, 11};
            final Object[] selected = new SimpleBuilder()
                    .bindDefaultBytes(":binding", Arrays.asList(expected))
                    .execute(db, rs -> (Object[]) rs.getObject(1));
            assertArrayEquals(expected, selected);
        }

        @Test
        public void valuesInArray() throws Exception {
            final Object[] selected = new SimpleBuilder()
                    .bindDefaultBytes(":binding", (byte) 10, (byte) 12)
                    .execute(db, rs -> (Object[]) rs.getObject(1));
            assertArrayEquals(new Byte[]{10, 12}, selected);
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullArray() throws Exception {
            new SimpleBuilder().bindDefaultBytes(":binding", (byte[]) null);
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullList() throws Exception {
            new SimpleBuilder().bindDefaultBytes(":binding", (List<Byte>) null);

        }
    }

    public static class BindDates extends HasExpectedTimeSinceEpoch {
        @Test
        public void values() throws Exception {
            final Date[] expected = {new Date(expectedTime), new Date(expectedTime)};
            final Object[] selected = new SimpleBuilder()
                    .bindDefaultDates(":binding", Arrays.asList(expected))
                    .execute(db, rs -> (Object[]) rs.getObject(1));
            assertArrayEquals(expected, selected);
        }

        @Test
        public void valuesAndCal() throws Exception {
            final Date[] expected = {new Date(expectedTime), new Date(expectedTime)};
            final Object[] selected = new SimpleBuilder()
                    .bindDefaultDates(":binding", Arrays.asList(expected), GregorianCalendar.getInstance())
                    .execute(db, rs -> (Object[]) rs.getObject(1));
            assertArrayEquals(expected, selected);
        }

        @Test(expected = IllegalArgumentException.class)
        public void Null() throws Exception {
            new SimpleBuilder().bindDefaultDates(":binding", null);
        }

        @Test(expected = IllegalArgumentException.class)
        public void NullAndCalendar() throws Exception {
            new SimpleBuilder().bindDefaultDates(":binding", null, GregorianCalendar.getInstance());

        }
    }

    public static class BindDoubles {
        @Test
        public void values() throws Exception {
            final Double[] expected = {10.0, 12.0};
            final Object[] selected = new SimpleBuilder()
                    .bindDefaultDoubles(":binding", Arrays.asList(expected))
                    .execute(db, rs -> (Object[]) rs.getObject(1));
            assertArrayEquals(expected, selected);
        }

        @Test
        public void valuesInArray() throws Exception {
            final Object[] selected = new SimpleBuilder()
                    .bindDefaultDoubles(":binding", 10.0, 12.0)
                    .execute(db, rs -> (Object[]) rs.getObject(1));
            assertArrayEquals(new Double[]{10.0, 12.0}, selected);
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullArray() throws Exception {
            new SimpleBuilder().bindDefaultDoubles(":binding", (double[]) null);
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullList() throws Exception {
            new SimpleBuilder().bindDefaultDoubles(":binding", (List<Double>) null);

        }
    }

    public static class BindFloats {
        @Test
        public void values() throws Exception {
            final Float[] expected = {10.0f, 12.0f};
            final Object[] selected = new SimpleBuilder()
                    .bindDefaultFloats(":binding", Arrays.asList(expected))
                    .execute(db, rs -> (Object[]) rs.getObject(1));
            assertArrayEquals(expected, selected);
        }

        @Test
        public void valuesInArray() throws Exception {
            final Object[] selected = new SimpleBuilder()
                    .bindDefaultFloats(":binding", 10.0f, 12.0f)
                    .execute(db, rs -> (Object[]) rs.getObject(1));
            assertArrayEquals(new Float[]{10.0f, 12.0f}, selected);
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullArray() throws Exception {
            new SimpleBuilder().bindDefaultFloats(":binding", (float[]) null);
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullList() throws Exception {
            new SimpleBuilder().bindDefaultFloats(":binding", (List<Float>) null);

        }
    }

    public static class BindIntegers {
        @Test
        public void values() throws Exception {
            final Integer[] expected = {10, 12};
            final Object[] selected = new SimpleBuilder()
                    .bindDefaultIntegers(":binding", Arrays.asList(expected))
                    .execute(db, rs -> (Object[]) rs.getObject(1));
            assertArrayEquals(expected, selected);
        }

        @Test
        public void valuesInArray() throws Exception {
            final Object[] selected = new SimpleBuilder()
                    .bindDefaultIntegers(":binding", 10, 12)
                    .execute(db, rs -> (Object[]) rs.getObject(1));
            assertArrayEquals(new Integer[]{10, 12}, selected);
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullArray() throws Exception {
            new SimpleBuilder().bindDefaultIntegers(":binding", (int[]) null);
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullList() throws Exception {
            new SimpleBuilder().bindDefaultIntegers(":binding", (List<Integer>) null);
        }
    }

    public static class BindLongs {

        @Test
        public void values() throws Exception {
            final Object[] selected = new SimpleBuilder()
                    .bindDefaultLongs(":binding", Arrays.asList(152L, 51L))
                    .execute(db, rs -> (Object[]) rs.getObject(1));
            assertArrayEquals(new Long[]{152L, 51L}, selected);
        }

        @Test
        public void valuesInArray() throws Exception {
            final Object[] selected = new SimpleBuilder()
                    .bindDefaultLongs(":binding", 152L, 51L)
                    .execute(db, rs -> (Object[]) rs.getObject(1));
            assertArrayEquals(new Long[]{152L, 51L}, selected);
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullArray() throws Exception {
            new SimpleBuilder()
                    .bindDefaultLongs(":binding", (long[]) null)
                    .execute(db, rs -> rs.getObject(1));
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullList() throws Exception {
            new SimpleBuilder()
                    .bindDefaultLongs(":binding", (List<Long>) null)
                    .execute(db, rs -> rs.getObject(1));
        }
    }

    public static class BindObjects {
        @Test
        public void values() throws Exception {
            final String[] expected = {"abc", "def"};
            final Object[] selected = new SimpleBuilder()
                    .bindDefaultObjects(":binding", Arrays.asList(expected))
                    .execute(db, rs -> (Object[]) rs.getObject(1));
            assertArrayEquals(expected, selected);
        }

        @Test
        public void valuesInArray() throws Exception {
            final String[] expected = {"abc", "def"};
            final Object[] selected = new SimpleBuilder()
                    .bindDefaultObjects(":binding", expected)
                    .execute(db, rs -> (Object[]) rs.getObject(1));
            assertArrayEquals(expected, selected);
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullArray() throws Exception {
            new SimpleBuilder().bindDefaultObjects(":binding", (Object[]) null);
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullList() throws Exception {
            new SimpleBuilder().bindDefaultObjects(":binding", (List<Object>) null);
        }
    }

    public static class BindShorts {
        @Test
        public void values() throws Exception {
            final Short[] expected = {10, 12};
            final Object[] selected = new SimpleBuilder()
                    .bindDefaultShorts(":binding", Arrays.asList(expected))
                    .execute(db, rs -> (Object[]) rs.getObject(1));
            assertArrayEquals(expected, selected);
        }

        @Test
        public void valuesInArray() throws Exception {
            final Object[] selected = new SimpleBuilder()
                    .bindDefaultShorts(":binding", (short) 10, (short) 12)
                    .execute(db, rs -> (Object[]) rs.getObject(1));
            assertArrayEquals(new Short[]{10, 12}, selected);
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullArray() throws Exception {
            new SimpleBuilder().bindDefaultShorts(":binding", (short[]) null);
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullList() throws Exception {
            new SimpleBuilder().bindDefaultShorts(":binding", (List<Short>) null);
        }
    }

    public static class BindStrings {
        @Test
        public void values() throws Exception {
            final String[] expected = {"abc", "def"};
            final Object[] selected = new SimpleBuilder()
                    .bindDefaultStrings(":binding", Arrays.asList(expected))
                    .execute(db, rs -> (Object[]) rs.getObject(1));
            assertArrayEquals(expected, selected);
        }

        @Test
        public void valuesInArray() throws Exception {
            final String[] expected = {"abc", "def"};
            final Object[] selected = new SimpleBuilder()
                    .bindDefaultStrings(":binding", expected)
                    .execute(db, rs -> (Object[]) rs.getObject(1));
            assertArrayEquals(expected, selected);
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullArray() throws Exception {
            new SimpleBuilder().bindDefaultStrings(":binding", (String[]) null);
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullList() throws Exception {
            new SimpleBuilder().bindDefaultStrings(":binding", (List<String>) null);
        }
    }

    public static class BindTimes extends HasExpectedTimeOfDay {
        @Test
        public void values() throws Exception {
            final Time[] expected = {new Time(expectedMillis), new Time(expectedMillis)};
            final Object[] selected = new SimpleBuilder()
                    .bindDefaultTimes(":binding", Arrays.asList(expected))
                    .execute(db, rs -> (Object[]) rs.getObject(1));
            assertArrayEquals(expected, selected);
        }

        @Test
        public void valuesAndCal() throws Exception {
            final Time[] expected = {new Time(expectedMillis), new Time(expectedMillis)};
            final Object[] selected = new SimpleBuilder()
                    .bindDefaultTimes(":binding", Arrays.asList(expected), GregorianCalendar.getInstance())
                    .execute(db, rs -> (Object[]) rs.getObject(1));
            assertArrayEquals(expected, selected);
        }

        @Test(expected = IllegalArgumentException.class)
        public void Null() throws Exception {
            new SimpleBuilder().bindDefaultTimes(":binding", null);
        }

        @Test(expected = IllegalArgumentException.class)
        public void NullAndCalendar() throws Exception {
            new SimpleBuilder().bindDefaultTimes(":binding", null, GregorianCalendar.getInstance());
        }
    }

    public static class BindTimestamps extends HasExpectedTimeSinceEpoch {
        @Test
        public void values() throws Exception {
            final Timestamp[] expected = {new Timestamp(expectedTime), new Timestamp(expectedTime)};
            final Object[] selected = new SimpleBuilder()
                    .bindDefaultTimestamps(":binding", Arrays.asList(expected))
                    .execute(db, rs -> (Object[]) rs.getObject(1));
            assertArrayEquals(expected, selected);
        }

        @Test
        public void valuesAndCal() throws Exception {
            final Timestamp[] expected = {new Timestamp(expectedTime), new Timestamp(expectedTime)};
            final Object[] selected = new SimpleBuilder()
                    .bindDefaultTimestamps(":binding", Arrays.asList(expected), GregorianCalendar.getInstance())
                    .execute(db, rs -> (Object[]) rs.getObject(1));
            assertArrayEquals(expected, selected);
        }

        @Test(expected = IllegalArgumentException.class)
        public void Null() throws Exception {
            new SimpleBuilder().bindDefaultTimestamps(":binding", null);
        }

        @Test(expected = IllegalArgumentException.class)
        public void NullAndCalendar() throws Exception {
            new SimpleBuilder().bindDefaultTimestamps(":binding", null, GregorianCalendar.getInstance());

        }
    }

    static class SimpleBuilder extends com.github.randyp.jdbj.test.SimpleBuilder {
        public SimpleBuilder() {
            super(NamedParameterStatement.make("SELECT :binding as bound"));
        }
    }
}