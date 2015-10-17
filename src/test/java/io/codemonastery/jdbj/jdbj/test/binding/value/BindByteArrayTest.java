package io.codemonastery.jdbj.jdbj.test.binding.value;

import io.codemonastery.jdbj.jdbj.SimpleBuilder;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

public abstract class BindByteArrayTest implements DBSupplier {

    protected final byte[] expected = "abcde".getBytes();

    @Test
    public void value() throws Exception {
        final byte[] selected = builder()
                .bindByteArray(":binding", expected)
                .execute(db(), rs -> rs.getBytes(1));
        assertArrayEquals(expected, selected);
    }

    @Test
    public void valueNull() throws Exception {
        final byte[] selected = builder()
                .bindByteArray(":binding", null)
                .execute(db(), rs -> rs.getBytes(1));
        assertNull(selected);
    }

    public SimpleBuilder builder() {
        return new SimpleBuilder("varchar");
    }
}
