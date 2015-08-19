package com.github.randyp.jdbj.test.binding.value;

import com.github.randyp.jdbj.test.SimpleBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public abstract class BindNStringTest implements DBSupplier {

    @Test
    public void value() throws Exception {
        final String expected = "abcde";
        final String selected = new SimpleBuilder()
                .bindNString(":binding", expected)
                .execute(db(), rs -> rs.getNString(1));
        assertEquals(expected, selected);
    }

    @Test
    public void Null() throws Exception {
        final String selected = new SimpleBuilder()
                .bindNString(":binding", null)
                .execute(db(), rs -> rs.getString(1));
        assertNull(selected);
    }
}
