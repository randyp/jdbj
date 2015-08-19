package com.github.randyp.jdbj.test.query;

import com.github.randyp.jdbj.ExecuteQuery;
import com.github.randyp.jdbj.JDBJ;
import com.github.randyp.jdbj.StreamQuery;
import com.github.randyp.jdbj.test.binding.value.DBSupplier;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public abstract class ExecuteQueryTest implements DBSupplier {

    @Test
    public void selectMapToListExecute() throws Exception {
        final ExecuteQuery<List<String>> query = JDBJ.query("SELECT table_name FROM information_schema.tables WHERE LOWER(table_name) = 'tables'")
                .map(rs -> rs.getString("table_name").toLowerCase())
                .toList();
        final List<String> results = query.execute(db());
        assertEquals(Collections.singletonList("tables"), results);
    }

    @Test
    public void selectMapToListBindLongsExecute() throws Exception {
        final ExecuteQuery<List<String>> query = JDBJ.query("SELECT table_name FROM information_schema.tables WHERE LOWER(table_name) in :names")
                .map(rs -> rs.getString("table_name").toLowerCase())
                .toList()
                .bindStrings(":names", "tables");

        final List<String> results = query.execute(db());
        assertEquals(Collections.singletonList("tables"), results);
    }

    @Test
    public void selectMapFirstBindLongsExecute() throws Exception {
        final ExecuteQuery<Optional<String>> query = JDBJ.query("SELECT table_name FROM information_schema.tables WHERE LOWER(table_name) in :names")
                .map(rs -> rs.getString("table_name").toLowerCase())
                .first()
                .bindStrings(":names", "tables");

        final Optional<String> result = query.execute(db());
        assertTrue(result.isPresent());
        assertEquals("tables", result.get());
    }

    @Test
    public void selectMapRemapFirstBindLongsExecute() throws Exception {
        //noinspection RedundantArrayCreation
        final ExecuteQuery<Optional<Integer>> query = JDBJ.query("SELECT table_name FROM information_schema.tables WHERE LOWER(table_name) in :names")
                .map(rs -> rs.getString("table_name").toLowerCase())
                .remap(String::length)
                .first()
                .bindStrings(":names", "tables");

        final Optional<Integer> result = query.execute(db());
        assertTrue(result.isPresent());
        assertEquals("tables".length(), result.get().intValue());
    }

    @Test
    public void selectMapBindLongsStreamExecute() throws Exception {
        //noinspection RedundantArrayCreation
        final StreamQuery<String> query = JDBJ.query("SELECT table_name FROM information_schema.tables WHERE LOWER(table_name) in :names")
                .map(rs -> rs.getString("table_name").toLowerCase())
                .bindStrings(":names", "tables")
                .toStream();

        final Optional<String> result;
        try (Stream<String> stream = query.execute(db())) {
            result = stream.findFirst();
        }

        assertTrue(result.isPresent());
        assertEquals("tables", result.get());
    }

    @Test
    public void query() throws Exception {
        final ExecuteQuery<Optional<String>> query = JDBJ.resource("tables_by_schema.sql")
                .query()
                .map(rs -> rs.getString("table_name").toLowerCase())
                .bindString(":table_schema", "information_schema")
                .first();
        final Optional<String> result = query.execute(db());
        assertNotNull(result);
    }

    @Test
     public void defaultValues() throws Exception {
        final ExecuteQuery<List<String>> query = JDBJ.query("SELECT * FROM information_schema.tables LIMIT :limit")
                .bindDefault(":limit", pc -> pc.setInt(5))
                .map(rs -> rs.getString("table_name"))
                .toList();

        assertEquals(5, query.execute(db()).size());
        assertEquals(10, query.bindInt(":limit", 10).execute(db()).size());
    }

    @Ignore
    @Test
    public void defaultValuesLists() throws Exception {
        final ExecuteQuery<List<String>> query = JDBJ.query("SELECT * FROM information_schema.tables WHERE LOWER(table_name) in :names")
                .map(rs -> rs.getString("table_name"))
                .toList()
                .bindDefaultStrings(":names", "tables");

        assertEquals(1, query.execute(db()).size());
        assertEquals(2, query.bindStrings(":names", "tables", "schemata").execute(db()).size());
    }
}
