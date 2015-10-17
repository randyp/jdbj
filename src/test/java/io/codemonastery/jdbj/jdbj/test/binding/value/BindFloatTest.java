package io.codemonastery.jdbj.jdbj.test.binding.value;

import io.codemonastery.jdbj.jdbj.SimpleBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public abstract class BindFloatTest implements DBSupplier {

    protected final Float expected = 1.2f;

    @Test
    public void value() throws Exception {
        final Float selected = builder()
                .bindFloat(":binding", expected)
                .execute(db(), rs -> rs.getFloat(1));
        assertEquals(expected, selected, 0.0f);
    }

    @Test
    public void Null() throws Exception {
        final Float selected = builder()
                .bindFloat(":binding", null)
                .execute(db(), rs -> rs.getFloat(1));
        assertNull(selected);
    }

    @Test
    public void primitive() throws Exception {
        final float selected = builder()
                .bindFloatPrimitive(":binding", expected)
                .execute(db(), rs -> rs.getFloatPrimitive(1));
        assertEquals(expected, selected, 0.0f);
    }

    public SimpleBuilder builder() {
        return new SimpleBuilder();
    }

}
