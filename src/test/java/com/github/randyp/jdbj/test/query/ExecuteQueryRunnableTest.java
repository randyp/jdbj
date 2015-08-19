package com.github.randyp.jdbj.test.query;

import com.github.randyp.jdbj.JDBJ;
import com.github.randyp.jdbj.test.binding.value.DBSupplier;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public abstract class ExecuteQueryRunnableTest implements DBSupplier {

    @Test
    public void selectRunExecute() throws Exception {
        final String[] firstColumnName = {null};
        JDBJ.string("SELECT table_catalog FROM information_schema.tables")
                .query()
                .runnable(rs -> firstColumnName[0] = rs.getMetaData().getColumnName(1).toLowerCase())
                .execute(db());

        assertEquals("table_catalog", firstColumnName[0]);
    }

    @Test
    public void selectRunBindValueExecute() throws Exception {
        final int[] count = {0};
        JDBJ.string("SELECT table_name FROM information_schema.tables WHERE LOWER(table_name) = :name")
                .query()
                .runnable(rs -> {
                    while (rs.next()) {
                        count[0]++;
                    }
                })
                .bindString(":name", "tables")
                .execute(db());

        assertEquals(1, count[0]);
    }

    @Test
    public void selectRunBindStringListExecute() throws Exception {
        final int[] count = {0};
        JDBJ.string("SELECT * FROM information_schema.tables WHERE LOWER(table_schema) in :schemas")
                .query()
                .bindStrings(":schemas", "information_schema")
                .runnable(rs -> {
                    while (rs.next()) {
                        count[0]++;
                    }
                })
                .execute(db());

        assertTrue(count[0] > 20);
    }
}
