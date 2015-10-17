package io.codemonastery.jdbj.test.binding.value;

import io.codemonastery.jdbj.SimpleBuilder;
import org.junit.Test;

import java.io.ByteArrayInputStream;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

public abstract class BindBinaryStreamTest implements DBSupplier {

    protected final byte[] expected = "abcde".getBytes();

    @Test
    public void inputStream() throws Exception {
        final byte[] selected = builder()
                .bindBinaryStream(":binding", expectedStream())
                .execute(db(), rs -> rs.getBytes(1));
        assertArrayEquals(expected, selected);
    }

    @Test
    public void inputStreamNull() throws Exception {
        final byte[] selected = builder()
                .bindBinaryStream(":binding", null)
                .execute(db(), rs -> rs.getBytes(1));
        assertNull(selected);
    }

    @Test
    public void inputLengthStream() throws Exception {
        final byte[] selected = builder()
                .bindBinaryStream(":binding", expectedStream(), 5)
                .execute(db(), rs -> rs.getBytes(1));
        assertArrayEquals(expected, selected);
    }

    @Test
    public void inputLengthStreamNull() throws Exception {
        final byte[] selected = builder()
                .bindBinaryStream(":binding", null, 5)
                .execute(db(), rs -> rs.getBytes(1));
        assertNull(selected);
    }

    @Test
    public void inputLengthLongStream() throws Exception {
        final byte[] selected = builder()
                .bindBinaryStream(":binding", expectedStream(), (long) expected.length)
                .execute(db(), rs -> rs.getBytes(1));
        assertArrayEquals(expected, selected);
    }

    @Test
    public void inputLengthLongStreamNull() throws Exception {
        final byte[] selected = builder()
                .bindBinaryStream(":binding", null, (long) expected.length)
                .execute(db(), rs -> rs.getBytes(1));
        assertNull(selected);
    }

    public SimpleBuilder builder() {
        return new SimpleBuilder();
    }

    public ByteArrayInputStream expectedStream() {
        return new ByteArrayInputStream(expected);
    }
}
