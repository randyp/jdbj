package com.github.randyp.jdbj.test.binding.value;

import com.github.randyp.jdbj.test.SimpleBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public abstract class BindIntegerTest implements DBSupplier {

    @Test
    public void value() throws Exception {
        final Integer expected = 12;
        final Integer selected = new SimpleBuilder()
                .bindInteger(":binding", expected)
                .execute(db(), rs -> rs.getInteger(1));
        assertEquals(expected, selected);
    }

    @Test
    public void Null() throws Exception {
        final Integer selected = new SimpleBuilder()
                .bindInteger(":binding", null)
                .execute(db(), rs -> rs.getInteger(1));
        assertNull(selected);
    }

    @Test
    public void primitive() throws Exception {
        final int expected = 12;
        final int selected = new SimpleBuilder()
                .bindIntegerPrimitive(":binding", expected)
                .execute(db(), rs -> rs.getIntegerPrimitive(1));
        assertEquals(expected, selected);
    }

    @Test
    public void primitiveAlias() throws Exception {
        final int expected = 12;
        final int selected = new SimpleBuilder()
                .bindInt(":binding", expected)
                .execute(db(), rs -> rs.getInt(1));
        assertEquals(expected, selected);
    }

}
