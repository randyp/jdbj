package io.github.randyp.jdbj.test.binding.value;

import io.github.randyp.jdbj.SimpleBuilder;
import org.junit.Test;

import java.sql.Connection;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

public abstract class BindArrayTest implements DBSupplier {

    protected final String[] expected = new String[]{"a", "b", "c"};

    @Test
    public void value() throws Exception {
        try (Connection connection = db().getConnection()) {
            final String[] selected = builder()
                    .bindArray(":binding", connection.createArrayOf("varchar", expected))
                    .execute(db(), rs -> (String[]) rs.getSQLArray(1).getArray());
            assertArrayEquals(expected, selected);
        }
    }

    @Test
    public void Null() throws Exception {
        final Object selected = builder()
                .bindArray(":binding", null)
                .execute(db(), rs -> rs.getObject(1));
        assertNull(selected);
    }

    public SimpleBuilder builder() {
        return new SimpleBuilder("varchar");
    }

}
