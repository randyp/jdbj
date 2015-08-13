package oof.jdbc;

import org.junit.ClassRule;
import org.junit.Test;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class JDBJTest {

    @ClassRule
    public static final H2Rule db = new H2Rule();

    @Test
    public void selectRunExecute() throws Exception {
        final String[] firstColumnName = {null};
        final ExecuteQueryRunnable query = JDBJ.queryString("SELECT * FROM INFORMATION_SCHEMA.TABLES")
                .runnable(rs -> firstColumnName[0] = rs.getMetaData().getColumnName(1));

        try (Connection connection = db.getConnection()) {
            query.execute(connection);
        }

        assertEquals("TABLE_CATALOG", firstColumnName[0]);
    }

    @Test
    public void selectRunBindValueExecute() throws Exception {
        final int[] count = {0};
        final ExecuteQueryRunnable query = JDBJ.queryString("SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE id = :id")
                .runnable(rs -> {
                    while (rs.next()) {
                        count[0]++;
                    }
                })
                .bindLong(":id", -28);

        try (Connection connection = db.getConnection()) {
            query.execute(connection);
        }

        assertEquals(1, count[0]);
    }

    @Test
    public void selectRunBindStringListExecute() throws Exception {
        final int[] count = {0};
        final ExecuteQueryRunnable query = JDBJ.queryString("SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA in :schemas")
                .runnable(rs -> {
                    while(rs.next()){
                        count[0]++;
                    }
                })
                .bindStrings(":schemas", "INFORMATION_SCHEMA");

        try (Connection connection = db.getConnection()) {
            query.execute(connection);
        }

        assertEquals(29, count[0]);
    }

    @Test
    public void selectMapToListExecute() throws Exception {
        final ExecuteQuery<List<String>> query = JDBJ.queryString("SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE id = -28")
                .map(rs->rs.getString("TABLE_NAME"))
                .toList();

        final List<String> results;
        try (Connection connection = db.getConnection()) {
            results = query.execute(connection);
        }

        assertEquals(Collections.singletonList("SESSION_STATE"), results);
    }

    @Test
    public void selectMapToListBindLongsExecute() throws Exception {
        //noinspection RedundantArrayCreation
        final ExecuteQuery<List<String>> query = JDBJ.queryString("SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE id in :ids")
                .map(rs->rs.getString("TABLE_NAME"))
                .toList()
                .bindLongs(":ids", new long[]{-28L});

        final List<String> results;
        try (Connection connection = db.getConnection()) {
            results = query.execute(connection);
        }

        assertEquals(Collections.singletonList("SESSION_STATE"), results);
    }

    @Test
    public void selectMapFirstBindLongsExecute() throws Exception {
        //noinspection RedundantArrayCreation
        final ExecuteQuery<String> query = JDBJ.queryString("SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE id in :ids")
                .map(rs -> rs.getString("TABLE_NAME"))
                .first()
                .bindLongs(":ids", new long[]{-28L});

        final String result;
        try (Connection connection = db.getConnection()) {
            result = query.execute(connection);
        }

        assertEquals("SESSION_STATE", result);
    }

    @Test
    public void selectMapRemapFirstBindLongsExecute() throws Exception {
        //noinspection RedundantArrayCreation
        final ExecuteQuery<Integer> query = JDBJ.queryString("SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE id in :ids")
                .map(rs -> rs.getString("TABLE_NAME"))
                .remap(String::length)
                .first()
                .bindLongs(":ids", new long[]{-28L});

        final Integer result;
        try (Connection connection = db.getConnection()) {
            result = query.execute(connection);
        }

        assertNotNull(result);
        assertEquals("SESSION_STATE".length(), result.intValue());
    }

    @Test
    public void selectMapBindLongsStreamExecute() throws Exception {
        //noinspection RedundantArrayCreation
        final StreamQuery<String> query = JDBJ.queryString("SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE id in :ids")
                .map(rs -> rs.getString("TABLE_NAME"))
                .stream()
                .bindLongs(":ids", new long[]{-28L});

        final Optional<String> result;
        try (Connection connection = db.getConnection()) {
            try (Stream<String> stream = query.execute(connection)) {
                result = stream.findFirst();
            }
        }

        assertTrue(result.isPresent());
        assertEquals("SESSION_STATE", result.get());
    }

    @Test
    public void query() throws Exception {
        final ExecuteQuery<String> query = JDBJ.query("tables_by_schema.sql")
                .map(rs -> rs.getString("TABLE_NAME"))
                .first()
                .bindString(":table_schema", "INFORMATION_SCHEMA");

        final String result;
        try (Connection connection = db.getConnection()) {
            result = query.execute(connection);
        }

        assertNotNull(result);
    }

    @Test(expected = RuntimeException.class)
    public void queryResourceNotFound() throws Exception {
        //noinspection AccessStaticViaInstance
        new JDBJ().query("tables_by_froozgaba.sql");
    }
}
