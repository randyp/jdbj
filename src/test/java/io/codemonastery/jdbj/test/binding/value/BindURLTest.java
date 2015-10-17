package io.codemonastery.jdbj.test.binding.value;

import io.codemonastery.jdbj.SimpleBuilder;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;

import static org.junit.Assert.*;

public abstract class BindURLTest implements DBSupplier {

    protected final URL expected;

    public BindURLTest() {
        try {
            this.expected = new URL("http", "google.com", 8080, "/");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void value() throws Exception {
        final URL selected = builder()
                .bindURL(":binding", expected)
                .execute(db(), rs -> rs.getURL(1));
        assertEquals(expected, selected);
    }

    @Test
    public void Null() throws Exception {
        try (Connection connection = db().getConnection()) {
            final URL selected = builder()
                    .bindURL(":binding", null)
                    .execute(connection, rs -> rs.getURL(1));
            assertNull(selected);
        }
    }

    public SimpleBuilder builder() {
        return new SimpleBuilder();
    }
}
