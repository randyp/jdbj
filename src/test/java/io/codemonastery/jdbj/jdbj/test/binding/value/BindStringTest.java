package io.codemonastery.jdbj.jdbj.test.binding.value;

import io.codemonastery.jdbj.jdbj.SimpleBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public abstract class BindStringTest implements DBSupplier {

    protected final String expected = "abcde";

    @Test
    public void value() throws Exception {
        final String selected = builder()
                .bindString(":binding", expected)
                .execute(db(), rs -> rs.getString(1));
        assertEquals(expected, selected);
    }

    @Test
    public void valueNull() throws Exception {
        final String selected = builder()
                .bindString(":binding", null)
                .execute(db(), rs -> rs.getString(1));
        assertNull(selected);
    }

    public SimpleBuilder builder() {
        return new SimpleBuilder();
    }

}
