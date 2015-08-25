package com.github.randyp.jdbj;

import com.github.randyp.jdbj.lambda.Binding;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.*;

@RunWith(Enclosed.class)
public class ValueBindingsTest {

    public static class Keys {
        @Test(expected = UnsupportedOperationException.class)
        public void unmodifiable() throws Exception {
            new ValueBindings().keys().clear();
        }
    }

    public static class Get {
        @Test(expected = NullPointerException.class)
        public void exceptOnNull() throws Exception {
            new ValueBindings().get(null);
        }
    }

    public static class ContainsBinding {
        @Test(expected = NullPointerException.class)
        public void exceptOnNull() throws Exception {
            new ValueBindings().containsBinding(null);
        }
    }

    public static class AsMap {
        @Test
        public void empty() throws Exception {
            assertTrue(new ValueBindings().asMap().isEmpty());
        }

        @Test
        public void someEntries() throws Exception {
            final ValueBindings bindings = new ValueBindings()
                    .bindInt(":id", 1)
                    .bindInt(":not_id", 2);
            assertEquals(new HashSet<>(Arrays.asList(":id", ":not_id")), bindings.asMap().keySet());
        }
    }

    public static class BindValue {

        @Test
        public void notBound() throws Exception {
            final ValueBindings positionalBindings = new ValueBindings();
            assertFalse(positionalBindings.containsBinding(":id"));
        }

        @Test
        public void bind() throws Exception {
            final Binding expected = pc -> pc.setInt(1);
            final ValueBindings positionalBindings = new ValueBindings()
                    .bind(":id", expected);
            assertTrue(positionalBindings.containsBinding(":id"));

            assertSame(expected, positionalBindings.get(":id").getBinding());
        }

        @Test(expected = IllegalArgumentException.class)
        public void alreadyBound() throws Exception {
            new ValueBindings()
                    .bind(":id", pc -> pc.setInt(1))
                    .bind(":id", pc -> pc.setInt(1));
        }

        @Test(expected = NullPointerException.class)
        public void nullBinding() throws Exception {
            new ValueBindings().bind(":id", null);
        }

        @Test(expected = NullPointerException.class)
        public void nullName() throws Exception {
            new ValueBindings().bind(null, pc->pc.setInt(1));
        }
    }

    public static class AddAll {

        @Test
        public void notBound() throws Exception {
            final ValueBindings left = new ValueBindings();
            final ValueBindings right = new ValueBindings();
            final ValueBindings positionalBindings = left.addAll(right);
            assertFalse(positionalBindings.containsBinding(":id"));
        }

        @Test
        public void bindLeftValue() throws Exception {
            final Binding expected = pc -> pc.setInt(1);
            final ValueBindings left = new ValueBindings().bind(":id", expected);
            final ValueBindings right = new ValueBindings();
            final ValueBindings positionalBindings = left.addAll(right);
            assertTrue(positionalBindings.containsBinding(":id"));

            assertSame(expected, positionalBindings.get(":id").getBinding());
        }

        @Test
        public void bindRightValue() throws Exception {
            final Binding expected = pc -> pc.setInt(1);
            final ValueBindings left = new ValueBindings();
            final ValueBindings right = new ValueBindings().bind(":id", expected);
            final ValueBindings positionalBindings = left.addAll(right);
            assertTrue(positionalBindings.containsBinding(":id"));

            assertSame(expected, positionalBindings.get(":id").getBinding());
        }

        @Test(expected = IllegalArgumentException.class)
        public void alreadyBound() throws Exception {
            final Binding expected = pc -> pc.setInt(1);
            final ValueBindings left = new ValueBindings().bind(":id", expected);
            final ValueBindings right = new ValueBindings().bind(":id", expected);
            left.addAll(right);
        }

        @Test(expected = NullPointerException.class)
        public void nullBindings() throws Exception {
            new ValueBindings().addAll(null);
        }
    }
}
