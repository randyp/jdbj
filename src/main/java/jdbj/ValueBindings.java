package jdbj;

import jdbj.binding.PositionalBinding;
import jdbj.lambda.Binding;

import java.util.Set;

public interface ValueBindings {

    boolean containsBinding(String name);

    PositionalBindings addValueBinding(String name, Binding binding);

    PositionalBinding get(String namedParameter);

    Set<String> keys();

}
