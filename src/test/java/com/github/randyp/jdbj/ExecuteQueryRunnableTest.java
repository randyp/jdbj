package com.github.randyp.jdbj;

import com.github.randyp.jdbj.db.h2_1_4.H2Rule;
import org.junit.ClassRule;
import org.junit.Test;

import java.sql.Connection;

import static org.junit.Assert.assertEquals;

public class ExecuteQueryRunnableTest {

    @ClassRule
    public static final H2Rule db = new H2Rule();

    @Test
    public void selectRunExecute() throws Exception {
        final String[] firstColumnName = {null};
        JDBJ.string("SELECT * FROM INFORMATION_SCHEMA.TABLES")
                .query()
                .runnable(rs -> firstColumnName[0] = rs.getMetaData().getColumnName(1))
                .execute(db);

        assertEquals("TABLE_CATALOG", firstColumnName[0]);
    }

    @Test
    public void selectRunBindValueExecute() throws Exception {
        final int[] count = {0};
        JDBJ.string("SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE id = :id")
                .query()
                .runnable(rs -> {
                    while (rs.next()) {
                        count[0]++;
                    }
                })
                .bindLong(":id", -28L)
                .execute(db);

        assertEquals(1, count[0]);
    }

    @Test
    public void selectRunBindStringListExecute() throws Exception {
        final int[] count = {0};
        JDBJ.string("SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA in :schemas")
                .query()
                .bindStrings(":schemas", "INFORMATION_SCHEMA")
                .runnable(rs -> {
                    while (rs.next()) {
                        count[0]++;
                    }
                })
                .execute(db);

        assertEquals(29, count[0]);
    }
}
