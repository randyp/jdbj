package com.github.randyp.jdbj.test.binding.value;

import com.github.randyp.jdbj.test.SimpleBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public abstract class BindByteTest implements DBSupplier {

    protected final Byte expected = (byte) 6;

    @Test
    public void value() throws Exception {
        final Byte selected = builder()
                .bindByte(":binding", expected)
                .execute(db(), rs -> rs.getByte(1));
        assertEquals(expected, selected);
    }

    @Test
    public void Null() throws Exception {
        final Byte selected = builder()
                .bindByte(":binding", null)
                .execute(db(), rs -> rs.getByte(1));
        assertNull(selected);
    }

    @Test
    public void primitive() throws Exception {
        final byte selected = builder()
                .bindBytePrimitive(":binding", expected)
                .execute(db(), rs -> rs.getBytePrimitive(1));
        assertEquals(expected.byteValue(), selected);
    }

    public SimpleBuilder builder() {
        return new SimpleBuilder();
    }

}
