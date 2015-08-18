package com.github.randyp.jdbj;

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
        final ExecuteQueryRunnable query = JDBJ.string("SELECT * FROM INFORMATION_SCHEMA.TABLES")
                .query()
                .runnable(rs -> firstColumnName[0] = rs.getMetaData().getColumnName(1));

        try (Connection connection = db.getConnection()) {
            query.execute(connection);
        }

        assertEquals("TABLE_CATALOG", firstColumnName[0]);
    }

    @Test
    public void selectRunBindValueExecute() throws Exception {
        final int[] count = {0};
        final ExecuteQueryRunnable query = JDBJ.string("SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE id = :id")
                .query()
                .runnable(rs -> {
                    while (rs.next()) {
                        count[0]++;
                    }
                })
                .bindLong(":id", -28L);

        try (Connection connection = db.getConnection()) {
            query.execute(connection);
        }

        assertEquals(1, count[0]);
    }

    @Test
    public void selectRunBindStringListExecute() throws Exception {
        final int[] count = {0};
        final ExecuteQueryRunnable query = JDBJ.string("SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA in :schemas")
                .query()
                .bindStrings(":schemas", "INFORMATION_SCHEMA")
                .runnable(rs -> {
                    while (rs.next()) {
                        count[0]++;
                    }
                });

        try (Connection connection = db.getConnection()) {
            query.execute(connection);
        }

        assertEquals(29, count[0]);
    }

}
