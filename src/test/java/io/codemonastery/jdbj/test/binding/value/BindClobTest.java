package io.codemonastery.jdbj.test.binding.value;

import io.codemonastery.jdbj.SimpleBuilder;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.sql.Clob;
import java.sql.Connection;

import static org.junit.Assert.*;

public abstract class BindClobTest implements DBSupplier {

    protected final String expected = "abcd";

    @Test
    public void value() throws Exception {
        try (Connection connection = db().getConnection()) {
            final Clob clob = connection.createClob();
            assertNotNull("Driver created null clob", clob);
            clob.setString(1, expected);
            final String selected = builder()
                    .bindClob(":binding", clob)
                    .execute(connection, rs -> rs.getClob(1).getSubString(1L, expected.length()));
            assertEquals(expected, selected);
        }
    }

    @Test
    public void valueNull() throws Exception {
        try (Connection connection = db().getConnection()) {
            final String selected = builder()
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
        try (Connection connection = db().getConnection()) {
            final String selected = builder()
                    .bindClob(":binding", expectedReader())
                    .execute(connection, rs -> rs.getClob(1).getSubString(1L, expected.length()));
            assertEquals(expected, selected);
        }
    }

    @Test
    public void readerNull() throws Exception {
        try (Connection connection = db().getConnection()) {
            final String selected = builder()
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
        try (Connection connection = db().getConnection()) {
            final String selected = builder()
                    .bindClob(":binding", expectedReader(), (long) expected.length())
                    .execute(connection, rs -> rs.getClob(1).getSubString(1L, expected.length()));
            assertEquals(expected, selected);
        }
    }

    @Test
    public void readerLengthNull() throws Exception {
        try (Connection connection = db().getConnection()) {
            final String selected = builder()
                    .bindClob(":binding", null, 4L)
                    .execute(connection, rs -> {
                        final byte[] bytes = rs.getBytes(1);
                        return bytes == null ? null : new String(bytes);
                    });
            assertNull(selected);
        }
    }

    public StringReader expectedReader() {
        return new StringReader(expected);
    }

    public SimpleBuilder builder() {
        return new SimpleBuilder();
    }

}
