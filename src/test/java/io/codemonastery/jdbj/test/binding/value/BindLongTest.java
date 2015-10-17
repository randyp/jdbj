package io.codemonastery.jdbj.test.binding.value;

import io.codemonastery.jdbj.SimpleBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public abstract class BindLongTest implements DBSupplier {

    protected final Long expected = 12L;

    @Test
    public void value() throws Exception {
        final Long selected = builder()
                .bindLong(":binding", expected)
                .execute(db(), rs -> rs.getLong(1));
        assertEquals(expected, selected);
    }

    @Test
    public void Null() throws Exception {
        final Long selected = builder()
                .bindLong(":binding", null)
                .execute(db(), rs -> rs.getLong(1));
        assertNull(selected);
    }

    @Test
    public void primitive() throws Exception {
        final long selected = builder()
                .bindLongPrimitive(":binding", expected)
                .execute(db(), rs -> rs.getLongPrimitive(1));
        assertEquals(expected.longValue(), selected);
    }

    public SimpleBuilder builder() {
        return new SimpleBuilder();
    }

}
