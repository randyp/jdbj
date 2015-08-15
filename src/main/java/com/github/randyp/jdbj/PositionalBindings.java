package com.github.randyp.jdbj;

import com.github.randyp.jdbj.binding.ListBinding;
import com.github.randyp.jdbj.binding.PositionalBinding;
import com.github.randyp.jdbj.binding.ValueBinding;
import jdk.nashorn.internal.ir.annotations.Immutable;
import com.github.randyp.jdbj.lambda.Binding;

import java.util.*;

@Immutable
public class PositionalBindings implements ValueBindings {

    public static PositionalBindings empty() {
        return new PositionalBindings(new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>());
    }

    private final Map<String, Binding> defaultValueBindings;
    private final Map<String, List<Binding>> defaultListBindings;

    private final Map<String, Binding> valueBindings;
    private final Map<String, List<Binding>> listBindings;

    private final Set<String> keys;

    private PositionalBindings(Map<String, Binding> defaultValueBindings,
                               Map<String, List<Binding>> defaultListBindings,
                               Map<String, Binding> valueBindings, Map<String,
            List<Binding>> listBindings) {
        this.defaultValueBindings = defaultValueBindings;
        this.defaultListBindings = defaultListBindings;
        this.valueBindings = valueBindings;
        this.listBindings = listBindings;

        {
            final Set<String> keys = new HashSet<>(valueBindings.keySet());
            keys.addAll(listBindings.keySet());
            keys.addAll(defaultValueBindings.keySet());
            keys.addAll(defaultListBindings.keySet());
            this.keys = Collections.unmodifiableSet(keys);
        }
    }

    @Override
    public boolean containsBinding(String name) {
        return containsHardBinding(name) || containsDefaultedBinding(name);
    }

    public boolean containsHardBinding(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name cannot be null");
        }
        return valueBindings.containsKey(name) || listBindings.containsKey(name);
    }

    public boolean containsDefaultedBinding(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name cannot be null");
        }
        return defaultValueBindings.containsKey(name) || defaultListBindings.containsKey(name);
    }

    @Override
    public PositionalBindings valueBinding(String name, Binding binding) {
        if (binding == null) {
            throw new IllegalArgumentException("binding cannot be null");
        }

        if (containsHardBinding(name)) {
            throw new IllegalArgumentException("named parameter \"" + name + "\" already has a binding");
        }

        final Map<String, Binding> newValueBindings = new HashMap<>(valueBindings);
        newValueBindings.put(name, binding);

        return new PositionalBindings(
                defaultValueBindings,
                defaultListBindings,
                newValueBindings,
                listBindings
        );
    }

    @Override
    public PositionalBindings defaultValueBinding(String name, Binding binding) {
        if (binding == null) {
            throw new IllegalArgumentException("binding cannot be null");
        }
        if(containsBinding(name)){
            throw new IllegalArgumentException("named parameter \"" + name + "\" already has a default binding");
        }
        final Map<String, Binding> newDefaultValueBindings = new HashMap<>(defaultValueBindings);
        newDefaultValueBindings.put(name, binding);
        return new PositionalBindings(newDefaultValueBindings, defaultListBindings, valueBindings, listBindings);
    }

    public PositionalBindings listBinding(String name, List<Binding> bindings) {
        if (name == null) {
            throw new IllegalArgumentException("name cannot be null");
        }
        checkBindings(bindings);

        if (containsHardBinding(name)) {
            throw new IllegalArgumentException("named parameter \"" + name + "\" already has a binding");
        }

        final Map<String, List<Binding>> newListBindings = new HashMap<>(listBindings);
        newListBindings.put(name, bindings);

        return new PositionalBindings(
                defaultValueBindings,
                defaultListBindings,
                valueBindings,
                newListBindings
        );
    }

    public PositionalBindings defaultListBinding(String name, List<Binding> bindings) {
        if (name == null) {
            throw new IllegalArgumentException("name cannot be null");
        }
        checkBindings(bindings);
        if (containsBinding(name)) {
            throw new IllegalArgumentException("named parameter \"" + name + "\" already has a binding");
        }

        final Map<String, List<Binding>> newDefaultListBindings = new HashMap<>(defaultListBindings);
        newDefaultListBindings.put(name, bindings);

        return new PositionalBindings(
                defaultValueBindings,
                newDefaultListBindings,
                valueBindings,
                listBindings
        );
    }

    @Override
    public PositionalBinding get(String namedParameter) {
        final PositionalBinding toReturn;
        if (valueBindings.containsKey(namedParameter)) {
            toReturn = new ValueBinding(valueBindings.get(namedParameter));
        } else if (listBindings.containsKey(namedParameter)) {
            toReturn = new ListBinding(listBindings.get(namedParameter));
        } else if(defaultValueBindings.containsKey(namedParameter)){
            toReturn = new ValueBinding(defaultValueBindings.get(namedParameter));
        }else if(defaultListBindings.containsKey(namedParameter)){
            toReturn = new ListBinding(defaultListBindings.get(namedParameter));
        }else {
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
