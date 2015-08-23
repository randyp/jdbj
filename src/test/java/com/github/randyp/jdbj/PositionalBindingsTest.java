package com.github.randyp.jdbj;

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
        public void bind() throws Exception {
            final Binding expected = pc -> pc.setInt(1);
            final PositionalBindings positionalBindings = PositionalBindings.empty()
                    .bind(":id", expected);
            assertTrue(positionalBindings.containsBinding(":id"));

            final PositionalBinding positionalBinding = positionalBindings.get(":id");
            assertTrue(positionalBinding instanceof ValueBinding);
            assertSame(expected, ((ValueBinding) positionalBinding).getBinding());
        }

        @Test(expected = IllegalArgumentException.class)
        public void alreadyBound() throws Exception {
            PositionalBindings.empty()
                    .bind(":id", pc -> pc.setInt(1))
                    .bind(":id", pc -> pc.setInt(1));
        }
    }

    public static class BindCollection {

        @Test
        public void notBound() throws Exception {
            final PositionalBindings positionalBindings = PositionalBindings.empty();
            assertFalse(positionalBindings.containsBinding(":id"));
        }

        @Test
        public void bind() throws Exception {
            final List<Binding> expected = Collections.singletonList(pc -> pc.setInt(1));
            final PositionalBindings positionalBindings = PositionalBindings.empty()
                    .bindCollection(":id", expected);
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
                    .bindCollection(":id", new ArrayList<>())
                    .bindCollection(":id", new ArrayList<>());
        }
    }

    public static class AddBindings {
        
        

        @Test
        public void notBound() throws Exception {
            final PositionalBindings left = PositionalBindings.empty();
            final PositionalBindings right = PositionalBindings.empty();
            final PositionalBindings positionalBindings = left.addAll(right);
            assertFalse(positionalBindings.containsBinding(":id"));
        }

        @Test
        public void bindLeftValue() throws Exception {
            final Binding expected = pc -> pc.setInt(1);
            final PositionalBindings left = PositionalBindings.empty().bind(":id", expected);
            final PositionalBindings right = PositionalBindings.empty();
            final PositionalBindings positionalBindings = left.addAll(right);
            assertTrue(positionalBindings.containsBinding(":id"));

            final PositionalBinding positionalBinding = positionalBindings.get(":id");
            assertTrue(positionalBinding instanceof ValueBinding);
            assertSame(expected, ((ValueBinding) positionalBinding).getBinding());
        }

        @Test
        public void bindRightValue() throws Exception {
            final Binding expected = pc -> pc.setInt(1);
            final PositionalBindings left = PositionalBindings.empty();
            final PositionalBindings right = PositionalBindings.empty().bind(":id", expected);
            final PositionalBindings positionalBindings = left.addAll(right);
            assertTrue(positionalBindings.containsBinding(":id"));

            final PositionalBinding positionalBinding = positionalBindings.get(":id");
            assertTrue(positionalBinding instanceof ValueBinding);
            assertSame(expected, ((ValueBinding) positionalBinding).getBinding());
        }

        @Test
        public void bindLeftCollection() throws Exception {
            final List<Binding> expected = Collections.singletonList(pc -> pc.setInt(1));
            final PositionalBindings left = PositionalBindings.empty().bindCollection(":id", expected);
            final PositionalBindings right = PositionalBindings.empty();
            final PositionalBindings positionalBindings = left.addAll(right);
            assertTrue(positionalBindings.containsBinding(":id"));

            final PositionalBinding positionalBinding = positionalBindings.get(":id");
            assertTrue(positionalBinding instanceof ListBinding);

            final List<Binding> bindings = ((ListBinding) positionalBinding).getBindings();
            assertEquals(expected.size(), bindings.size());
            assertSame(expected.get(0), bindings.get(0));
        }

        @Test
        public void bindRightCollection() throws Exception {
            final List<Binding> expected = Collections.singletonList(pc -> pc.setInt(1));
            final PositionalBindings left = PositionalBindings.empty();
            final PositionalBindings right = PositionalBindings.empty().bindCollection(":id", expected);
            final PositionalBindings positionalBindings = left.addAll(right);
            assertTrue(positionalBindings.containsBinding(":id"));

            final PositionalBinding positionalBinding = positionalBindings.get(":id");
            assertTrue(positionalBinding instanceof ListBinding);

            final List<Binding> bindings = ((ListBinding) positionalBinding).getBindings();
            assertEquals(expected.size(), bindings.size());
            assertSame(expected.get(0), bindings.get(0));
        }

        @Test(expected = IllegalArgumentException.class)
        public void alreadyBound() throws Exception {
            final Binding expected = pc -> pc.setInt(1);
            final PositionalBindings left = PositionalBindings.empty().bind(":id", expected);
            final PositionalBindings right = PositionalBindings.empty().bind(":id", expected);
            left.addAll(right);
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullBindings() throws Exception {
            PositionalBindings.empty().addAll(null);
        }
    }
}
