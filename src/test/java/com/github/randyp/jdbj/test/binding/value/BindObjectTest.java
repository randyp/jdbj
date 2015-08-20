package com.github.randyp.jdbj.test.binding.value;

import com.github.randyp.jdbj.test.SimpleBuilder;
import org.junit.Test;

import java.sql.JDBCType;
import java.sql.Types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public abstract class BindObjectTest implements DBSupplier {

    protected final String expected = "abcde";

    @Test
    public void value() throws Exception {
        final String selected = builder()
                .bindObject(":binding", expected)
                .execute(db(), rs -> rs.getObject(1).toString());
        assertEquals(expected, selected);
    }

    @Test
    public void valueNull() throws Exception {
        final String selected = builder()
                .bindObject(":binding", null)
                .execute(db(), rs -> rs.getString(1));
        assertNull(selected);
    }

    @Test
    public void valueType() throws Exception {
        final String selected = builder()
                .bindObject(":binding", expected, Types.VARCHAR)
                .execute(db(), rs -> rs.getObject(1).toString());
        assertEquals(expected, selected);
    }

    @Test
    public void valueNullType() throws Exception {
        final String selected = builder()
                .bindObject(":binding", null, Types.VARCHAR)
                .execute(db(), rs -> rs.getString(1));
        assertNull(selected);
    }

    @Test
    public void valueTypeLength() throws Exception {
        final String selected = builder()
                .bindObject(":binding", expected, Types.VARCHAR, expected.length())
                .execute(db(), rs -> rs.getObject(1).toString());
        assertEquals(expected, selected);
    }

    @Test
    public void valueNullTypeLength() throws Exception {
        final String selected = builder()
                .bindObject(":binding", null, Types.VARCHAR, 5)
                .execute(db(), rs -> rs.getString(1));
        assertNull(selected);
    }

    @Test
    public void valueSQLType() throws Exception {
        final String selected = builder()
                .bindObject(":binding", expected, JDBCType.VARCHAR)
                .execute(db(), rs -> rs.getObject(1).toString());
        assertEquals(expected, selected);
    }

    @Test
    public void valueNullSQLType() throws Exception {
        final String selected = builder()
                .bindObject(":binding", null, JDBCType.VARCHAR)
                .execute(db(), rs -> rs.getString(1));
        assertNull(selected);
    }

    @Test
    public void valueSQLTypeLength() throws Exception {
        final String selected = builder()
                .bindObject(":binding", expected, JDBCType.VARCHAR, expected.length())
                .execute(db(), rs -> rs.getObject(1).toString());
        assertEquals(expected, selected);
    }

    @Test
    public void valueNullSQLTypeLength() throws Exception {
        final String selected = builder()
                .bindObject(":binding", null, JDBCType.VARCHAR, 5)
                .execute(db(), rs -> rs.getString(1));
        assertNull(selected);
    }

    public SimpleBuilder builder() {
        return new SimpleBuilder();
    }
}
