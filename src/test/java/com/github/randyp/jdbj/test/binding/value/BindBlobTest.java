package com.github.randyp.jdbj.test.binding.value;

import com.github.randyp.jdbj.JDBJ;
import com.github.randyp.jdbj.test.SimpleBuilder;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Blob;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

public abstract class BindBlobTest implements DBSupplier {

    protected final byte[] expected = "abcde".getBytes();

    @Test
    public void value() throws Exception {
        final byte[] selected = JDBJ.returningTransaction(db(), connection -> {
            final Blob blob = connection.createBlob();
            blob.setBytes(1, expected);
            return builder()
                    .bindBlob(":binding", blob)
                    .execute(connection, rs -> rs.getBlob(1).getBytes(1, expected.length));
        });
        assertArrayEquals(expected, selected);
    }

    @Test
    public void valueNull() throws Exception {
        final byte[] selected = JDBJ.returningTransaction(db(), connection -> {
            final Blob blob = connection.createBlob();
            blob.setBytes(1, null);
            return builder()
                    .bindBlob(":binding", blob)
                    .execute(connection, rs -> rs.getBytes(1));
        });
        assertNull(selected);
    }

    @Test
    public void inputStream() throws Exception {
        final byte[] selected = JDBJ.returningTransaction(db(), connection -> {
            //noinspection CodeBlock2Expr
            return builder()
                    .bindBlob(":binding", expectedStream())
                    .execute(connection, rs -> rs.getBlob(1).getBytes(1, expected.length));
        });
        assertArrayEquals(expected, selected);
    }

    @Test
    public void inputStreamNull() throws Exception {
        final byte[] selected = JDBJ.returningTransaction(db(), connection -> {
            //noinspection CodeBlock2Expr
            return builder()
                    .bindBlob(":binding", (InputStream) null)
                    .execute(connection, rs -> rs.getBytes(1));
        });
        assertNull(selected);
    }

    @Test
    public void inputStreamLength() throws Exception {
        final byte[] selected = JDBJ.returningTransaction(db(), connection -> {
            //noinspection CodeBlock2Expr
            return builder()
                    .bindBlob(":binding", expectedStream(), (long) expected.length)
                    .execute(connection, rs -> rs.getBlob(1).getBytes(1, expected.length));
        });
        assertArrayEquals(expected, selected);
    }

    @Test
    public void inputStreamNullLength() throws Exception {
        final byte[] selected = JDBJ.returningTransaction(db(), connection -> {
            //noinspection CodeBlock2Expr
            return builder()
                    .bindBlob(":binding", null, 5L)
                    .execute(connection, rs -> rs.getBytes(1));
        });
        assertNull(selected);
    }

    public SimpleBuilder builder() {
        return new SimpleBuilder();
    }

    public ByteArrayInputStream expectedStream() {
        return new ByteArrayInputStream(expected);
    }
}
