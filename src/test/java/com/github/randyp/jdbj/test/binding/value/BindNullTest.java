package com.github.randyp.jdbj.test.binding.value;

import com.github.randyp.jdbj.test.SimpleBuilder;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Types;

import static org.junit.Assert.assertNull;

public abstract class BindNullTest implements DBSupplier {

    @Test
    public void type() throws Exception {
        try (Connection connection = db().getConnection()) {
            final String selected = new SimpleBuilder()
                    .bindNull(":binding", Types.VARCHAR)
                    .execute(connection, rs -> rs.getString(1));
            assertNull(selected);
        }
    }

    @Test
    public void typeAndName() throws Exception {
        try (Connection connection = db().getConnection()) {
            final String selected = new SimpleBuilder()
                    .bindNull(":binding", Types.VARCHAR, "varchar")
                    .execute(connection, rs -> rs.getString(1));
            assertNull(selected);
        }
    }
}