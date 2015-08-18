package com.github.randyp.jdbj;

import com.github.randyp.jdbj.lambda.Binding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class DefaultListBindingsBuilder<E> extends ListBindingsBuilder<E> {

    public abstract E bindDefaultList(String name, List<Binding> bindings);

    public E bindDefaultLongs(String name, long... xs){
        checkNotNull(xs);
        final List<Binding> bindings = new ArrayList<>();
        for (final long x : xs) {
            bindings.add(pc -> pc.setLong(x));
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
}
