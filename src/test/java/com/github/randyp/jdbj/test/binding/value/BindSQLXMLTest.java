package com.github.randyp.jdbj.test.binding.value;

import com.github.randyp.jdbj.test.SimpleBuilder;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLXML;

import static org.junit.Assert.*;

public abstract class BindSQLXMLTest implements DBSupplier {

    @Test
    public void value() throws Exception {
        final String expected = "<a></a>";
        try (Connection connection = db().getConnection()) {
            final SQLXML xml = connection.createSQLXML();
            xml.setString(expected);
            final String selected = new SimpleBuilder()
                    .bindSQLXML(":binding", xml)
                    .execute(connection, rs -> rs.getSQLXML(1).getString());
            assertEquals(expected, selected);
        }
    }

    @Test
    public void Null() throws Exception {
        try (Connection connection = db().getConnection()) {
            final String selected = new SimpleBuilder()
                    .bindSQLXML(":binding", null)
                    .execute(connection, rs -> {
                        final SQLXML xml = rs.getSQLXML(1);
                        return xml == null ? null : xml.getString();
                    });
            assertNull(selected);
        }
    }
}
