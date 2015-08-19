package com.github.randyp.jdbj.test.binding.value;

import com.github.randyp.jdbj.test.SimpleBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public abstract class BindShortTest implements DBSupplier{

    @Test
    public void value() throws Exception {
        final Short expected = 12;
        final Short selected = new SimpleBuilder()
                .bindShort(":binding", expected)
                .execute(db(), rs -> rs.getShort(1));
        assertEquals(expected, selected);
    }

    @Test
    public void Null() throws Exception {
        final Short selected = new SimpleBuilder()
                .bindShort(":binding", null)
                .execute(db(), rs -> rs.getShort(1));
        assertNull(selected);
    }

    @Test
    public void primitive() throws Exception {
        final short expected = 12;
        final short selected = new SimpleBuilder()
                .bindShortPrimitive(":binding", expected)
                .execute(db(), rs -> rs.getShortPrimitive(1));
        assertEquals(expected, selected);
    }

}
