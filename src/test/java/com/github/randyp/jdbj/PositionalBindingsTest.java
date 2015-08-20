package com.github.randyp.jdbj;

import com.github.randyp.jdbj.binding.ListBinding;
import com.github.randyp.jdbj.binding.PositionalBinding;
import com.github.randyp.jdbj.binding.ValueBinding;
import com.github.randyp.jdbj.lambda.Binding;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Enclosed.class)
public class PositionalBindingsTest {

    public static class Keys {
        @Test(expected = UnsupportedOperationException.class)
        public void unmodifiable() throws Exception {
            PositionalBindings.empty().keys().clear();
        }
    }

    public static class BindValue {

        @Test
        public void notBound() throws Exception {
            final PositionalBindings positionalBindings = PositionalBindings.empty();
            assertFalse(positionalBindings.containsBinding(":id"));
        }

        @Test
        public void bindingOnlyUseBinding() throws Exception {
            final Binding expected = pc -> pc.setInt(1);
            final PositionalBindings positionalBindings = PositionalBindings.empty()
                    .valueBinding(":id", expected);
            assertTrue(positionalBindings.containsBinding(":id"));

            final PositionalBinding positionalBinding = positionalBindings.get(":id");
            assertTrue(positionalBinding instanceof ValueBinding);
            assertSame(expected, ((ValueBinding) positionalBinding).getBinding());
        }

        @Test(expected = IllegalArgumentException.class)
        public void alreadyBound() throws Exception {
            PositionalBindings.empty()
                    .valueBinding(":id", pc -> pc.setInt(1))
                    .valueBinding(":id", pc -> pc.setInt(1));
        }
    }

    public static class Bindlist {

        @Test
        public void notBound() throws Exception {
            final PositionalBindings positionalBindings = PositionalBindings.empty();
            assertFalse(positionalBindings.containsBinding(":id"));
        }

        @Test
        public void bindingOnlyUseBinding() throws Exception {
            final List<Binding> expected = Collections.singletonList(pc -> pc.setInt(1));
            final PositionalBindings positionalBindings = PositionalBindings.empty()
                    .collectionBinding(":id", expected);
            assertTrue(positionalBindings.containsBinding(":id"));

            final PositionalBinding positionalBinding = positionalBindings.get(":id");
            assertTrue(positionalBinding instanceof ListBinding);

            final List<Binding> bindings = ((ListBinding) positionalBinding).getBindings();
            assertEquals(expected.size(), bindings.size());
            assertSame(expected.get(0), bindings.get(0));
        }


        @Test(expected = IllegalArgumentException.class)
        public void alreadyBound() throws Exception {
            PositionalBindings.empty()
                    .collectionBinding(":id", new ArrayList<>())
                    .collectionBinding(":id", new ArrayList<>());
        }
    }
}