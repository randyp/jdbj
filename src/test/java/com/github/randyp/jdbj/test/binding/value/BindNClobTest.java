package com.github.randyp.jdbj.test.binding.value;

import com.github.randyp.jdbj.test.SimpleBuilder;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.NClob;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public abstract class BindNClobTest implements DBSupplier {

    @Test
    public void value() throws Exception {
        final String expected = "abcde";
        try (Connection connection = db().getConnection()) {
            final NClob nClob = connection.createNClob();
            nClob.setString(1L, expected);
            final String selected = new SimpleBuilder()
                    .bindNClob(":binding", nClob)
                    .execute(connection, rs -> rs.getNClob(1).getSubString(1L, expected.length()));
            assertEquals(expected, selected);
        }
    }

    @Test
    public void valueNull() throws Exception {
        try (Connection connection = db().getConnection()) {
            final String selected = new SimpleBuilder()
                    .bindNClob(":binding", (NClob) null)
                    .execute(connection, rs -> rs.getString(1));
            assertNull(selected);
        }
    }

    @Test
    public void reader() throws Exception {
        final String expected = "abcde";
        try (Connection connection = db().getConnection()) {
            final String selected = new SimpleBuilder()
                    .bindNClob(":binding", new StringReader(expected))
                    .execute(connection, rs -> rs.getString(1));
            assertEquals(expected, selected);
        }
    }

    @Test
    public void readerNull() throws Exception {
        try (Connection connection = db().getConnection()) {
            final String selected = new SimpleBuilder()
                    .bindNClob(":binding", (Reader) null)
                    .execute(connection, rs -> rs.getString(1));
            assertNull(selected);
        }
    }

    @Test
    public void readerLength() throws Exception {
        final String expected = "abcde";
        try (Connection connection = db().getConnection()) {
            final String selected = new SimpleBuilder()
                    .bindNClob(":binding", new StringReader(expected), (long) expected.length())
                    .execute(connection, rs -> rs.getString(1));
            assertEquals(expected, selected);
        }
    }

    @Test
    public void readerNullLength() throws Exception {
        try (Connection connection = db().getConnection()) {
            final String selected = new SimpleBuilder()
                    .bindNClob(":binding", null, 5L)
                    .execute(connection, rs -> rs.getString(1));
            assertNull(selected);
        }
    }
}
