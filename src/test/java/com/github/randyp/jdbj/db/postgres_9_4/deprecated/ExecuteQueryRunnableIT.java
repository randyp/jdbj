package com.github.randyp.jdbj.db.postgres_9_4.deprecated;

import com.github.randyp.jdbj.ExecuteQueryRunnable;
import com.github.randyp.jdbj.JDBJ;
import org.junit.Test;

import java.sql.Connection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ExecuteQueryRunnableIT extends StudentTest {
    @Test
    public void selectRunExecute() throws Exception {
        final String[] firstColumnName = {null};
        final ExecuteQueryRunnable query = JDBJ.string("SELECT * FROM information_schema.tables")
                .query()
                .runnable(rs -> firstColumnName[0] = rs.getMetaData().getColumnName(1));

        try (Connection connection = db.getConnection()) {
            query.execute(connection);
        }

        assertEquals("table_catalog", firstColumnName[0]);
    }

    @Test
    public void selectRunBindValueExecute() throws Exception {
        final int[] count = {0};
        final ExecuteQueryRunnable query = JDBJ.string("SELECT * FROM information_schema.tables WHERE table_schema = :table_schema")
                .query()
                .runnable(rs -> {
                    while (rs.next()) {
                        count[0]++;
                    }
                })
                .bindString(":table_schema", "information_schema");

        query.execute(db);

        assertTrue(count[0] > 0);
    }

    @Test
    public void selectRunBindStringListExecute() throws Exception {
        final int[] count = {0};
        final ExecuteQueryRunnable query = JDBJ.string("SELECT * FROM information_schema.tables WHERE table_schema in :table_schemas")
                .query()
                .bindStrings(":table_schemas", "information_schema", "pg_catalog")
                .runnable(rs -> {
                    while (rs.next()) {
                        count[0]++;
                    }
                });

        try (Connection connection = db.getConnection()) {
            query.execute(connection);
        }

        assertTrue(count[0] > 100);
    }
}
