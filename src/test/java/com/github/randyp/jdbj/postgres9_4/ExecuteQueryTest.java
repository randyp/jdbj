package com.github.randyp.jdbj.postgres9_4;

import com.github.randyp.jdbj.ExecuteQuery;
import com.github.randyp.jdbj.JDBJ;
import com.github.randyp.jdbj.StreamQuery;
import org.junit.Test;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class ExecuteQueryTest extends StudentTest {

    @Test
    public void selectMapToListExecute() throws Exception {
        final ExecuteQuery<List<String>> query = JDBJ.query("SELECT * FROM information_schema.tables WHERE table_name = 'tables'")
                .map(rs -> rs.getString("table_name"))
                .toList();
        final List<String> results = query.execute(db);
        assertEquals(Collections.singletonList("tables"), results);
    }

    @Test
    public void selectMapToListBindLongsExecute() throws Exception {
        //noinspection RedundantArrayCreation
        final ExecuteQuery<List<String>> query = JDBJ.query("SELECT * FROM information_schema.tables WHERE table_name in :table_names")
                .map(rs -> rs.getString("table_name"))
                .toList()
                .bindStrings(":table_names", "tables");
        final List<String> results = query.execute(db);
        assertEquals(Collections.singletonList("tables"), results);
    }

    @Test
    public void selectMapFirstBindStringsExecute() throws Exception {
        //noinspection RedundantArrayCreation
        final ExecuteQuery<Optional<String>> query = JDBJ.query("SELECT * FROM information_schema.tables WHERE table_name in :table_names")
                .map(rs -> rs.getString("TABLE_NAME"))
                .first()
                .bindStrings(":table_names", "tables");

        final Optional<String> result = query.execute(db);
        assertTrue(result.isPresent());
        assertEquals("tables", result.get());
    }

    @Test
    public void selectMapRemapFirstBindLongsExecute() throws Exception {
        //noinspection RedundantArrayCreation
        final ExecuteQuery<Optional<Integer>> query = JDBJ.query("SELECT * FROM information_schema.tables WHERE table_name in :table_names")
                .map(rs -> rs.getString("TABLE_NAME"))
                .remap(String::length)
                .first()
                .bindStrings(":table_names", "tables");

        final Optional<Integer> result = query.execute(db);

        assertTrue(result.isPresent());
        assertEquals("tables".length(), result.get().intValue());
    }

    @Test
    public void selectMapBindLongsStreamExecute() throws Exception {
        //noinspection RedundantArrayCreation
        final StreamQuery<String> query = JDBJ.query("SELECT * FROM information_schema.tables WHERE table_name in :table_names")
                .map(rs -> rs.getString("table_name"))
                .bindStrings(":table_names", "tables")
                .toStream();

        final Optional<String> result;
        try (Stream<String> stream = query.execute(db)) {
            result = stream.findFirst();
        }

        assertTrue(result.isPresent());
        assertEquals("tables", result.get());
    }

    @Test
    public void query() throws Exception {
        final ExecuteQuery<Optional<String>> query = JDBJ.query("SELECT TABLE_SCHEMA, TABLE_NAME from information_schema.tables WHERE TABLE_SCHEMA = :table_schema")
                .map(rs -> rs.getString("table_name"))
                .bindString(":table_schema", "information_Schema")
                .first();

        final Optional<String> result = query.execute(db);
        assertNotNull(result);
    }

    @Test
     public void defaultValues() throws Exception {
        final ExecuteQuery<List<String>> query = JDBJ.query("SELECT * FROM information_schema.tables LIMIT :limit")
                .bindDefault(":limit", pc -> pc.setInt(5))
                .map(rs -> rs.getString("table_name"))
                .toList();

        assertEquals(5, query.execute(db).size());
        assertEquals(10, query.bindInt(":limit", 10).execute(db).size());
    }

    @Test
    public void defaultValuesLists() throws Exception {
        final ExecuteQuery<List<String>> query = JDBJ.query("SELECT * FROM information_schema.tables WHERE table_name in :table_names")
                .map(rs -> rs.getString("table_name"))
                .toList()
                .bindDefaultStrings(":table_names", "tables");

        try (Connection connection = db.getConnection()) {
            assertEquals(1, query.execute(connection).size());
            assertEquals(2, query.bindStrings(":table_names", "tables", "schemata").execute(connection).size());
        }
    }
}
