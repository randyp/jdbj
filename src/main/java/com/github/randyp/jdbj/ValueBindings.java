package com.github.randyp.jdbj;

import com.github.randyp.jdbj.lambda.Binding;

import java.util.Set;

/**
 * Restricted interface of PositionalBindings for {@link ValueBindingsBuilder}.
 */
public interface ValueBindings {

    boolean containsBinding(String name);

    PositionalBindings bind(String name, Binding binding);

    PositionalBinding get(String namedParameter);

    Set<String> keys();

}
