package com.github.randyp.jdbj;

import com.github.randyp.jdbj.lambda.Binding;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class DefaultCollectionBindingsBuilder<E> extends CollectionBindingsBuilder<E> {

    public abstract E bindDefaultList(String name, List<Binding> bindings);

    public E bindDefaultBooleans(String name, Collection<Boolean> xs){
        checkNotNull(xs);
        final Function<Boolean, Binding> createBinding = x -> (Binding) preparedColumn -> preparedColumn.setBoolean(x);
        final List<Binding> bindings = xs.stream().map(createBinding).collect(Collectors.toList());
        return bindDefaultList(name, bindings);
    }

    public E bindDefaultBooleans(String name, boolean... xs){
        checkNotNull(xs);
        final List<Binding> bindings = new ArrayList<>();
        for (final boolean x : xs) {
            bindings.add(pc -> pc.setBooleanPrimitive(x));
        }
        return bindDefaultList(name, bindings);
    }

    public E bindDefaultBytes(String name, Collection<Byte> xs){
        checkNotNull(xs);
        final Function<Byte, Binding> createBinding = x -> (Binding) preparedColumn -> preparedColumn.setByte(x);
        final List<Binding> bindings = xs.stream().map(createBinding).collect(Collectors.toList());
        return bindDefaultList(name, bindings);
    }

    public E bindDefaultBytes(String name, byte... xs){
        checkNotNull(xs);
        final List<Binding> bindings = new ArrayList<>();
        for (final byte x : xs) {
            bindings.add(pc -> pc.setBytePrimitive(x));
        }
        return bindDefaultList(name, bindings);
    }

    public E bindDefaultDates(String name, Collection<Date> xs){
        checkNotNull(xs);
        final Function<Date, Binding> createBinding = x -> (Binding) preparedColumn -> preparedColumn.setDate(x);
        final List<Binding> bindings = xs.stream().map(createBinding).collect(Collectors.toList());
        return bindDefaultList(name, bindings);
    }

    public E bindDefaultDates(String name, Collection<Date> xs, Calendar calendar){
        checkNotNull(xs);
        final Function<Date, Binding> createBinding = x -> (Binding) preparedColumn -> preparedColumn.setDate(x, calendar);
        final List<Binding> bindings = xs.stream().map(createBinding).collect(Collectors.toList());
        return bindDefaultList(name, bindings);
    }

    public E bindDefaultDoubles(String name, Collection<Double> xs){
        checkNotNull(xs);
        final Function<Double, Binding> createBinding = x -> (Binding) preparedColumn -> preparedColumn.setDouble(x);
        final List<Binding> bindings = xs.stream().map(createBinding).collect(Collectors.toList());
        return bindDefaultList(name, bindings);
    }

    public E bindDefaultDoubles(String name, double... xs){
        checkNotNull(xs);
        final List<Binding> bindings = new ArrayList<>();
        for (final double x : xs) {
            bindings.add(pc -> pc.setDoublePrimitive(x));
        }
        return bindDefaultList(name, bindings);
    }

    public E bindDefaultFloats(String name, Collection<Float> xs){
        checkNotNull(xs);
        final Function<Float, Binding> createBinding = x -> (Binding) preparedColumn -> preparedColumn.setFloat(x);
        final List<Binding> bindings = xs.stream().map(createBinding).collect(Collectors.toList());
        return bindDefaultList(name, bindings);
    }

    public E bindDefaultFloats(String name, float... xs){
        checkNotNull(xs);
        final List<Binding> bindings = new ArrayList<>();
        for (final float x : xs) {
            bindings.add(pc -> pc.setFloatPrimitive(x));
        }
        return bindDefaultList(name, bindings);
    }

    public E bindDefaultIntegers(String name, Collection<Integer> xs){
        checkNotNull(xs);
        final Function<Integer, Binding> createBinding = x -> (Binding) preparedColumn -> preparedColumn.setInteger(x);
        final List<Binding> bindings = xs.stream().map(createBinding).collect(Collectors.toList());
        return bindDefaultList(name, bindings);
    }

    public E bindDefaultIntegers(String name, int... xs){
        checkNotNull(xs);
        final List<Binding> bindings = new ArrayList<>();
        for (final int x : xs) {
            bindings.add(pc -> pc.setInt(x));
        }
        return bindDefaultList(name, bindings);
    }

    public E bindDefaultLongs(String name, Collection<Long> xs){
        checkNotNull(xs);
        final Function<Long, Binding> createBinding = x -> (Binding) preparedColumn -> preparedColumn.setLong(x);
        final List<Binding> bindings = xs.stream().map(createBinding).collect(Collectors.toList());
        return bindDefaultList(name, bindings);
    }

    public E bindDefaultLongs(String name, long... xs){
        checkNotNull(xs);
        final List<Binding> bindings = new ArrayList<>();
        for (final long x : xs) {
            bindings.add(pc -> pc.setLongPrimitive(x));
        }
        return bindDefaultList(name, bindings);
    }

    public E bindDefaultObjects(String name, Collection<Object> xs){
        checkNotNull(xs);
        final Function<Object, Binding> createBinding = x -> (Binding) preparedColumn -> preparedColumn.setObject(x);
        final List<Binding> bindings = xs.stream().map(createBinding).collect(Collectors.toList());
        return bindDefaultList(name, bindings);
    }

    public E bindDefaultObjects(String name, Object... xs){
        checkNotNull(xs);
        return bindDefaultObjects(name, Arrays.asList(xs));
    }

    public E bindDefaultShorts(String name, Collection<Short> xs){
        checkNotNull(xs);
        final Function<Short, Binding> createBinding = x -> (Binding) preparedColumn -> preparedColumn.setShort(x);
        final List<Binding> bindings = xs.stream().map(createBinding).collect(Collectors.toList());
        return bindDefaultList(name, bindings);
    }

    public E bindDefaultShorts(String name, short... xs){
        checkNotNull(xs);
        final List<Binding> bindings = new ArrayList<>();
        for (final short x : xs) {
            bindings.add(pc -> pc.setShortPrimitive(x));
        }
        return bindDefaultList(name, bindings);
    }

    public E bindDefaultStrings(String name, Collection<String> xs){
        checkNotNull(xs);
        final Function<String, Binding> createBinding = x -> (Binding) preparedColumn -> preparedColumn.setString(x);
        final List<Binding> bindings = xs.stream().map(createBinding).collect(Collectors.toList());
        return bindDefaultList(name, bindings);
    }

    public E bindDefaultStrings(String name, String... xs){
        checkNotNull(xs);
        return bindDefaultStrings(name, Arrays.asList(xs));
    }

    public E bindDefaultTimes(String name, Collection<Time> xs){
        checkNotNull(xs);
        final Function<Time, Binding> createBinding = x -> (Binding) preparedColumn -> preparedColumn.setTime(x);
        final List<Binding> bindings = xs.stream().map(createBinding).collect(Collectors.toList());
        return bindDefaultList(name, bindings);
    }

    public E bindDefaultTimes(String name, Collection<Time> xs, Calendar calendar){
        checkNotNull(xs);
        final Function<Time, Binding> createBinding = x -> (Binding) preparedColumn -> preparedColumn.setTime(x, calendar);
        final List<Binding> bindings = xs.stream().map(createBinding).collect(Collectors.toList());
        return bindDefaultList(name, bindings);
    }

    public E bindDefaultTimestamps(String name, Collection<Timestamp> xs){
        checkNotNull(xs);
        final Function<Timestamp, Binding> createBinding = x -> (Binding) preparedColumn -> preparedColumn.setTimestamp(x);
        final List<Binding> bindings = xs.stream().map(createBinding).collect(Collectors.toList());
        return bindDefaultList(name, bindings);
    }

    public E bindDefaultTimestamps(String name, Collection<Timestamp> xs, Calendar calendar){
        checkNotNull(xs);
        final Function<Timestamp, Binding> createBinding = x -> (Binding) preparedColumn -> preparedColumn.setTimestamp(x, calendar);
        final List<Binding> bindings = xs.stream().map(createBinding).collect(Collectors.toList());
        return bindDefaultList(name, bindings);
    }
}