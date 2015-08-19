package com.github.randyp.jdbj.test.binding.value;

import com.github.randyp.jdbj.test.SimpleBuilder;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public abstract class BindAsciiStreamTest implements DBSupplier {

    @Test
    public void stream() throws Exception {
        final String expected = "abcde";
        final String selected = new SimpleBuilder()
                .bindAsciiStream(":binding", new ByteArrayInputStream(expected.getBytes(Charset.forName("ascii"))))
                .execute(db(), rs -> rs.getString(1));
        assertEquals(expected, selected);
    }

    @Test
    public void streamNull() throws Exception {
        final String selected = new SimpleBuilder()
                .bindAsciiStream(":binding", null)
                .execute(db(), rs -> rs.getString(1));
        assertNull(selected);
    }

    @Test
    public void streamLength() throws Exception {
        final String expected = "abcde";
        final String selected = new SimpleBuilder()
                .bindAsciiStream(":binding", new ByteArrayInputStream(expected.getBytes(Charset.forName("ascii"))), expected.length())
                .execute(db(), rs -> rs.getString(1));
        assertEquals(expected, selected);
    }

    @Test
    public void streamNullLength() throws Exception {
        final String selected = new SimpleBuilder()
                .bindAsciiStream(":binding", null, 5)
                .execute(db(), rs -> rs.getString(1));
        assertNull(selected);
    }

    @Test
    public void streamLengthLong() throws Exception {
        final String expected = "abcde";
        final String selected = new SimpleBuilder()
                .bindAsciiStream(":binding", new ByteArrayInputStream(expected.getBytes(Charset.forName("ascii"))), (long) expected.length())
                .execute(db(), rs -> rs.getString(1));
        assertEquals(expected, selected);
    }

    @Test
    public void streamNullLengthLong() throws Exception {
        final String selected = new SimpleBuilder()
                .bindAsciiStream(":binding", null, 5L)
                .execute(db(), rs -> rs.getString(1));
        assertNull(selected);
    }
}
