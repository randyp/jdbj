package com.github.randyp.jdbj;

import jdk.nashorn.internal.ir.annotations.Immutable;
import com.github.randyp.jdbj.lambda.Binding;

import javax.annotation.concurrent.ThreadSafe;
import java.util.*;

/**
 * Data structure to store bindings for named parameters.
 * <p>
 * Will except if you try to bind for the same name multiple times.
 * <p>
 * Is {@link Immutable}, you (re)assign to a variable after each call to {@link PositionalBindings#bind(String, Binding)} and {@link PositionalBindings#bindCollection(String, List)}.
 */
@Immutable
@ThreadSafe
public class PositionalBindings implements Bindings, ValueBindingsBuilder<PositionalBindings>, CollectionBindingsBuilder<PositionalBindings> {

    private final Map<String, Binding> valueBindings;
    private final Map<String, List<Binding>> collectionBindings;
    private final Set<String> keys;

    /**
     * New Empty PositionalBindings
     */
    public PositionalBindings(){
        this(new HashMap<>(), new HashMap<>());
    }

    PositionalBindings(Map<String, Binding> valueBindings,
                               Map<String, List<Binding>> collectionBindings) {
        this.valueBindings = valueBindings;
        this.collectionBindings = collectionBindings;

        {
            final Set<String> keys = new HashSet<>(valueBindings.keySet());
            keys.addAll(collectionBindings.keySet());
            this.keys = Collections.unmodifiableSet(keys);
        }
    }

    public PositionalBindings addAll(PositionalBindings bindings) {
        if (bindings == null) {
            throw new IllegalArgumentException("bindings cannot be null");
        }
        PositionalBindings newBindings = this;
        for (Map.Entry<String, Binding> entry : bindings.valueBindings.entrySet()) {
            newBindings = newBindings.bind(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, List<Binding>> entry : bindings.collectionBindings.entrySet()) {
            newBindings = newBindings.bindCollection(entry.getKey(), entry.getValue());
        }
        
        return newBindings;
    }

    public PositionalBindings addAll(ValueBindings bindings) {
        if (bindings == null) {
            throw new IllegalArgumentException("bindings cannot be null");
        }
        PositionalBindings newBindings = this;
        for (Map.Entry<String, Binding> entry : bindings.asMap().entrySet()) {
            newBindings = newBindings.bind(entry.getKey(), entry.getValue());
        }
        return newBindings;
    }
    
    @Override
    public boolean containsBinding(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name cannot be null");
        }
        return valueBindings.containsKey(name) || collectionBindings.containsKey(name);
    }

    @Override
    public PositionalBindings bind(String name, Binding binding) {
        if (binding == null) {
            throw new IllegalArgumentException("binding cannot be null");
        }

        if (containsBinding(name)) {
            throw new IllegalArgumentException("named parameter \"" + name + "\" already has a binding");
        }

        final Map<String, Binding> newValueBindings = new HashMap<>(valueBindings);
        newValueBindings.put(name, binding);

        return new PositionalBindings(
                newValueBindings,
                collectionBindings
        );
    }

    @Override
    public PositionalBindings bindCollection(String name, List<Binding> bindings) {
        if (name == null) {
            throw new IllegalArgumentException("name cannot be null");
        }
        checkBindings(bindings);

        if (containsBinding(name)) {
            throw new IllegalArgumentException("named parameter \"" + name + "\" already has a binding");
        }

        final Map<String, List<Binding>> newListBindings = new HashMap<>(collectionBindings);
        newListBindings.put(name, bindings);

        return new PositionalBindings(
                valueBindings,
                newListBindings
        );
    }

    @Override
    public PositionalBinding get(String namedParameter) {
        final PositionalBinding toReturn;
        if (valueBindings.containsKey(namedParameter)) {
            toReturn = new ValueBinding(valueBindings.get(namedParameter));
        } else if (collectionBindings.containsKey(namedParameter)) {
            toReturn = new ListBinding(collectionBindings.get(namedParameter));
        } else {
            throw new IllegalArgumentException("no such binding: \"" + namedParameter + "\"");
        }
        return toReturn;
    }

    @Override
    public Set<String> keys() {
        return keys;
    }

    private static void checkBindings(List<Binding> bindings) {
        if (bindings == null) {
            throw new IllegalArgumentException("bindings cannot be null");
        }
        for (Binding binding : bindings) {
            if (binding == null) {
                throw new IllegalArgumentException("bindings cannot contain null elements");
            }
        }
    }
}
