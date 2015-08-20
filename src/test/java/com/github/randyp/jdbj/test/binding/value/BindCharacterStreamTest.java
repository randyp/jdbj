package com.github.randyp.jdbj.test.binding.value;

import com.github.randyp.jdbj.test.SimpleBuilder;
import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public abstract class BindCharacterStreamTest implements DBSupplier {

    protected final String expected = "abcde";

    @Test
    public void reader() throws Exception {
        final String selected = builder()
                .bindCharacterStream(":binding", new StringReader(expected))
                .execute(db(), rs -> rs.getString(1));
        assertEquals(expected, selected);
    }

    @Test
    public void readerNull() throws Exception {
        final String selected = builder()
                .bindCharacterStream(":binding", null)
                .execute(db(), rs -> rs.getString(1));
        assertNull(selected);
    }

    @Test
    public void readerLength() throws Exception {
        final String selected = builder()
                .bindCharacterStream(":binding", new StringReader(expected), 5)
                .execute(db(), rs -> rs.getString(1));
        assertEquals(expected, selected);
    }

    @Test
    public void readerLengthNull() throws Exception {
        final String selected = builder()
                .bindCharacterStream(":binding", null, 5)
                .execute(db(), rs -> rs.getString(1));
        assertNull(selected);
    }

    @Test
    public void readerLengthLong() throws Exception {
        final String selected = builder()
                .bindCharacterStream(":binding", new StringReader(expected), 5L)
                .execute(db(), rs -> rs.getString(1));
        assertEquals(expected, selected);
    }

    @Test
    public void readerLengthLongNull() throws Exception {
        final String selected = builder()
                .bindCharacterStream(":binding", null, 5L)
                .execute(db(), rs -> rs.getString(1));
        assertNull(selected);
    }

    public SimpleBuilder builder() {
        return new SimpleBuilder();
    }

}
