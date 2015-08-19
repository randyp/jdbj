package com.github.randyp.jdbj.test.binding.value;

import com.github.randyp.jdbj.test.SimpleBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public abstract class BindDoubleTest implements DBSupplier{

    @Test
    public void value() throws Exception {
        final Double expected = 1.2;
        final Double selected = new SimpleBuilder()
                .bindDouble(":binding", expected)
                .execute(db(), rs -> rs.getDouble(1));
        assertEquals(expected, selected, 0.0);
    }

    @Test
    public void Null() throws Exception {
        final Double selected = new SimpleBuilder()
                .bindDouble(":binding", null)
                .execute(db(), rs -> rs.getDouble(1));
        assertNull(selected);
    }

    @Test
    public void primitive() throws Exception {
        final double expected = 1.2;
        final double selected = new SimpleBuilder()
                .bindDoublePrimitive(":binding", expected)
                .execute(db(), rs -> rs.getDoublePrimitive(1));
        assertEquals(expected, selected, 0.0);
    }
}
