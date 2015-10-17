package io.codemonastery.jdbj.test.binding.value;

import io.codemonastery.jdbj.SimpleBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public abstract class BindNStringTest implements DBSupplier {

    protected final String expected = "abcde";

    @Test
    public void value() throws Exception {
        final String selected = builder()
                .bindNString(":binding", expected)
                .execute(db(), rs -> rs.getNString(1));
        assertEquals(expected, selected);
    }

    @Test
    public void Null() throws Exception {
        final String selected = builder()
                .bindNString(":binding", null)
                .execute(db(), rs -> rs.getString(1));
        assertNull(selected);
    }

    public SimpleBuilder builder() {
        return new SimpleBuilder();
    }
}
