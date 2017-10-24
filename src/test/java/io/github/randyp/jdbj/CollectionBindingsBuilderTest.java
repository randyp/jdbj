package io.github.randyp.jdbj;

import io.github.randyp.jdbj.db.h2_1_4.H2Rule;
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
public class CollectionBindingsBuilderTest {

    @ClassRule
    public static final H2Rule db = new H2Rule();

    public static class BindList {
        @Test(expected = NullPointerException.class)
        public void nullName() throws Exception {
            new SimpleBuilder().bindCollection(null, Collections.singletonList(pc -> pc.setInt(1)));
        }

        @Test(expected = NullPointerException.class)
        public void nulList() throws Exception {
            new SimpleBuilder().bindCollection(":binding", null);
        }
    }

    public static class BindBooleans {
        @Test
        public void values() throws Exception {
            final Boolean[] expected = {Boolean.FALSE, Boolean.TRUE};
            final Object[] selected = new SimpleBuilder()
                    .bindBooleans(":binding", Arrays.asList(expected))
                    .execute(db, rs -> (Object[]) rs.getObject(1));
            assertArrayEquals(expected, selected);
        }

        @Test
        public void valuesInArray() throws Exception {
            final Object[] selected = new SimpleBuilder()
                    .bindBooleans(":binding", true, false)
                    .execute(db, rs -> (Object[]) rs.getObject(1));
            assertArrayEquals(new Boolean[]{Boolean.TRUE, Boolean.FALSE}, selected);
        }

        @Test(expected = NullPointerException.class)
        public void nullArray() throws Exception {
            new SimpleBuilder().bindBooleans(":binding", (boolean[]) null);
        }

        @Test(expected = NullPointerException.class)
        public void nullList() throws Exception {
            new SimpleBuilder().bindBooleans(":binding", (List<Boolean>) null);
        }
    }

    public static class BindBytes {
        @Test
        public void values() throws Exception {
            final Byte[] expected = {10, 11};
            final Object[] selected = new SimpleBuilder()
                    .bindBytes(":binding", Arrays.asList(expected))
                    .execute(db, rs -> (Object[]) rs.getObject(1));
            assertArrayEquals(expected, selected);
        }

        @Test
        public void valuesInArray() throws Exception {
            final Object[] selected = new SimpleBuilder()
                    .bindBytes(":binding", (byte) 10, (byte) 12)
                    .execute(db, rs -> (Object[]) rs.getObject(1));
            assertArrayEquals(new Byte[]{10, 12}, selected);
        }

        @Test(expected = NullPointerException.class)
        public void nullArray() throws Exception {
            new SimpleBuilder().bindBytes(":binding", (byte[]) null);
        }

        @Test(expected = NullPointerException.class)
        public void nullList() throws Exception {
            new SimpleBuilder().bindBytes(":binding", (List<Byte>) null);

        }
    }

    public static class BindDates extends HasExpectedTimeSinceEpoch {
        @Test
        public void values() throws Exception {
            final Date[] expected = {new Date(expectedTime), new Date(expectedTime)};
            final Object[] selected = new SimpleBuilder()
                    .bindDates(":binding", Arrays.asList(expected))
                    .execute(db, rs -> (Object[]) rs.getObject(1));
            assertArrayEquals(expected, selected);
        }

        @Test
        public void valuesAndCal() throws Exception {
            final Date[] expected = {new Date(expectedTime), new Date(expectedTime)};
            final Object[] selected = new SimpleBuilder()
                    .bindDates(":binding", Arrays.asList(expected), GregorianCalendar.getInstance())
                    .execute(db, rs -> (Object[]) rs.getObject(1));
            assertArrayEquals(expected, selected);
        }

        @Test(expected = NullPointerException.class)
        public void Null() throws Exception {
            new SimpleBuilder().bindDates(":binding", null);
        }

        @Test(expected = NullPointerException.class)
        public void NullAndCalendar() throws Exception {
            new SimpleBuilder().bindDates(":binding", null, GregorianCalendar.getInstance());

        }
    }

    public static class BindDoubles {
        @Test
        public void values() throws Exception {
            final Double[] expected = {10.0, 12.0};
            final Object[] selected = new SimpleBuilder()
                    .bindDoubles(":binding", Arrays.asList(expected))
                    .execute(db, rs -> (Object[]) rs.getObject(1));
            assertArrayEquals(expected, selected);
        }

        @Test
        public void valuesInArray() throws Exception {
            final Object[] selected = new SimpleBuilder()
                    .bindDoubles(":binding", 10.0, 12.0)
                    .execute(db, rs -> (Object[]) rs.getObject(1));
            assertArrayEquals(new Double[]{10.0, 12.0}, selected);
        }

