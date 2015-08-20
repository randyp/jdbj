package com.github.randyp.jdbj.test.binding.value;

import com.github.randyp.jdbj.test.SimpleBuilder;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public abstract class BindBooleanTest implements DBSupplier {

    @Test
    public void False() throws Exception {
        final Boolean selected = builder()
                .bindBoolean(":binding", Boolean.FALSE)
                .execute(db(), rs -> rs.getBoolean(1));
        assertFalse(selected);
    }

    @Test
    public void True() throws Exception {
        final Boolean selected = builder()
                .bindBoolean(":binding", Boolean.TRUE)
                .execute(db(), rs -> rs.getBoolean(1));
        assertTrue(selected);

    }

    @Test
    public void Null() throws Exception {
        final Boolean selected = builder()
                .bindBoolean(":binding", null)
                .execute(db(), rs -> rs.getBoolean(1));
        assertNull(selected);
    }

    @Test
    public void primitiveFalse() throws Exception {
        final boolean selected = builder()
                .bindBooleanPrimitive(":binding", false)
                .execute(db(), rs -> rs.getBooleanPrimitive(1));
        assertFalse(selected);
    }

    @Test
    public void primitiveTrue() throws Exception {
        final boolean selected = builder()
                .bindBooleanPrimitive(":binding", true)
                .execute(db(), rs -> rs.getBooleanPrimitive(1));
        assertTrue(selected);
    }

    public SimpleBuilder builder() {
        return new SimpleBuilder();
    }
}
