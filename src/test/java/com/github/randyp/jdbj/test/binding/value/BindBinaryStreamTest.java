package com.github.randyp.jdbj.test.binding.value;

import com.github.randyp.jdbj.test.SimpleBuilder;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.util.Arrays;

import static org.junit.Assert.*;

public abstract class BindBinaryStreamTest implements DBSupplier {

    @Test
    public void inputStream() throws Exception {
        final byte[] expected = "abcde".getBytes();
        final byte[] selected = new SimpleBuilder(getCastType())
                .bindBinaryStream(":binding", new ByteArrayInputStream(expected))
                .execute(db(), rs -> rs.getBytes(1));
        assertArrayEquals(expected, selected);
    }

    @Test
    public void inputStreamNull() throws Exception {
        final String selected = new SimpleBuilder(getCastType())
                .bindBinaryStream(":binding", null)
                .execute(db(), rs -> {
                    final byte[] bytes = rs.getBytes(1);
                    return bytes == null ? null : new String(bytes);
                });
        assertNull(selected);
    }

    @Test
    public void inputLengthStream() throws Exception {
        final byte[] expected = "abcde".getBytes();
        final byte[] selected = new SimpleBuilder(getCastType())
                .bindBinaryStream(":binding", new ByteArrayInputStream(expected), 5)
                .execute(db(), rs -> rs.getBytes(1));
        assertArrayEquals(expected, selected);
    }

    @Test
    public void inputLengthStreamNull() throws Exception {
        final String selected = new SimpleBuilder(getCastType())
                .bindBinaryStream(":binding", null, 5)
                .execute(db(), rs -> {
                    final byte[] bytes = rs.getBytes(1);
                    return bytes == null ? null : new String(bytes);
                });
        assertNull(selected);
    }

    @Test
    public void inputLengthLongStream() throws Exception {
        final byte[] expected = "abcde".getBytes();
        final byte[] selected = new SimpleBuilder(getCastType())
                .bindBinaryStream(":binding", new ByteArrayInputStream(expected), (long) expected.length)
                .execute(db(), rs -> rs.getBytes(1));
        assertArrayEquals(expected, selected);
    }

    @Test
    public void inputLengthLongStreamNull() throws Exception {
        final String selected = new SimpleBuilder(getCastType())
                .bindBinaryStream(":binding", null, 5L)
                .execute(db(), rs -> {
                    final byte[] bytes = rs.getBytes(1);
                    return bytes == null ? null : new String(bytes);
                });
        assertNull(selected);
    }

    public String getCastType() {
        return "varchar";
    }

}
