package com.github.randyp.jdbj.test.binding.value;

import com.github.randyp.jdbj.SimpleBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public abstract class BindShortTest implements DBSupplier{

    protected final Short expected = 12;

    @Test
    public void value() throws Exception {
        final Short selected = builder()
                .bindShort(":binding", expected)
                .execute(db(), rs -> rs.getShort(1));
        assertEquals(expected, selected);
    }

    @Test
    public void Null() throws Exception {
        final Short selected = builder()
                .bindShort(":binding", null)
                .execute(db(), rs -> rs.getShort(1));
        assertNull(selected);
    }

    @Test
    public void primitive() throws Exception {
        final short selected = builder()
                .bindShortPrimitive(":binding", expected)
                .execute(db(), rs -> rs.getShortPrimitive(1));
        assertEquals(expected.shortValue(), selected);
    }

    public SimpleBuilder builder() {
        return new SimpleBuilder();
    }

}
