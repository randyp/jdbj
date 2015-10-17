package io.codemonastery.jdbj;

import io.codemonastery.jdbj.*;
import io.codemonastery.jdbj.lambda.Binding;
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
            new PositionalBindings().keys().clear();
        }
    }

    public static class BindValue {

        @Test
        public void notBound() throws Exception {
            final PositionalBindings positionalBindings = new PositionalBindings();
            assertFalse(positionalBindings.containsBinding(":id"));
        }

        @Test
        public void bind() throws Exception {
            final Binding expected = pc -> pc.setInt(1);
            final PositionalBindings positionalBindings = new PositionalBindings()
                    .bind(":id", expected);
            assertTrue(positionalBindings.containsBinding(":id"));

            final PositionalBinding positionalBinding = positionalBindings.get(":id");
            assertTrue(positionalBinding instanceof ValueBinding);
            assertSame(expected, ((ValueBinding) positionalBinding).getBinding());
        }

        @Test(expected = IllegalArgumentException.class)
        public void alreadyBound() throws Exception {
            new PositionalBindings()
                    .bind(":id", pc -> pc.setInt(1))
                    .bind(":id", pc -> pc.setInt(1));
        }
    }

    public static class BindCollection {

        @Test
        public void notBound() throws Exception {
            final PositionalBindings positionalBindings = new PositionalBindings();
            assertFalse(positionalBindings.containsBinding(":id"));
        }

        @Test
        public void bind() throws Exception {
            final List<Binding> expected = Collections.singletonList(pc -> pc.setInt(1));
            final PositionalBindings positionalBindings = new PositionalBindings()
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
            new PositionalBindings()
                    .bindCollection(":id", new ArrayList<>())
                    .bindCollection(":id", new ArrayList<>());
        }
    }

    public static class AddAll {

        @Test
        public void notBound() throws Exception {
            final PositionalBindings left = new PositionalBindings();
            final PositionalBindings right = new PositionalBindings();
            final PositionalBindings positionalBindings = left.addAll(right);
            assertFalse(positionalBindings.containsBinding(":id"));
        }

        @Test
        public void bindLeftValue() throws Exception {
            final Binding expected = pc -> pc.setInt(1);
            final PositionalBindings left = new PositionalBindings().bind(":id", expected);
            final PositionalBindings right = new PositionalBindings();
            final PositionalBindings positionalBindings = left.addAll(right);
            assertTrue(positionalBindings.containsBinding(":id"));

            final PositionalBinding positionalBinding = positionalBindings.get(":id");
            assertTrue(positionalBinding instanceof ValueBinding);
            assertSame(expected, ((ValueBinding) positionalBinding).getBinding());
        }

        @Test
        public void bindRightValue() throws Exception {
            final Binding expected = pc -> pc.setInt(1);
            final PositionalBindings left = new PositionalBindings();
            final PositionalBindings right = new PositionalBindings().bind(":id", expected);
            final PositionalBindings positionalBindings = left.addAll(right);
            assertTrue(positionalBindings.containsBinding(":id"));

            final PositionalBinding positionalBinding = positionalBindings.get(":id");
            assertTrue(positionalBinding instanceof ValueBinding);
            assertSame(expected, ((ValueBinding) positionalBinding).getBinding());
        }

        @Test
        public void bindLeftCollection() throws Exception {
            final List<Binding> expected = Collections.singletonList(pc -> pc.setInt(1));
            final PositionalBindings left = new PositionalBindings().bindCollection(":id", expected);
            final PositionalBindings right = new PositionalBindings();
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
            final PositionalBindings left = new PositionalBindings();
            final PositionalBindings right = new PositionalBindings().bindCollection(":id", expected);
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
            final PositionalBindings left = new PositionalBindings().bind(":id", expected);
            final PositionalBindings right = new PositionalBindings().bind(":id", expected);
            left.addAll(right);
        }

        @Test(expected = NullPointerException.class)
        public void nullBindings() throws Exception {
            new PositionalBindings().addAll((PositionalBindings) null);
        }
    }

    public static class AddAllValues {

        @Test
        public void notBound() throws Exception {
            final PositionalBindings left = new PositionalBindings();
            final ValueBindings right = new ValueBindings();
            final PositionalBindings positionalBindings = left.addAll(right);
            assertFalse(positionalBindings.containsBinding(":id"));
        }

        @Test
        public void bindLeftValue() throws Exception {
            final Binding expected = pc -> pc.setInt(1);
            final PositionalBindings left = new PositionalBindings().bind(":id", expected);
            final ValueBindings right = new ValueBindings();
            final PositionalBindings positionalBindings = left.addAll(right);
            assertTrue(positionalBindings.containsBinding(":id"));

            final PositionalBinding positionalBinding = positionalBindings.get(":id");
            assertTrue(positionalBinding instanceof ValueBinding);
            assertSame(expected, ((ValueBinding) positionalBinding).getBinding());
        }

        @Test
        public void bindRightValue() throws Exception {
            final Binding expected = pc -> pc.setInt(1);
            final PositionalBindings left = new PositionalBindings();
            final ValueBindings right = new ValueBindings().bind(":id", expected);
            final PositionalBindings positionalBindings = left.addAll(right);
            assertTrue(positionalBindings.containsBinding(":id"));

            final PositionalBinding positionalBinding = positionalBindings.get(":id");
            assertTrue(positionalBinding instanceof ValueBinding);
            assertSame(expected, ((ValueBinding) positionalBinding).getBinding());
        }

        @Test
        public void bindLeftCollection() throws Exception {
            final List<Binding> expected = Collections.singletonList(pc -> pc.setInt(1));
            final PositionalBindings left = new PositionalBindings().bindCollection(":id", expected);
            final ValueBindings right = new ValueBindings();
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
            final PositionalBindings left = new PositionalBindings().bind(":id", expected);
            final ValueBindings right = new ValueBindings().bind(":id", expected);
            left.addAll(right);
        }

        @Test(expected = NullPointerException.class)
        public void nullBindings() throws Exception {
            new PositionalBindings().addAll((ValueBindings) null);
        }
    }
}
