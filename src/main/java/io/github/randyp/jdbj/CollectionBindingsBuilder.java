package io.github.randyp.jdbj;

import io.github.randyp.jdbj.lambda.Binding;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Not intended for external creation. However, subclasses such as {@link ExecuteQuery} are intended for external use.
 * <p>
 * Binds {@link Collection} to a {@link PreparedColumn}. 
 * @param <P> prototype type since {@link Immutable}
 * @see PositionalBindingsBuilder           
 */
@Immutable
@ThreadSafe
interface CollectionBindingsBuilder<P> {

    String nullMessage = "xs cannot be null - consider using PreparedColumn.bindArray";

    P bindCollection(String name, List<Binding> bindings);

    default P bindBooleans(String name, Collection<Boolean> xs){
        Objects.requireNonNull(xs, nullMessage);
        final Function<Boolean, Binding> createBinding = x -> (Binding) preparedColumn -> preparedColumn.setBoolean(x);
        final List<Binding> bindings = xs.stream().map(createBinding).collect(Collectors.toList());
        return bindCollection(name, bindings);
    }

    default P bindBooleans(String name, boolean... xs){
        Objects.requireNonNull(xs, nullMessage);
        final List<Binding> bindings = new ArrayList<>();
        for (final boolean x : xs) {
            bindings.add(pc -> pc.setBooleanPrimitive(x));
        }
        return bindCollection(name, bindings);
    }

    default P bindBytes(String name, Collection<Byte> xs){
        Objects.requireNonNull(xs, nullMessage);
        final Function<Byte, Binding> createBinding = x -> (Binding) preparedColumn -> preparedColumn.setByte(x);
        final List<Binding> bindings = xs.stream().map(createBinding).collect(Collectors.toList());
        return bindCollection(name, bindings);
    }

    default P bindBytes(String name, byte... xs){
        Objects.requireNonNull(xs, nullMessage);
        final List<Binding> bindings = new ArrayList<>();
        for (final byte x : xs) {
            bindings.add(pc -> pc.setBytePrimitive(x));
        }
        return bindCollection(name, bindings);
    }

    default P bindDates(String name, Collection<Date> xs){
        Objects.requireNonNull(xs, nullMessage);
        final Function<Date, Binding> createBinding = x -> (Binding) preparedColumn -> preparedColumn.setDate(x);
        final List<Binding> bindings = xs.stream().map(createBinding).collect(Collectors.toList());
        return bindCollection(name, bindings);
    }

    default P bindDates(String name, Collection<Date> xs, Calendar calendar){
        Objects.requireNonNull(xs, nullMessage);
        final Function<Date, Binding> createBinding = x -> (Binding) preparedColumn -> preparedColumn.setDate(x, calendar);
        final List<Binding> bindings = xs.stream().map(createBinding).collect(Collectors.toList());
        return bindCollection(name, bindings);
    }

    default P bindDoubles(String name, Collection<Double> xs){
        Objects.requireNonNull(xs, nullMessage);
        final Function<Double, Binding> createBinding = x -> (Binding) preparedColumn -> preparedColumn.setDouble(x);
        final List<Binding> bindings = xs.stream().map(createBinding).collect(Collectors.toList());
        return bindCollection(name, bindings);
    }

    default P bindDoubles(String name, double... xs){
        Objects.requireNonNull(xs, nullMessage);
        final List<Binding> bindings = new ArrayList<>();
        for (final double x : xs) {
            bindings.add(pc -> pc.setDoublePrimitive(x));
        }
        return bindCollection(name, bindings);
    }

    default P bindFloats(String name, Collection<Float> xs){
        Objects.requireNonNull(xs, nullMessage);
        final Function<Float, Binding> createBinding = x -> (Binding) preparedColumn -> preparedColumn.setFloat(x);
        final List<Binding> bindings = xs.stream().map(createBinding).collect(Collectors.toList());
        return bindCollection(name, bindings);
    }

    default P bindFloats(String name, float... xs){
        Objects.requireNonNull(xs, nullMessage);
        final List<Binding> bindings = new ArrayList<>();
        for (final float x : xs) {
            bindings.add(pc -> pc.setFloatPrimitive(x));
        }
        return bindCollection(name, bindings);
    }

    default P bindIntegers(String name, Collection<Integer> xs){
        Objects.requireNonNull(xs, nullMessage);
        final Function<Integer, Binding> createBinding = x -> (Binding) preparedColumn -> preparedColumn.setInteger(x);
        final List<Binding> bindings = xs.stream().map(createBinding).collect(Collectors.toList());
        return bindCollection(name, bindings);
    }

