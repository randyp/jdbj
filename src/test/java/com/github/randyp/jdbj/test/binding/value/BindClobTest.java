package com.github.randyp.jdbj.test.binding.value;

import com.github.randyp.jdbj.test.SimpleBuilder;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.sql.Clob;
import java.sql.Connection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public abstract class BindClobTest implements DBSupplier {

    @Test
    public void value() throws Exception {
        final String expected = "abcd";
        try (Connection connection = db().getConnection()) {
            final Clob clob = connection.createClob();
            clob.setString(1, expected);
            final String selected = new SimpleBuilder()
                    .bindClob(":binding", clob)
                    .execute(connection, rs -> rs.getClob(1).getSubString(1L, expected.length()));
            assertEquals(expected, selected);
        }
    }

    @Test
    public void valueNull() throws Exception {
        try (Connection connection = db().getConnection()) {
            final String selected = new SimpleBuilder()
                    .bindClob(":binding", (Clob) null)
                    .execute(connection, rs -> {
                        final byte[] bytes = rs.getBytes(1);
                        return bytes == null ? null : new String(bytes);
                    });
            assertNull(selected);
        }
    }

    @Test
    public void reader() throws Exception {
        final String expected = "abcd";
        try (Connection connection = db().getConnection()) {
            final String selected = new SimpleBuilder()
                    .bindClob(":binding", new StringReader(expected))
                    .execute(connection, rs -> rs.getClob(1).getSubString(1L, expected.length()));
            assertEquals(expected, selected);
        }
    }

    @Test
    public void readerNull() throws Exception {
        try (Connection connection = db().getConnection()) {
            final String selected = new SimpleBuilder()
                    .bindClob(":binding", (Reader) null)
                    .execute(connection, rs -> {
                        final byte[] bytes = rs.getBytes(1);
                        return bytes == null ? null : new String(bytes);
                    });
            assertNull(selected);
        }
    }

    @Test
    public void readerLength() throws Exception {
        final String expected = "abcd";
        try (Connection connection = db().getConnection()) {
            final String selected = new SimpleBuilder()
                    .bindClob(":binding", new StringReader(expected), (long) expected.length())
                    .execute(connection, rs -> rs.getClob(1).getSubString(1L, expected.length()));
            assertEquals(expected, selected);
        }
    }

    @Test
    public void readerLengthNull() throws Exception {
        try (Connection connection = db().getConnection()) {
            final String selected = new SimpleBuilder()
                    .bindClob(":binding", null, 4L)
                    .execute(connection, rs -> {
                        final byte[] bytes = rs.getBytes(1);
                        return bytes == null ? null : new String(bytes);
                    });
            assertNull(selected);
        }
    }
    
}
