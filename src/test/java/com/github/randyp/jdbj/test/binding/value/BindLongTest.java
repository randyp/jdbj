package com.github.randyp.jdbj.test.binding.value;

import com.github.randyp.jdbj.test.SimpleBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public abstract class BindLongTest implements DBSupplier {

    @Test
    public void value() throws Exception {
        final Long expected = 12L;
        final Long selected = new SimpleBuilder()
                .bindLong(":binding", expected)
                .execute(db(), rs -> rs.getLong(1));
        assertEquals(expected, selected);
    }

    @Test
    public void Null() throws Exception {
        final Long selected = new SimpleBuilder()
                .bindLong(":binding", null)
                .execute(db(), rs -> rs.getLong(1));
        assertNull(selected);
    }

    @Test
    public void primitive() throws Exception {
        final long expected = 12L;
        final long selected = new SimpleBuilder()
                .bindLongPrimitive(":binding", expected)
                .execute(db(), rs -> rs.getLongPrimitive(1));
        assertEquals(expected, selected);
    }

}
