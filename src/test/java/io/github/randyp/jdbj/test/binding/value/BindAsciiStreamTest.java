package io.github.randyp.jdbj.test.binding.value;

import io.github.randyp.jdbj.SimpleBuilder;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public abstract class BindAsciiStreamTest implements DBSupplier {

    protected final String expected = "abcde";

    @Test
    public void stream() throws Exception {

        final String selected = builder()
                .bindAsciiStream(":binding", expectedStream())
                .execute(db(), rs -> rs.getString(1));
        assertEquals(expected, selected);
    }

    @Test
    public void streamNull() throws Exception {
        final String selected = builder()
                .bindAsciiStream(":binding", null)
                .execute(db(), rs -> rs.getString(1));
        assertNull(selected);
    }

    @Test
    public void streamLength() throws Exception {
        final String selected = builder()
                .bindAsciiStream(":binding", expectedStream(), expected.length())
                .execute(db(), rs -> rs.getString(1));
        assertEquals(expected, selected);
    }

    @Test
    public void streamNullLength() throws Exception {
        final String selected = builder()
                .bindAsciiStream(":binding", null, 0)
                .execute(db(), rs -> rs.getString(1));
        assertNull(selected);
    }

    @Test
    public void streamLengthLong() throws Exception {
        final String selected = builder()
                .bindAsciiStream(":binding", expectedStream(), (long) expected.length())
                .execute(db(), rs -> rs.getString(1));
        assertEquals(expected, selected);
    }

    @Test
    public void streamNullLengthLong() throws Exception {
        final String selected = builder()
                .bindAsciiStream(":binding", null, 5L)
                .execute(db(), rs -> rs.getString(1));
        assertNull(selected);
    }

    public ByteArrayInputStream expectedStream() {
        return new ByteArrayInputStream(expected.getBytes(Charset.forName("ascii")));
    }

    public SimpleBuilder builder() {
        return new SimpleBuilder();
    }
}
