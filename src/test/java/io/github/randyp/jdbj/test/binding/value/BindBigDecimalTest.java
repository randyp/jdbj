package io.github.randyp.jdbj.test.binding.value;

import io.github.randyp.jdbj.SimpleBuilder;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public abstract class BindBigDecimalTest implements DBSupplier {

    protected final BigDecimal expected = new BigDecimal("1.234");

    @Test
    public void value() throws Exception {
        final BigDecimal selected = builder()
                .bindBigDecimal(":binding", expected)
                .execute(db(), rs -> rs.getBigDecimal(1).setScale(expected.scale(), BigDecimal.ROUND_FLOOR));
        assertEquals(expected, selected);
    }

    @Test
    public void Null() throws Exception {
        final BigDecimal selected = builder()
                .bindBigDecimal(":binding", null)
                .execute(db(), rs -> rs.getBigDecimal(1));
        assertNull(selected);
    }

    public SimpleBuilder builder() {
        return new SimpleBuilder();
    }

}