        @Test(expected = NullPointerException.class)
        public void nullArray() throws Exception {
            new SimpleBuilder().bindDoubles(":binding", (double[]) null);
        }

        @Test(expected = NullPointerException.class)
        public void nullList() throws Exception {
            new SimpleBuilder().bindDoubles(":binding", (List<Double>) null);

        }
    }

    public static class BindFloats {
        @Test
        public void values() throws Exception {
            final Float[] expected = {10.0f, 12.0f};
            final Object[] selected = new SimpleBuilder()
                    .bindFloats(":binding", Arrays.asList(expected))
                    .execute(db, rs -> (Object[]) rs.getObject(1));
            assertArrayEquals(expected, selected);
        }

        @Test
        public void valuesInArray() throws Exception {
            final Object[] selected = new SimpleBuilder()
                    .bindFloats(":binding", 10.0f, 12.0f)
                    .execute(db, rs -> (Object[]) rs.getObject(1));
            assertArrayEquals(new Float[]{10.0f, 12.0f}, selected);
        }

        @Test(expected = NullPointerException.class)
        public void nullArray() throws Exception {
            new SimpleBuilder().bindFloats(":binding", (float[]) null);
        }

        @Test(expected = NullPointerException.class)
        public void nullList() throws Exception {
            new SimpleBuilder().bindFloats(":binding", (List<Float>) null);

        }
    }

    public static class BindIntegers {
        @Test
        public void values() throws Exception {
            final Integer[] expected = {10, 12};
            final Object[] selected = new SimpleBuilder()
                    .bindIntegers(":binding", Arrays.asList(expected))
                    .execute(db, rs -> (Object[]) rs.getObject(1));
            assertArrayEquals(expected, selected);
        }

        @Test
        public void valuesInArray() throws Exception {
            final Object[] selected = new SimpleBuilder()
                    .bindIntegers(":binding", 10, 12)
                    .execute(db, rs -> (Object[]) rs.getObject(1));
            assertArrayEquals(new Integer[]{10, 12}, selected);
        }

        @Test(expected = NullPointerException.class)
        public void nullArray() throws Exception {
            new SimpleBuilder().bindIntegers(":binding", (int[]) null);
        }

        @Test(expected = NullPointerException.class)
        public void nullList() throws Exception {
            new SimpleBuilder().bindIntegers(":binding", (List<Integer>) null);
        }
    }

    public static class BindLongs {

        @Test
        public void values() throws Exception {
            final Object[] selected = new SimpleBuilder()
                    .bindLongs(":binding", Arrays.asList(152L, 51L))
                    .execute(db, rs -> (Object[]) rs.getObject(1));
            assertArrayEquals(new Long[]{152L, 51L}, selected);
        }

        @Test
        public void valuesInArray() throws Exception {
            final Object[] selected = new SimpleBuilder()
                    .bindLongs(":binding", 152L, 51L)
                    .execute(db, rs -> (Object[]) rs.getObject(1));
            assertArrayEquals(new Long[]{152L, 51L}, selected);
        }

        @Test(expected = NullPointerException.class)
        public void nullArray() throws Exception {
            new SimpleBuilder()
                    .bindLongs(":binding", (long[]) null)
                    .execute(db, rs -> rs.getObject(1));
        }

        @Test(expected = NullPointerException.class)
        public void nullList() throws Exception {
            new SimpleBuilder()
                    .bindLongs(":binding", (List<Long>) null)
                    .execute(db, rs -> rs.getObject(1));
        }
    }

    public static class BindObjects {
        @Test
        public void values() throws Exception {
            final String[] expected = {"abc", "def"};
            final Object[] selected = new SimpleBuilder()
                    .bindObjects(":binding", Arrays.asList(expected))
                    .execute(db, rs -> (Object[]) rs.getObject(1));
            assertArrayEquals(expected, selected);
        }

        @Test
        public void valuesInArray() throws Exception {
            final String[] expected = {"abc", "def"};
            final Object[] selected = new SimpleBuilder()
                    .bindObjects(":binding", expected)
                    .execute(db, rs -> (Object[]) rs.getObject(1));
            assertArrayEquals(expected, selected);
        }

        @Test(expected = NullPointerException.class)
        public void nullArray() throws Exception {
            new SimpleBuilder().bindObjects(":binding", (Object[]) null);
        }

        @Test(expected = NullPointerException.class)
        public void nullList() throws Exception {
            new SimpleBuilder().bindObjects(":binding", (List<Object>) null);
        }
    }

