package com.github.randyp.jdbj.optional;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.util.NoSuchElementException;

import static org.junit.Assert.*;

@RunWith(Enclosed.class)
public class OptionalFloatTest {

    public static class Empty {
        @Test(expected = NoSuchElementException.class)
        public void get() throws Exception {
            OptionalFloat.empty().getAsFloat();
        }

        @Test
        public void isPresent() throws Exception {
            assertFalse(OptionalFloat.empty().isPresent());
        }

        @Test
        public void ifPresent() throws Exception {
            OptionalFloat.empty().ifPresent(b -> fail("is not present, should not have been called"));
        }

        @Test
        public void orElse() throws Exception {
            assertEquals(1.2f, OptionalFloat.empty().orElse(1.2f), 0.0f);
        }

        @Test
        public void orElseGet() throws Exception {
            assertEquals(1.2f, OptionalFloat.empty().orElseGet(() -> 1.2f), 0.0f);
        }

        @Test(expected = IllegalArgumentException.class)
        public void orElseThrow() throws Exception {
            OptionalFloat.empty().orElseThrow(IllegalArgumentException::new);
        }

        @Test
        public void equals() throws Exception {
            assertEquals(OptionalFloat.empty(), OptionalFloat.empty());
            assertNotEquals(OptionalFloat.empty(), OptionalFloat.of(1.2f));
            assertNotEquals(OptionalFloat.empty(), null);
        }

        @Test
        public void hashcode() throws Exception {
            assertEquals(0, OptionalFloat.empty().hashCode());
        }

        @Test
        public void tostring() throws Exception {
            assertEquals("OptionalFloat.empty", OptionalFloat.empty().toString());
        }
    }

    public static class Present {
        @Test
        public void get() throws Exception {
            assertEquals(1.2f, OptionalFloat.of(1.2f).getAsFloat(), 0.0f);
        }

        @Test
        public void isPresent() throws Exception {
            assertTrue(OptionalFloat.of(1.2f).isPresent());
        }

        @Test
        public void ifPresent() throws Exception {
            final StringBuilder builder = new StringBuilder();
            OptionalFloat.of(1.2f).ifPresent(builder::append);
            assertEquals("1.2", builder.toString());
        }

        @Test
        public void orElse() throws Exception {
            assertEquals(1.2f, OptionalFloat.of(1.2f).orElse(1.1f), 0.0f);
        }

        @Test
        public void orElseGet() throws Exception {
            assertEquals(1.2f, OptionalFloat.of(1.2f).orElseGet(() -> 1.1f), 0.0f);
        }

        @Test
        public void orElseThrow() throws Exception {
            assertEquals(1.2f, OptionalFloat.of(1.2f).orElseThrow(IllegalArgumentException::new), 0.0f);
        }

        @Test
        public void equals() throws Exception {
            assertEquals(OptionalFloat.of(1.2f), OptionalFloat.of(1.2f));
            assertNotEquals(OptionalFloat.of(1.2f), OptionalFloat.of(1.1f));
            assertNotEquals(OptionalFloat.of(1.2f), OptionalFloat.empty());
            assertNotEquals(OptionalFloat.of(1.2f), null);
        }

        @Test
        public void hashcode() throws Exception {
            assertEquals(Float.hashCode(1.2f), OptionalFloat.of(1.2f).hashCode());
        }

        @Test
        public void tostring() throws Exception {
            assertEquals("OptionalFloat[1.2]", OptionalFloat.of(1.2f).toString());
        }
    }
}