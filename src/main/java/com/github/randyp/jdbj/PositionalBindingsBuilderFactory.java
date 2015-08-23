package com.github.randyp.jdbj;

import com.github.randyp.jdbj.lambda.Binding;

/**
 * Not intended for external use.
 * <p>
 * Allows sub-classes of {@link PositionalBindingsBuilder} to specify a prototype callback for when {@link PositionalBindingsBuilder#bind(String, Binding)} is called, or its variables such as {@link PositionalBindingsBuilder#bindInt(String, int)}
 * @param <E>
 */
interface PositionalBindingsBuilderFactory<E> {

    E make(NamedParameterStatement statement, PositionalBindings bindings);

}
