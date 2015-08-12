package oof.jdbc;

import oof.jdbc.binding.ListBinding;
import oof.jdbc.binding.PositionalBinding;
import oof.jdbc.binding.ValueBinding;
import oof.jdbc.lambda.Binding;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.*;

public class BindingsTest {

    @Test
    public void empty() throws Exception {
        final Bindings bindings = Bindings.empty();

        assertFalse(bindings.containsKey(":status"));
    }

    @Test
    public void valueBinding() throws Exception {
        final Binding binding = pc -> pc.setString("ACTIVE");
        final Bindings bindings = Bindings.empty().addValueBinding(":status", binding);

        assertTrue(bindings.containsKey(":status"));
        final PositionalBinding positionalBinding = bindings.get(":status");
        assertTrue(positionalBinding instanceof ValueBinding);
    }

    @Test
    public void listBinding() throws Exception {
        final Bindings bindings = Bindings.empty().addListBinding(":status", Collections.singletonList(pc -> pc.setString("ACTIVE")));

        assertTrue(bindings.containsKey(":status"));
        final PositionalBinding positionalBinding = bindings.get(":status");
        assertTrue(positionalBinding instanceof ListBinding);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rebind() throws Exception {
        Bindings.empty()
                .addValueBinding(":status", pc -> pc.setString("ACTIVE"))
                .addValueBinding(":status", pc -> pc.setString("ACTIVE"));
    }
}