package com.github.randyp.jdbj.optional;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.util.NoSuchElementException;

import static org.junit.Assert.*;

@RunWith(Enclosed.class)
public class OptionalBooleanTest {

    public static class Empty {
        @Test(expected = NoSuchElementException.class)
        public void get() throws Exception {
            OptionalBoolean.empty().getAsBoolean();
        }

        @Test
        public void isPresent() throws Exception {
            assertFalse(OptionalBoolean.empty().isPresent());
        }

        @Test
        public void ifPresent() throws Exception {
            OptionalBoolean.empty().ifPresent(b -> fail("is not present, should not have been called"));
        }

        @Test
        public void orElse() throws Exception {
            assertTrue(OptionalBoolean.empty().orElse(true));
        }

        @Test
        public void orElseGet() throws Exception {
            assertTrue(OptionalBoolean.empty().orElseGet(() -> true));
        }

        @Test(expected = IllegalArgumentException.class)
        public void orElseThrow() throws Exception {
            OptionalBoolean.empty().orElseThrow(IllegalArgumentException::new);
        }

        @Test
        public void equals() throws Exception {
            assertEquals(OptionalBoolean.empty(), OptionalBoolean.empty());
            assertNotEquals(OptionalBoolean.empty(), OptionalBoolean.of(true));
            assertNotEquals(OptionalBoolean.empty(), OptionalBoolean.of(false));
            assertNotEquals(OptionalBoolean.empty(), null);
        }

        @Test
        public void hashcode() throws Exception {
            assertEquals(0, OptionalBoolean.empty().hashCode());
        }

        @Test
        public void tostring() throws Exception {
            assertEquals("OptionalBoolean.empty", OptionalBoolean.empty().toString());
        }
    }

    public static class OfFalse {
        @Test
        public void get() throws Exception {
            assertFalse(OptionalBoolean.of(false).getAsBoolean());
        }

        @Test
        public void isPresent() throws Exception {
            assertTrue(OptionalBoolean.of(false).isPresent());
        }

        @Test
        public void ifPresent() throws Exception {
            final StringBuilder builder = new StringBuilder();
            OptionalBoolean.of(false).ifPresent(builder::append);
            assertEquals("false", builder.toString());
        }

        @Test
        public void orElse() throws Exception {
            assertFalse(OptionalBoolean.of(false).orElse(true));
        }

        @Test
        public void orElseGet() throws Exception {
            assertFalse(OptionalBoolean.of(false).orElseGet(() -> true));
        }

        @Test
        public void orElseThrow() throws Exception {
            assertFalse(OptionalBoolean.of(false).orElseThrow(IllegalArgumentException::new));
        }

        @Test
        public void equals() throws Exception {
            assertEquals(OptionalBoolean.of(false), OptionalBoolean.of(false));
            assertNotEquals(OptionalBoolean.of(false), OptionalBoolean.of(true));
            assertNotEquals(OptionalBoolean.of(false), OptionalBoolean.empty());
            assertNotEquals(OptionalBoolean.of(false), null);
        }

        @Test
        public void hashcode() throws Exception {
            assertEquals(Boolean.hashCode(false), OptionalBoolean.of(false).hashCode());
        }

        @Test
        public void tostring() throws Exception {
            assertEquals("OptionalBoolean[false]", OptionalBoolean.of(false).toString());
        }
    }

    public static class OfTrue {
        @Test
        public void get() throws Exception {
            assertTrue(OptionalBoolean.of(true).getAsBoolean());
        }

        @Test
        public void isPresent() throws Exception {
            assertTrue(OptionalBoolean.of(true).isPresent());
        }

        @Test
        public void ifPresent() throws Exception {
            final StringBuilder builder = new StringBuilder();
            OptionalBoolean.of(true).ifPresent(builder::append);
            assertEquals("true", builder.toString());
        }

        @Test
        public void orElse() throws Exception {
            assertTrue(OptionalBoolean.of(true).orElse(false));
        }

        @Test
        public void orElseGet() throws Exception {
            assertTrue(OptionalBoolean.of(true).orElseGet(() -> false));
        }

        @Test
        public void orElseThrow() throws Exception {
            assertTrue(OptionalBoolean.of(true).orElseThrow(IllegalArgumentException::new));
        }

        @Test
        public void equals() throws Exception {
            assertEquals(OptionalBoolean.of(true), OptionalBoolean.of(true));
            assertNotEquals(OptionalBoolean.of(true), OptionalBoolean.of(false));
            assertNotEquals(OptionalBoolean.of(true), OptionalBoolean.empty());
        }

        @Test
        public void hashcode() throws Exception {
            assertEquals(Boolean.hashCode(true), OptionalBoolean.of(true).hashCode());
        }

        @Test
        public void tostring() throws Exception {
            assertEquals("OptionalBoolean[true]", OptionalBoolean.of(true).toString());
        }
    }

}