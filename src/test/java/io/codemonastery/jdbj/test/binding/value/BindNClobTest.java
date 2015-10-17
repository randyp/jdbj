package io.codemonastery.jdbj.test.binding.value;

import io.codemonastery.jdbj.SimpleBuilder;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.NClob;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public abstract class BindNClobTest implements DBSupplier {

    protected final String expected = "abcde";

    @Test
    public void value() throws Exception {
        try (Connection connection = db().getConnection()) {
            final NClob nClob = connection.createNClob();
            assertNotNull("Driver created null NClob", nClob);
            nClob.setString(1L, expected);
            final String selected = builder()
                    .bindNClob(":binding", nClob)
                    .execute(connection, rs -> rs.getNClob(1).getSubString(1L, expected.length()));
            assertEquals(expected, selected);
        }
    }

    @Test
    public void valueNull() throws Exception {
        try (Connection connection = db().getConnection()) {
            final String selected = builder()
                    .bindNClob(":binding", (NClob) null)
                    .execute(connection, rs -> rs.getString(1));
            assertNull(selected);
        }
    }

    @Test
    public void reader() throws Exception {
        try (Connection connection = db().getConnection()) {
            final String selected = builder()
                    .bindNClob(":binding", expectedReader())
                    .execute(connection, rs -> rs.getString(1));
            assertEquals(expected, selected);
        }
    }

    @Test
    public void readerNull() throws Exception {
        try (Connection connection = db().getConnection()) {
            final String selected = builder()
                    .bindNClob(":binding", (Reader) null)
                    .execute(connection, rs -> rs.getString(1));
            assertNull(selected);
        }
    }

    @Test
    public void readerLength() throws Exception {
        try (Connection connection = db().getConnection()) {
            final String selected = builder()
                    .bindNClob(":binding", expectedReader(), (long) expected.length())
                    .execute(connection, rs -> rs.getString(1));
            assertEquals(expected, selected);
        }
    }

    @Test
    public void readerNullLength() throws Exception {
        try (Connection connection = db().getConnection()) {
            final String selected = builder()
                    .bindNClob(":binding", null, 5L)
                    .execute(connection, rs -> rs.getString(1));
            assertNull(selected);
        }
    }

    public SimpleBuilder builder() {
        return new SimpleBuilder();
    }

    public StringReader expectedReader() {
        return new StringReader(expected);
    }
}
