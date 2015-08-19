package com.github.randyp.jdbj.test.binding.value;

import com.github.randyp.jdbj.test.SimpleBuilder;
import org.junit.Test;

import java.net.URL;
import java.sql.Connection;

import static org.junit.Assert.*;

public abstract class BindURLTest implements DBSupplier {

    @Test
    public void value() throws Exception {
        final URL expected = new URL("http", "google.com", 8080, "/");
        final URL selected = new SimpleBuilder()
                .bindURL(":binding", expected)
                .execute(db(), rs -> rs.getURL(1));
        assertEquals(expected, selected);
    }

    @Test
    public void Null() throws Exception {
        try (Connection connection = db().getConnection()) {
            final URL selected = new SimpleBuilder()
                    .bindURL(":binding", null)
                    .execute(connection, rs -> rs.getURL(1));
            assertNull(selected);
        }
    }
}
