package com.github.randyp.jdbj.test.binding.value;

import com.github.randyp.jdbj.test.SimpleBuilder;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public abstract class BindBigDecimalTest implements DBSupplier {

    @Test
    public void value() throws Exception {
        final BigDecimal expected = new BigDecimal("1.234");
        final BigDecimal selected = new SimpleBuilder()
                .bindBigDecimal(":binding", expected)
                .execute(db(), rs -> rs.getBigDecimal(1));
        assertEquals(expected, selected);
    }

    @Test
    public void Null() throws Exception {
        final BigDecimal selected = new SimpleBuilder()
                .bindBigDecimal(":binding", null)
                .execute(db(), rs -> rs.getBigDecimal(1));
        assertNull(selected);
    }

}
