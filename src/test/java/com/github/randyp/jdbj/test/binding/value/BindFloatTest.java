package com.github.randyp.jdbj.test.binding.value;

import com.github.randyp.jdbj.test.SimpleBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public abstract class BindFloatTest implements DBSupplier {

    @Test
    public void value() throws Exception {
        final Float expected = 1.2f;
        final Float selected = new SimpleBuilder()
                .bindFloat(":binding", expected)
                .execute(db(), rs -> rs.getFloat(1));
        assertEquals(expected, selected, 0.0f);
    }

    @Test
    public void Null() throws Exception {
        final Float selected = new SimpleBuilder()
                .bindFloat(":binding", null)
                .execute(db(), rs -> rs.getFloat(1));
        assertNull(selected);
    }

    @Test
    public void primitive() throws Exception {
        final float expected = 1.2f;
        final float selected = new SimpleBuilder()
                .bindFloatPrimitive(":binding", expected)
                .execute(db(), rs -> rs.getFloatPrimitive(1));
        assertEquals(expected, selected, 0.0f);
    }

}
