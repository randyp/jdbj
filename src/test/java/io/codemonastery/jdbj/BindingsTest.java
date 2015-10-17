package io.codemonastery.jdbj;

import io.codemonastery.jdbj.ListBinding;
import io.codemonastery.jdbj.PositionalBinding;
import io.codemonastery.jdbj.PositionalBindings;
import io.codemonastery.jdbj.ValueBinding;
import io.codemonastery.jdbj.lambda.Binding;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BindingsTest {

    @Test
    public void empty() throws Exception {
        final PositionalBindings bindings = new PositionalBindings();
        assertFalse(bindings.containsBinding(":status"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getNotPresent() throws Exception {
        new PositionalBindings().get(":a");
    }

    @Test
    public void valueBinding() throws Exception {
        final Binding binding = pc -> pc.setString("ACTIVE");
        final PositionalBindings bindings = new PositionalBindings().bind(":status", binding);

        assertTrue(bindings.containsBinding(":status"));
        final PositionalBinding positionalBinding = bindings.get(":status");
        assertTrue(positionalBinding instanceof ValueBinding);
    }

    @Test
    public void listBinding() throws Exception {
        final PositionalBindings bindings = new PositionalBindings().bindCollection(":status", Collections.singletonList(pc -> pc.setString("ACTIVE")));

        assertTrue(bindings.containsBinding(":status"));
        final PositionalBinding positionalBinding = bindings.get(":status");
        assertTrue(positionalBinding instanceof ListBinding);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rebindValue() throws Exception {
        new PositionalBindings()
                .bind(":status", pc -> pc.setString("ACTIVE"))
                .bind(":status", pc -> pc.setString("ACTIVE"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void rebindList() throws Exception {
        new PositionalBindings()
                .bind(":status", pc -> pc.setString("ACTIVE"))
                .bindCollection(":status", new ArrayList<>());
    }

    @Test(expected = NullPointerException.class)
    public void bindValueNullName() throws Exception {
        new PositionalBindings().bind(null, pc -> pc.setInt(1));
    }

    @Test(expected = NullPointerException.class)
    public void bindValueNullValue() throws Exception {
        new PositionalBindings().bind(":a", null);
    }

    @Test(expected = NullPointerException.class)
    public void bindListNullName() throws Exception {
        new PositionalBindings().bindCollection(null, new ArrayList<>());
    }

    @Test(expected = NullPointerException.class)
    public void bindListNullList() throws Exception {
        new PositionalBindings().bindCollection(":a", null);
    }

    @Test(expected = NullPointerException.class)
    public void bindListNullInList() throws Exception {
        new PositionalBindings().bindCollection(":a", Arrays.asList(pc -> pc.setInt(1), null));
    }
}
