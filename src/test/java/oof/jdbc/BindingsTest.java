package oof.jdbc;

import oof.jdbc.binding.ListBinding;
import oof.jdbc.binding.PositionalBinding;
import oof.jdbc.binding.ValueBinding;
import oof.jdbc.lambda.Binding;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

public class BindingsTest {

    @Test
    public void empty() throws Exception {
        final Bindings bindings = Bindings.empty();
        assertFalse(bindings.containsBinding(":status"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getNotPresent() throws Exception {
        Bindings.empty().get(":a");
    }

    @Test
    public void valueBinding() throws Exception {
        final Binding binding = pc -> pc.setString("ACTIVE");
        final Bindings bindings = Bindings.empty().addValueBinding(":status", binding);

        assertTrue(bindings.containsBinding(":status"));
        final PositionalBinding positionalBinding = bindings.get(":status");
        assertTrue(positionalBinding instanceof ValueBinding);
    }

    @Test
    public void listBinding() throws Exception {
        final Bindings bindings = Bindings.empty().addListBinding(":status", Collections.singletonList(pc -> pc.setString("ACTIVE")));

        assertTrue(bindings.containsBinding(":status"));
        final PositionalBinding positionalBinding = bindings.get(":status");
        assertTrue(positionalBinding instanceof ListBinding);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rebindValue() throws Exception {
        Bindings.empty()
                .addValueBinding(":status", pc -> pc.setString("ACTIVE"))
                .addValueBinding(":status", pc -> pc.setString("ACTIVE"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void rebindList() throws Exception {
        Bindings.empty()
                .addValueBinding(":status", pc -> pc.setString("ACTIVE"))
                .addListBinding(":status", new ArrayList<>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void bindValueNullName() throws Exception {
        Bindings.empty().addValueBinding(null, pc->pc.setInt(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void bindValueNullValue() throws Exception {
        Bindings.empty().addValueBinding(":a", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void bindListNullName() throws Exception {
        Bindings.empty().addListBinding(null, new ArrayList<>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void bindListNullList() throws Exception {
        Bindings.empty().addListBinding(":a", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void bindListNullInList() throws Exception {
        Bindings.empty().addListBinding(":a", Arrays.asList(pc->pc.setInt(1), null));
    }
}