    default P bindIntegers(String name, int... xs){
        Objects.requireNonNull(xs, nullMessage);
        final List<Binding> bindings = new ArrayList<>();
        for (final int x : xs) {
            bindings.add(pc -> pc.setInt(x));
        }
        return bindCollection(name, bindings);
    }

    default P bindLongs(String name, Collection<Long> xs){
        Objects.requireNonNull(xs, nullMessage);
        final Function<Long, Binding> createBinding = x -> (Binding) preparedColumn -> preparedColumn.setLong(x);
        final List<Binding> bindings = xs.stream().map(createBinding).collect(Collectors.toList());
        return bindCollection(name, bindings);
    }

    default P bindLongs(String name, long... xs){
        Objects.requireNonNull(xs, nullMessage);
        final List<Binding> bindings = new ArrayList<>();
        for (final long x : xs) {
            bindings.add(pc -> pc.setLongPrimitive(x));
        }
        return bindCollection(name, bindings);
    }

    default P bindObjects(String name, Collection<Object> xs){
        Objects.requireNonNull(xs, nullMessage);
        final Function<Object, Binding> createBinding = x -> (Binding) preparedColumn -> preparedColumn.setObject(x);
        final List<Binding> bindings = xs.stream().map(createBinding).collect(Collectors.toList());
        return bindCollection(name, bindings);
    }

    default P bindObjects(String name, Object... xs){
        Objects.requireNonNull(xs, nullMessage);
        return bindObjects(name, Arrays.asList(xs));
    }

    default P bindShorts(String name, Collection<Short> xs){
        Objects.requireNonNull(xs, nullMessage);
        final Function<Short, Binding> createBinding = x -> (Binding) preparedColumn -> preparedColumn.setShort(x);
        final List<Binding> bindings = xs.stream().map(createBinding).collect(Collectors.toList());
        return bindCollection(name, bindings);
    }

    default P bindShorts(String name, short... xs){
        Objects.requireNonNull(xs, nullMessage);
        final List<Binding> bindings = new ArrayList<>();
        for (final short x : xs) {
            bindings.add(pc -> pc.setShortPrimitive(x));
        }
        return bindCollection(name, bindings);
    }

    default P bindStrings(String name, Collection<String> xs){
        Objects.requireNonNull(xs, nullMessage);
        final Function<String, Binding> createBinding = x -> (Binding) preparedColumn -> preparedColumn.setString(x);
        final List<Binding> bindings = xs.stream().map(createBinding).collect(Collectors.toList());
        return bindCollection(name, bindings);
    }

    default P bindStrings(String name, String... xs){
        Objects.requireNonNull(xs, nullMessage);
        return bindStrings(name, Arrays.asList(xs));
    }

    default P bindTimes(String name, Collection<Time> xs){
        Objects.requireNonNull(xs, nullMessage);
        final Function<Time, Binding> createBinding = x -> (Binding) preparedColumn -> preparedColumn.setTime(x);
        final List<Binding> bindings = xs.stream().map(createBinding).collect(Collectors.toList());
        return bindCollection(name, bindings);
    }

    default P bindTimes(String name, Collection<Time> xs, Calendar calendar){
        Objects.requireNonNull(xs, nullMessage);
        final Function<Time, Binding> createBinding = x -> (Binding) preparedColumn -> preparedColumn.setTime(x, calendar);
        final List<Binding> bindings = xs.stream().map(createBinding).collect(Collectors.toList());
        return bindCollection(name, bindings);
    }

    default P bindTimestamps(String name, Collection<Timestamp> xs){
        Objects.requireNonNull(xs, nullMessage);
        final Function<Timestamp, Binding> createBinding = x -> (Binding) preparedColumn -> preparedColumn.setTimestamp(x);
        final List<Binding> bindings = xs.stream().map(createBinding).collect(Collectors.toList());
        return bindCollection(name, bindings);
    }

    default P bindTimestamps(String name, Collection<Timestamp> xs, Calendar calendar){
        Objects.requireNonNull(xs, nullMessage);
        final Function<Timestamp, Binding> createBinding = x -> (Binding) preparedColumn -> preparedColumn.setTimestamp(x, calendar);
        final List<Binding> bindings = xs.stream().map(createBinding).collect(Collectors.toList());
        return bindCollection(name, bindings);
    }
}
