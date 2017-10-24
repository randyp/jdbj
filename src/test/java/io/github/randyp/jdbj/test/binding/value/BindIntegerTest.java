package io.github.randyp.jdbj.test.binding.value;

import io.github.randyp.jdbj.SimpleBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public abstract class BindIntegerTest implements DBSupplier {

    protected final Integer expected = 12;

    @Test
    public void value() throws Exception {
        final Integer selected = builder()
                .bindInteger(":binding", expected)
                .execute(db(), rs -> rs.getInteger(1));
        assertEquals(expected, selected);
    }

    @Test
    public void Null() throws Exception {
        final Integer selected = builder()
                .bindInteger(":binding", null)
                .execute(db(), rs -> rs.getInteger(1));
        assertNull(selected);
    }

    @Test
    public void primitive() throws Exception {
        final int selected = builder()
                .bindIntegerPrimitive(":binding", expected)
                .execute(db(), rs -> rs.getIntegerPrimitive(1));
        assertEquals(expected.intValue(), selected);
    }

    @Test
    public void primitiveAlias() throws Exception {
        final int selected = builder()
                .bindInt(":binding", expected)
                .execute(db(), rs -> rs.getInt(1));
        assertEquals(expected.intValue(), selected);
    }

    public SimpleBuilder builder() {
        return new SimpleBuilder();
    }

}
