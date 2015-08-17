package com.github.randyp.jdbj.optional;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.util.NoSuchElementException;

import static org.junit.Assert.*;

@RunWith(Enclosed.class)
public class OptionalShortTest {

    public static class Empty {
        @Test(expected = NoSuchElementException.class)
        public void get() throws Exception {
            OptionalShort.empty().getAsShort();
        }

        @Test
        public void isPresent() throws Exception {
            assertFalse(OptionalShort.empty().isPresent());
        }

        @Test
        public void ifPresent() throws Exception {
            OptionalShort.empty().ifPresent(b -> fail("is not present, should not have been called"));
        }

        @Test
        public void orElse() throws Exception {
            assertEquals((short) 12, OptionalShort.empty().orElse((short) 12));
        }

        @Test
        public void orElseGet() throws Exception {
            assertEquals((short) 12, OptionalShort.empty().orElseGet(() -> (short) 12));
        }

        @Test(expected = IllegalArgumentException.class)
        public void orElseThrow() throws Exception {
            OptionalShort.empty().orElseThrow(IllegalArgumentException::new);
        }

        @Test
        public void equals() throws Exception {
            assertEquals(OptionalShort.empty(), OptionalShort.empty());
            assertNotEquals(OptionalShort.empty(), OptionalShort.of((short) 12));
            assertNotEquals(OptionalShort.empty(), null);
        }

        @Test
        public void hashcode() throws Exception {
            assertEquals(0, OptionalShort.empty().hashCode());
        }

        @Test
        public void tostring() throws Exception {
            assertEquals("OptionalShort.empty", OptionalShort.empty().toString());
        }
    }

    public static class Present {
        @Test
        public void get() throws Exception {
            assertEquals((short) 12, OptionalShort.of((short) 12).getAsShort());
        }

        @Test
        public void isPresent() throws Exception {
            assertTrue(OptionalShort.of((short) 12).isPresent());
        }

        @Test
        public void ifPresent() throws Exception {
            final StringBuilder builder = new StringBuilder();
            OptionalShort.of((short) 12).ifPresent(builder::append);
            assertEquals("12", builder.toString());
        }

        @Test
        public void orElse() throws Exception {
            assertEquals((short) 12, OptionalShort.of((short) 12).orElse((short) 11));
        }

        @Test
        public void orElseGet() throws Exception {
            assertEquals((short) 12, OptionalShort.of((short) 12).orElseGet(() -> (short) 11));
        }

        @Test
        public void orElseThrow() throws Exception {
            assertEquals((short) 12, OptionalShort.of((short) 12).orElseThrow(IllegalArgumentException::new));
        }

        @Test
        public void equals() throws Exception {
            assertEquals(OptionalShort.of((short) 12), OptionalShort.of((short) 12));
            assertNotEquals(OptionalShort.of((short) 12), OptionalShort.of((short) 11));
            assertNotEquals(OptionalShort.of((short) 12), OptionalShort.empty());
            assertNotEquals(OptionalShort.of((short) 12), null);
        }

        @Test
        public void hashcode() throws Exception {
            assertEquals(Short.hashCode((short) 12), OptionalShort.of((short) 12).hashCode());
        }

        @Test
        public void tostring() throws Exception {
            assertEquals("OptionalShort[12]", OptionalShort.of((short) 12).toString());
        }
    }
}