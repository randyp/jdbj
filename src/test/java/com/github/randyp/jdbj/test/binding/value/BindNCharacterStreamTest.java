package com.github.randyp.jdbj.test.binding.value;

import com.github.randyp.jdbj.test.SimpleBuilder;
import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public abstract class BindNCharacterStreamTest implements DBSupplier {

    protected final String expected = "abcde";

    @Test
    public void reader() throws Exception {
        final String selected = builder()
                .bindNCharacterStream(":binding", expectedReader())
                .execute(db(), rs -> rs.getString(1));
        assertEquals(expected, selected);
    }

    @Test
    public void readerNull() throws Exception {
        final String selected = builder()
                .bindNCharacterStream(":binding", null)
                .execute(db(), rs -> rs.getString(1));
        assertNull(selected);
    }

    @Test
    public void readerLength() throws Exception {
        final String selected = builder()
                .bindNCharacterStream(":binding", expectedReader(), 5L)
                .execute(db(), rs -> rs.getString(1));
        assertEquals(expected, selected);
    }

    @Test
    public void readerNullLength() throws Exception {
        final String selected = builder()
                .bindNCharacterStream(":binding", null, 5L)
                .execute(db(), rs -> rs.getString(1));
        assertNull(selected);
    }

    public SimpleBuilder builder() {
        return new SimpleBuilder();
    }

    public StringReader expectedReader() {
        return new StringReader(expected);
    }
}