    public static class BindShorts {
        @Test
        public void values() throws Exception {
            final Short[] expected = {10, 12};
            final Object[] selected = new SimpleBuilder()
                    .bindShorts(":binding", Arrays.asList(expected))
                    .execute(db, rs -> (Object[]) rs.getObject(1));
            assertArrayEquals(expected, selected);
        }

        @Test
        public void valuesInArray() throws Exception {
            final Object[] selected = new SimpleBuilder()
                    .bindShorts(":binding", (short) 10, (short) 12)
                    .execute(db, rs -> (Object[]) rs.getObject(1));
            assertArrayEquals(new Short[]{10, 12}, selected);
        }

        @Test(expected = NullPointerException.class)
        public void nullArray() throws Exception {
            new SimpleBuilder().bindShorts(":binding", (short[]) null);
        }

        @Test(expected = NullPointerException.class)
        public void nullList() throws Exception {
            new SimpleBuilder().bindShorts(":binding", (List<Short>) null);

        }
    }

    public static class BindStrings {
        @Test
        public void values() throws Exception {
            final String[] expected = {"abc", "def"};
            final Object[] selected = new SimpleBuilder()
                    .bindStrings(":binding", Arrays.asList(expected))
                    .execute(db, rs -> (Object[]) rs.getObject(1));
            assertArrayEquals(expected, selected);
        }

        @Test
        public void valuesInArray() throws Exception {
            final String[] expected = {"abc", "def"};
            final Object[] selected = new SimpleBuilder()
                    .bindStrings(":binding", expected)
                    .execute(db, rs -> (Object[]) rs.getObject(1));
            assertArrayEquals(expected, selected);
        }

        @Test(expected = NullPointerException.class)
        public void nullArray() throws Exception {
            new SimpleBuilder().bindStrings(":binding", (String[]) null);
        }

        @Test(expected = NullPointerException.class)
        public void nullList() throws Exception {
            new SimpleBuilder().bindStrings(":binding", (List<String>) null);
        }
    }

    public static class BindTimes extends HasExpectedTimeOfDay {
        @Test
        public void values() throws Exception {
            final Time[] expected = {new Time(expectedMillis), new Time(expectedMillis)};
            final Object[] selected = new SimpleBuilder()
                    .bindTimes(":binding", Arrays.asList(expected))
                    .execute(db, rs -> (Object[]) rs.getObject(1));
            assertArrayEquals(expected, selected);
        }

        @Test
        public void valuesAndCal() throws Exception {
            final Time[] expected = {new Time(expectedMillis), new Time(expectedMillis)};
            final Object[] selected = new SimpleBuilder()
                    .bindTimes(":binding", Arrays.asList(expected), GregorianCalendar.getInstance())
                    .execute(db, rs -> (Object[]) rs.getObject(1));
            assertArrayEquals(expected, selected);
        }

        @Test(expected = NullPointerException.class)
        public void Null() throws Exception {
            new SimpleBuilder().bindTimes(":binding", null);
        }

        @Test(expected = NullPointerException.class)
        public void NullAndCalendar() throws Exception {
            new SimpleBuilder().bindTimes(":binding", null, GregorianCalendar.getInstance());

        }
    }

    public static class BindTimestamps extends HasExpectedTimeSinceEpoch {
        @Test
        public void values() throws Exception {
            final Timestamp[] expected = {new Timestamp(expectedTime), new Timestamp(expectedTime)};
            final Object[] selected = new SimpleBuilder()
                    .bindTimestamps(":binding", Arrays.asList(expected))
                    .execute(db, rs -> (Object[]) rs.getObject(1));
            assertArrayEquals(expected, selected);
        }

        @Test
        public void valuesAndCal() throws Exception {
            final Timestamp[] expected = {new Timestamp(expectedTime), new Timestamp(expectedTime)};
            final Object[] selected = new SimpleBuilder()
                    .bindTimestamps(":binding", Arrays.asList(expected), GregorianCalendar.getInstance())
                    .execute(db, rs -> (Object[]) rs.getObject(1));
            assertArrayEquals(expected, selected);
        }

        @Test(expected = NullPointerException.class)
        public void Null() throws Exception {
            new SimpleBuilder().bindTimestamps(":binding", null);
        }

        @Test(expected = NullPointerException.class)
        public void NullAndCalendar() throws Exception {
            new SimpleBuilder().bindTimestamps(":binding", null, GregorianCalendar.getInstance());
        }
    }

    static class SimpleBuilder extends io.github.randyp.jdbj.SimpleBuilder {
        public SimpleBuilder() {
            super(NamedParameterStatement.make("SELECT :binding as bound"));
        }
    }
}
