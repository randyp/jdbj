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
        return new PositionalBindings(new HashMap<>(), new HashMap<>());
    }

    private final Map<String, Binding> valueBindings;
    private final Map<String, List<Binding>> listBindings;

    private PositionalBindings(Map<String, Binding> valueBindings, Map<String, List<Binding>> listBindings) {
        this.valueBindings = valueBindings;
        this.listBindings = listBindings;
    }

    @Override
    public boolean containsBinding(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name cannot be null");
        }
        return valueBindings.containsKey(name) || listBindings.containsKey(name);
    }

    @Override
    public PositionalBindings addValueBinding(String name, Binding binding) {
        if (binding == null) {
            throw new IllegalArgumentException("binding cannot be null");
        }

        if(containsBinding(name)){
            throw new IllegalArgumentException("named parameter \""+name+"\" already has a binding");
        }

        final Map<String, Binding> newValueBindings = new HashMap<>(valueBindings);
        newValueBindings.put(name, binding);

        return new PositionalBindings(newValueBindings, listBindings);
    }

    public PositionalBindings addListBinding(String name, List<Binding> bindings) {
        if (name == null) {
            throw new IllegalArgumentException("name cannot be null");
        }
        if (bindings == null) {
            throw new IllegalArgumentException("bindings cannot be null");
        }
        for (Binding binding : bindings) {
            if(binding == null){
                throw new IllegalArgumentException("bindings cannot contain null elements");
            }
        }

        if(containsBinding(name)){
            throw new IllegalArgumentException("named parameter \""+name+"\" already has a binding");
        }

        final Map<String, List<Binding>> newListBindings = new HashMap<>(listBindings);
        newListBindings.put(name, bindings);

        return new PositionalBindings(valueBindings, newListBindings);
    }

    @Override
    public PositionalBinding get(String namedParameter) {
        final PositionalBinding toReturn;
        if(valueBindings.containsKey(namedParameter)){
            toReturn = new ValueBinding(valueBindings.get(namedParameter));
        }else if(listBindings.containsKey(namedParameter)){
            toReturn = new ListBinding(listBindings.get(namedParameter));
        }else{
            throw new IllegalArgumentException("no such binding: \"" + namedParameter+"\"");
        }
        return toReturn;
    }

    @Override
    public Set<String> keys() {
        final Set<String> toReturn = new HashSet<>(valueBindings.keySet());
        toReturn.addAll(listBindings.keySet());
        return toReturn;
    }
}
