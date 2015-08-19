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

    @Test
    public void value() throws Exception {
        final byte[] expected = "abcde".getBytes();
        final byte[] selected = JDBJ.returningTransaction(db(), connection -> {
            final Blob blob = connection.createBlob();
            blob.setBytes(1, expected);
            return new SimpleBuilder()
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
            return new SimpleBuilder()
                    .bindBlob(":binding", blob)
                    .execute(connection, rs -> rs.getBytes(1));
        });
        assertNull(selected);
    }

    @Test
    public void inputStream() throws Exception {
        final byte[] expected = "abcde".getBytes();
        final byte[] selected = JDBJ.returningTransaction(db(), connection -> {
            //noinspection CodeBlock2Expr
            return new SimpleBuilder()
                    .bindBlob(":binding", new ByteArrayInputStream(expected))
                    .execute(connection, rs -> rs.getBlob(1).getBytes(1, expected.length));
        });
        assertArrayEquals(expected, selected);
    }

    @Test
    public void inputStreamNull() throws Exception {
        final byte[] selected = JDBJ.returningTransaction(db(), connection -> {
            //noinspection CodeBlock2Expr
            return new SimpleBuilder()
                    .bindBlob(":binding", (InputStream) null)
                    .execute(connection, rs -> rs.getBytes(1));
        });
        assertNull(selected);
    }

    @Test
    public void inputStreamLength() throws Exception {
        final byte[] expected = "abcde".getBytes();
        final byte[] selected = JDBJ.returningTransaction(db(), connection -> {
            //noinspection CodeBlock2Expr
            return new SimpleBuilder()
                    .bindBlob(":binding", new ByteArrayInputStream(expected), (long) expected.length)
                    .execute(connection, rs -> rs.getBlob(1).getBytes(1, expected.length));
        });
        assertArrayEquals(expected, selected);
    }

    @Test
    public void inputStreamNullLength() throws Exception {
        final byte[] selected = JDBJ.returningTransaction(db(), connection -> {
            //noinspection CodeBlock2Expr
            return new SimpleBuilder()
                    .bindBlob(":binding", null, 5L)
                    .execute(connection, rs -> rs.getBytes(1));
        });
        assertNull(selected);
    }
}
