package com.github.randyp.jdbj;

import com.github.randyp.jdbj.lambda.Binding;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import java.util.*;

/**
 * Restricted interface of PositionalBindings for {@link ValueBindingsBuilder}.
 */
@Immutable
@ThreadSafe
public class ValueBindings implements Bindings, ValueBindingsBuilder<ValueBindings> {

    private final Map<String, Binding> valueBindings;
    private final Set<String> keys;

    /**
     * New empty value bindings
     */
    public ValueBindings() {
        this(new HashMap<>());
    }

    ValueBindings(Map<String, Binding> valueBindings) {
        this.valueBindings = valueBindings;
        this.keys = Collections.unmodifiableSet(valueBindings.keySet());
    }

    public ValueBindings bind(String name, Binding binding) {
        if (binding == null) {
            throw new IllegalArgumentException("binding cannot be null");
        }

        if (containsBinding(name)) {
            throw new IllegalArgumentException("named parameter \"" + name + "\" already has a binding");
        }

        final Map<String, Binding> newValueBindings = new HashMap<>(valueBindings);
        newValueBindings.put(name, binding);

        return new ValueBindings(newValueBindings);
    }

    public ValueBindings addAll(ValueBindings bindings) {
        if (bindings == null) {
            throw new IllegalArgumentException("bindings cannot be null");
        }
        ValueBindings newBindings = this;
        for (Map.Entry<String, Binding> entry : bindings.valueBindings.entrySet()) {
            newBindings = newBindings.bind(entry.getKey(), entry.getValue());
        }
        return newBindings;
    }

    public Map<String, Binding> asMap(){
        return Collections.unmodifiableMap(valueBindings);
    }
 
    @Override
    public boolean containsBinding(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name cannot be null");
        }
        return valueBindings.containsKey(name);
    }

    @Override
    public Set<String> keys() {
        return keys;
    }

    @Override
    public ValueBinding get(String namedParameter) {
        final Binding binding = valueBindings.get(namedParameter);
        if (binding  == null) {
            throw new IllegalArgumentException("no such binding: \"" + namedParameter + "\"");
        }
        return new ValueBinding(binding);
    }

}
