package com.github.randyp.jdbj.test.binding.value;

import com.github.randyp.jdbj.test.SimpleBuilder;
import org.junit.Test;

import java.sql.Connection;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

public abstract class BindArrayTest implements DBSupplier {

    public abstract String castType();

    @Test
    public void value() throws Exception {
        final String[] expected = {"a", "b", "c"};
        try (Connection connection = db().getConnection()) {
            final String[] selected = new SimpleBuilder(castType())
                    .bindArray(":binding", connection.createArrayOf("varchar", expected))
                    .execute(db(), rs -> (String[]) rs.getSQLArray(1).getArray());
            assertArrayEquals(expected, selected);
        }
    }

    @Test
    public void Null() throws Exception {
        final Object selected = new SimpleBuilder(castType())
                .bindArray(":binding", null)
                .execute(db(), rs -> rs.getObject(1));
        assertNull(selected);
    }

}
