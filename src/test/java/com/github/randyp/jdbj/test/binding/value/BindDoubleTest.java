package com.github.randyp.jdbj.test.binding.value;

import com.github.randyp.jdbj.SimpleBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public abstract class BindDoubleTest implements DBSupplier{

    protected final Double expected = 1.2;

    @Test
    public void value() throws Exception {
        final Double selected = builder()
                .bindDouble(":binding", expected)
                .execute(db(), rs -> rs.getDouble(1));
        assertEquals(expected, selected, 0.0);
    }

    @Test
    public void Null() throws Exception {
        final Double selected = builder()
                .bindDouble(":binding", null)
                .execute(db(), rs -> rs.getDouble(1));
        assertNull(selected);
    }

    @Test
    public void primitive() throws Exception {
        final double selected = builder()
                .bindDoublePrimitive(":binding", expected)
                .execute(db(), rs -> rs.getDoublePrimitive(1));
        assertEquals(expected, selected, 0.0);
    }

    public SimpleBuilder builder() {
        return new SimpleBuilder();
    }
}
