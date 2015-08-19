package com.github.randyp.jdbj.test.binding.value;

import com.github.randyp.jdbj.test.SimpleBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public abstract class BindByteTest implements DBSupplier {

    @Test
    public void value() throws Exception {
        final Byte expected = 6;
        final Byte selected = new SimpleBuilder()
                .bindByte(":binding", expected)
                .execute(db(), rs -> rs.getByte(1));
        assertEquals(expected, selected, 0.0);
    }

    @Test
    public void Null() throws Exception {
        final Byte selected = new SimpleBuilder()
                .bindByte(":binding", null)
                .execute(db(), rs -> rs.getByte(1));
        assertNull(selected);
    }

    @Test
    public void primitive() throws Exception {
        final byte expected = 6;
        final byte selected = new SimpleBuilder()
                .bindBytePrimitive(":binding", expected)
                .execute(db(), rs -> rs.getBytePrimitive(1));
        assertEquals(expected, selected);
    }

}
