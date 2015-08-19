package com.github.randyp.jdbj.test.binding.value;

import com.github.randyp.jdbj.test.SimpleBuilder;
import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public abstract class BindNCharacterStreamTest implements DBSupplier {

    @Test
    public void reader() throws Exception {
        final String expected = "abcde";
        final String selected = new SimpleBuilder()
                .bindNCharacterStream(":binding", new StringReader(expected))
                .execute(db(), rs -> rs.getString(1));
        assertEquals(expected, selected);
    }

    @Test
    public void readerNull() throws Exception {
        final String selected = new SimpleBuilder()
                .bindNCharacterStream(":binding", null)
                .execute(db(), rs -> rs.getString(1));
        assertNull(selected);
    }

    @Test
    public void readerLength() throws Exception {
        final String expected = "abcde";
        final String selected = new SimpleBuilder()
                .bindNCharacterStream(":binding", new StringReader(expected), 5L)
                .execute(db(), rs -> rs.getString(1));
        assertEquals(expected, selected);
    }

    @Test
    public void readerNullLength() throws Exception {
        final String selected = new SimpleBuilder()
                .bindNCharacterStream(":binding", null, 5L)
                .execute(db(), rs -> rs.getString(1));
        assertNull(selected);
    }
}