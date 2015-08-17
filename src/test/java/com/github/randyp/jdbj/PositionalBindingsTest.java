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

    public static class ContainsBinding {

        @Test(expected = IllegalArgumentException.class)
        public void nullNameDefault() throws Exception {
            PositionalBindings.empty().containsDefaultedBinding(null);
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullNameHard() throws Exception {
            PositionalBindings.empty().containsDefaultedBinding(null);
        }
    }

    public static class Value {

        @Test
        public void defaultOnlyUseDefault() throws Exception {
            final Binding expected = pc -> pc.setInt(1);
            final PositionalBindings positionalBindings = PositionalBindings.empty()
                    .defaultValueBinding(":id", expected);
            assertTrue(positionalBindings.containsBinding(":id"));
            assertTrue(positionalBindings.containsDefaultedBinding(":id"));
            assertFalse(positionalBindings.containsHardBinding(":id"));

            final PositionalBinding positionalBinding = positionalBindings.get(":id");
            assertTrue(positionalBinding instanceof ValueBinding);
            assertSame(expected, ((ValueBinding) positionalBinding).getBinding());
        }

        @Test
        public void bindingOnlyUseBinding() throws Exception {
            final Binding expected = pc -> pc.setInt(1);
            final PositionalBindings positionalBindings = PositionalBindings.empty()
                    .valueBinding(":id", expected);
            assertTrue(positionalBindings.containsBinding(":id"));
            assertFalse(positionalBindings.containsDefaultedBinding(":id"));
            assertTrue(positionalBindings.containsHardBinding(":id"));

            final PositionalBinding positionalBinding = positionalBindings.get(":id");
            assertTrue(positionalBinding instanceof ValueBinding);
            assertSame(expected, ((ValueBinding) positionalBinding).getBinding());
        }

        @Test
        public void defaultAndBindingUseBinding() throws Exception {
            final Binding expected = pc -> pc.setInt(1);
            final PositionalBindings positionalBindings = PositionalBindings.empty()
                    .defaultValueBinding(":id", pc -> pc.setInt(2))
                    .valueBinding(":id", expected);
            assertTrue(positionalBindings.containsBinding(":id"));
            assertTrue(positionalBindings.containsDefaultedBinding(":id"));
            assertTrue(positionalBindings.containsHardBinding(":id"));

            final PositionalBinding positionalBinding = positionalBindings.get(":id");
            assertTrue(positionalBinding instanceof ValueBinding);
            assertSame(expected, ((ValueBinding) positionalBinding).getBinding());
        }

        @Test(expected = IllegalArgumentException.class)
        public void alreadyDefaulted() throws Exception {
            PositionalBindings.empty()
                    .defaultValueBinding(":id", pc -> pc.setInt(1))
                    .defaultValueBinding(":id", pc -> pc.setInt(1));
        }

        @Test(expected = IllegalArgumentException.class)
        public void alreadyBound() throws Exception {
            PositionalBindings.empty()
                    .valueBinding(":id", pc -> pc.setInt(1))
                    .valueBinding(":id", pc -> pc.setInt(1));
        }

        @Test(expected = IllegalArgumentException.class)
        public void alreadyBoundWhyAreYouDefaulting() throws Exception {
            PositionalBindings.empty()
                    .valueBinding(":id", pc -> pc.setInt(1))
                    .defaultValueBinding(":id", pc -> pc.setInt(1));
        }
    }

    public static class list {

        @Test
        public void defaultOnlyUseDefault() throws Exception {
            final List<Binding> expected = Collections.singletonList(pc -> pc.setInt(1));
            final PositionalBindings positionalBindings = PositionalBindings.empty()
                    .defaultListBinding(":id", expected);
            assertTrue(positionalBindings.containsBinding(":id"));
            assertTrue(positionalBindings.containsDefaultedBinding(":id"));
            assertFalse(positionalBindings.containsHardBinding(":id"));

            final PositionalBinding positionalBinding = positionalBindings.get(":id");
            assertTrue(positionalBinding instanceof ListBinding);

            final List<Binding> bindings = ((ListBinding) positionalBinding).getBindings();
            assertEquals(expected.size(), bindings.size());
            assertSame(expected.get(0), bindings.get(0));
        }

        @Test
        public void bindingOnlyUseBinding() throws Exception {
            final List<Binding> expected = Collections.singletonList(pc -> pc.setInt(1));
            final PositionalBindings positionalBindings = PositionalBindings.empty()
                    .listBinding(":id", expected);
            assertTrue(positionalBindings.containsBinding(":id"));
            assertFalse(positionalBindings.containsDefaultedBinding(":id"));
            assertTrue(positionalBindings.containsHardBinding(":id"));

            final PositionalBinding positionalBinding = positionalBindings.get(":id");
            assertTrue(positionalBinding instanceof ListBinding);

            final List<Binding> bindings = ((ListBinding) positionalBinding).getBindings();
            assertEquals(expected.size(), bindings.size());
            assertSame(expected.get(0), bindings.get(0));
        }

        @Test
        public void defaultAndBindingUseBinding() throws Exception {
            final List<Binding> expected = Collections.singletonList(pc -> pc.setInt(1));
            final PositionalBindings positionalBindings = PositionalBindings.empty()
                    .defaultListBinding(":id", new ArrayList<>())
                    .listBinding(":id", expected);
            assertTrue(positionalBindings.containsBinding(":id"));
            assertTrue(positionalBindings.containsDefaultedBinding(":id"));
            assertTrue(positionalBindings.containsHardBinding(":id"));

            final PositionalBinding positionalBinding = positionalBindings.get(":id");
            assertTrue(positionalBinding instanceof ListBinding);

            final List<Binding> bindings = ((ListBinding) positionalBinding).getBindings();
            assertEquals(expected.size(), bindings.size());
            assertSame(expected.get(0), bindings.get(0));
        }

        @Test(expected = IllegalArgumentException.class)
        public void alreadyDefaulted() throws Exception {
            PositionalBindings.empty()
                    .defaultListBinding(":id", new ArrayList<>())
                    .defaultListBinding(":id", new ArrayList<>());
        }

        @Test(expected = IllegalArgumentException.class)
        public void alreadyBound() throws Exception {
            PositionalBindings.empty()
                    .listBinding(":id", new ArrayList<>())
                    .listBinding(":id", new ArrayList<>());
        }

        @Test(expected = IllegalArgumentException.class)
        public void alreadyBoundWhyAreYouDefaulting() throws Exception {
            PositionalBindings.empty()
                    .listBinding(":id", new ArrayList<>())
                    .defaultListBinding(":id", new ArrayList<>());
        }

        @Test(expected = IllegalArgumentException.class)
        public void defaultListNullName() throws Exception {
            PositionalBindings.empty()
                    .defaultListBinding(null, new ArrayList<>());
        }
    }

    public static class DefaultValuesHardLists {

        @Test
        public void defaultAndBindingUseBinding() throws Exception {
            final List<Binding> expected = Collections.singletonList(pc -> pc.setInt(1));
            final PositionalBindings positionalBindings = PositionalBindings.empty()
                    .defaultValueBinding(":id", pc -> pc.setInt(1))
                    .listBinding(":id", expected);
            assertTrue(positionalBindings.containsBinding(":id"));
            assertTrue(positionalBindings.containsDefaultedBinding(":id"));
            assertTrue(positionalBindings.containsHardBinding(":id"));

            final PositionalBinding positionalBinding = positionalBindings.get(":id");
            assertTrue(positionalBinding instanceof ListBinding);

            final List<Binding> bindings = ((ListBinding) positionalBinding).getBindings();
            assertEquals(expected.size(), bindings.size());
            assertSame(expected.get(0), bindings.get(0));
        }

        @Test(expected = IllegalArgumentException.class)
        public void alreadyBoundWhyAreYouDefaulting() throws Exception {
            PositionalBindings.empty()
                    .listBinding(":id", new ArrayList<>())
                    .defaultValueBinding(":id", pc -> pc.setInt(1));
        }
    }

    public static class DefaultListsHardValues {

        @Test
        public void defaultAndBindingUseBinding() throws Exception {
            final Binding expected = pc -> pc.setInt(1);
            final PositionalBindings positionalBindings = PositionalBindings.empty()
                    .defaultListBinding(":id", new ArrayList<>())
                    .valueBinding(":id", expected);
            assertTrue(positionalBindings.containsBinding(":id"));
            assertTrue(positionalBindings.containsDefaultedBinding(":id"));
            assertTrue(positionalBindings.containsHardBinding(":id"));

            final PositionalBinding positionalBinding = positionalBindings.get(":id");
            assertTrue(positionalBinding instanceof ValueBinding);
            assertSame(expected, ((ValueBinding) positionalBinding).getBinding());
        }

        @Test(expected = IllegalArgumentException.class)
        public void alreadyBoundWhyAreYouDefaulting() throws Exception {
            PositionalBindings.empty()
                    .valueBinding(":id", pc -> pc.setInt(1))
                    .defaultListBinding(":id", new ArrayList<>());
        }
    }
}