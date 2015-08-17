package com.github.randyp.jdbj.optional;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.util.NoSuchElementException;

import static org.junit.Assert.*;

@RunWith(Enclosed.class)
public class OptionalByteTest {

    public static class Empty {
        @Test(expected = NoSuchElementException.class)
        public void get() throws Exception {
            OptionalByte.empty().getAsByte();
        }

        @Test
        public void isPresent() throws Exception {
            assertFalse(OptionalByte.empty().isPresent());
        }

        @Test
        public void ifPresent() throws Exception {
            OptionalByte.empty().ifPresent(b -> fail("is not present, should not have been called"));
        }

        @Test
        public void orElse() throws Exception {
            assertEquals((byte) 12, OptionalByte.empty().orElse((byte) 12));
        }

        @Test
        public void orElseGet() throws Exception {
            assertEquals((byte) 12, OptionalByte.empty().orElseGet(() -> (byte) 12));
        }

        @Test(expected = IllegalArgumentException.class)
        public void orElseThrow() throws Exception {
            OptionalByte.empty().orElseThrow(IllegalArgumentException::new);
        }

        @Test
        public void equals() throws Exception {
            assertEquals(OptionalByte.empty(), OptionalByte.empty());
            assertNotEquals(OptionalByte.empty(), OptionalByte.of((byte) 12));
            assertNotEquals(OptionalByte.empty(), null);
        }

        @Test
        public void hashcode() throws Exception {
            assertEquals(0, OptionalByte.empty().hashCode());
        }

        @Test
        public void tostring() throws Exception {
            assertEquals("OptionalByte.empty", OptionalByte.empty().toString());
        }
    }

    public static class Present {
        @Test
        public void get() throws Exception {
            assertEquals((byte) 12, OptionalByte.of((byte) 12).getAsByte());
        }

        @Test
        public void isPresent() throws Exception {
            assertTrue(OptionalByte.of((byte) 12).isPresent());
        }

        @Test
        public void ifPresent() throws Exception {
            final StringBuilder builder = new StringBuilder();
            OptionalByte.of((byte) 12).ifPresent(builder::append);
            assertEquals("12", builder.toString());
        }

        @Test
        public void orElse() throws Exception {
            assertEquals((byte) 12, OptionalByte.of((byte) 12).orElse((byte) 11));
        }

        @Test
        public void orElseGet() throws Exception {
            assertEquals((byte) 12, OptionalByte.of((byte) 12).orElseGet(() -> (byte) 11));
        }

        @Test
        public void orElseThrow() throws Exception {
            assertEquals((byte) 12, OptionalByte.of((byte) 12).orElseThrow(IllegalArgumentException::new));
        }

        @Test
        public void equals() throws Exception {
            assertEquals(OptionalByte.of((byte) 12), OptionalByte.of((byte) 12));
            assertNotEquals(OptionalByte.of((byte) 12), OptionalByte.of((byte) 11));
            assertNotEquals(OptionalByte.of((byte) 12), OptionalByte.empty());
            assertNotEquals(OptionalByte.of((byte) 12), null);
        }

        @Test
        public void hashcode() throws Exception {
            assertEquals(Short.hashCode((byte) 12), OptionalByte.of((byte) 12).hashCode());
        }

        @Test
        public void tostring() throws Exception {
            assertEquals("OptionalByte[12]", OptionalByte.of((byte) 12).toString());
        }
    }
}