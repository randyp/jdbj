package oof.jdbc;

import jdk.nashorn.internal.ir.annotations.Immutable;
import oof.jdbc.binding.ListBinding;
import oof.jdbc.binding.PositionalBinding;
import oof.jdbc.binding.ValueBinding;
import oof.jdbc.lambda.Binding;

import java.util.*;

@Immutable
public class Bindings {

    public static Bindings empty() {
        return new Bindings(new HashMap<>(), new HashMap<>());
    }

    private final Map<String, Binding> valueBindings;
    private final Map<String, List<Binding>> listBindings;

    private Bindings(Map<String, Binding> valueBindings, Map<String, List<Binding>> listBindings) {
        this.valueBindings = valueBindings;
        this.listBindings = listBindings;
    }

    public boolean containsBinding(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name cannot be null");
        }
        return valueBindings.containsKey(name) || listBindings.containsKey(name);
    }

    public Bindings addValueBinding(String name, Binding binding) {
        if (binding == null) {
            throw new IllegalArgumentException("binding cannot be null");
        }

        if(containsBinding(name)){
            throw new IllegalArgumentException("named parameter \""+name+"\" already has a binding");
        }

        final Map<String, Binding> newValueBindings = new HashMap<>(valueBindings);
        newValueBindings.put(name, binding);

        return new Bindings(newValueBindings, listBindings);
    }

    public Bindings addListBinding(String name, List<Binding> bindings) {
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

        return new Bindings(valueBindings, newListBindings);
    }


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

    public Set<String> keys() {
        final Set<String> toReturn = new HashSet<>(valueBindings.keySet());
        toReturn.addAll(listBindings.keySet());
        return toReturn;
    }
}
