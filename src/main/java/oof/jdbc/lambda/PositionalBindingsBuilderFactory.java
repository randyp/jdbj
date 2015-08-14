package oof.jdbc.lambda;

import oof.jdbc.NamedParameterStatement;
import oof.jdbc.PositionalBindings;
import oof.jdbc.PositionalBindingsBuilder;

public interface PositionalBindingsBuilderFactory<E extends PositionalBindingsBuilder<E>> {

    E make(NamedParameterStatement statement, PositionalBindings bindings);

}
