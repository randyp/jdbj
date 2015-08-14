package jdbj.lambda;

import jdbj.NamedParameterStatement;
import jdbj.PositionalBindings;
import jdbj.PositionalBindingsBuilder;

public interface PositionalBindingsBuilderFactory<E> {

    E make(NamedParameterStatement statement, PositionalBindings bindings);

}
