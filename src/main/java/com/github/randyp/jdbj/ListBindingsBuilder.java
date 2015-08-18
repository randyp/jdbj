package com.github.randyp.jdbj;

import com.github.randyp.jdbj.lambda.Binding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class ListBindingsBuilder<E> {

    public abstract E bindList(String name, List<Binding> bindings);

    public E bindLongs(String name, long... xs){
        checkNotNull(xs);
        final List<Binding> bindings = new ArrayList<>();
        for (final long x : xs) {
            bindings.add(pc -> pc.setLong(x));
        }

        return bindList(name, bindings);
    }

    public E bindStrings(String name, Collection<String> xs){
        checkNotNull(xs);
        final Function<String, Binding> createBinding = x -> (Binding) preparedColumn -> preparedColumn.setString(x);
        final List<Binding> bindings = xs.stream().map(createBinding).collect(Collectors.toList());
        return bindList(name, bindings);
    }

    public E bindStrings(String name, String... xs){
        checkNotNull(xs);
        return bindStrings(name, Arrays.asList(xs));
    }

    public void checkNotNull(Object xs) {
        if (xs == null) {
            throw new IllegalArgumentException("xs cannot be null - consider using PreparedColumn.bindArray");
        }
    }
}
