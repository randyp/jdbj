package com.github.randyp.jdbj.lambda;

import com.github.randyp.jdbj.NamedParameterStatement;
import com.github.randyp.jdbj.PositionalBindings;

public interface PositionalBindingsBuilderFactory<E> {

    E make(NamedParameterStatement statement, PositionalBindings bindings);

}
