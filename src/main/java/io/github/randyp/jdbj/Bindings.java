package io.github.randyp.jdbj;

import java.util.Set;

public interface Bindings {

    boolean containsBinding(String name);

    Set<String> keys();

    PositionalBinding get(String namedParameter);
    
}
