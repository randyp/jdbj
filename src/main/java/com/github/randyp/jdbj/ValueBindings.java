package com.github.randyp.jdbj;

import com.github.randyp.jdbj.lambda.Binding;

import java.util.Set;

public interface ValueBindings {

    boolean containsBinding(String name);

    PositionalBindings valueBinding(String name, Binding binding);

    PositionalBinding get(String namedParameter);

    Set<String> keys();

}
