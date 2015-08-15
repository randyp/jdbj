package com.github.randyp.jdbj;

import com.github.randyp.jdbj.binding.ListBinding;
import com.github.randyp.jdbj.binding.PositionalBinding;
import com.github.randyp.jdbj.binding.ValueBinding;
import com.github.randyp.jdbj.lambda.Binding;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

public class BindingsTest {

    @Test
    public void empty() throws Exception {
        final PositionalBindings bindings = PositionalBindings.empty();
        assertFalse(bindings.containsBinding(":status"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getNotPresent() throws Exception {
        PositionalBindings.empty().get(":a");
    }

    @Test
    public void valueBinding() throws Exception {
        final Binding binding = pc -> pc.setString("ACTIVE");
        final PositionalBindings bindings = PositionalBindings.empty().valueBinding(":status", binding);

        assertTrue(bindings.containsBinding(":status"));
        final PositionalBinding positionalBinding = bindings.get(":status");
        assertTrue(positionalBinding instanceof ValueBinding);
    }

    @Test
    public void listBinding() throws Exception {
        final PositionalBindings bindings = PositionalBindings.empty().listBinding(":status", Collections.singletonList(pc -> pc.setString("ACTIVE")));

        assertTrue(bindings.containsBinding(":status"));
        final PositionalBinding positionalBinding = bindings.get(":status");
        assertTrue(positionalBinding instanceof ListBinding);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rebindValue() throws Exception {
        PositionalBindings.empty()
                .valueBinding(":status", pc -> pc.setString("ACTIVE"))
                .valueBinding(":status", pc -> pc.setString("ACTIVE"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void rebindList() throws Exception {
        PositionalBindings.empty()
                .valueBinding(":status", pc -> pc.setString("ACTIVE"))
                .listBinding(":status", new ArrayList<>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void bindValueNullName() throws Exception {
        PositionalBindings.empty().valueBinding(null, pc -> pc.setInt(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void bindValueNullValue() throws Exception {
        PositionalBindings.empty().valueBinding(":a", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void bindListNullName() throws Exception {
        PositionalBindings.empty().listBinding(null, new ArrayList<>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void bindListNullList() throws Exception {
        PositionalBindings.empty().listBinding(":a", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void bindListNullInList() throws Exception {
        PositionalBindings.empty().listBinding(":a", Arrays.asList(pc -> pc.setInt(1), null));
    }
}