package com.github.randyp.jdbj;

import org.junit.ClassRule;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Spliterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class ResultSetSpliteratorTest {

    @ClassRule
    public static final H2Rule db = new H2Rule();

    @Test
    public void noResults() throws Exception {
        try(final Connection connection = db.getConnection();
            final PreparedStatement ps = connection.prepareStatement("SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE false");
            final ResultSet rs = ps.executeQuery()){
            final ResultSetSpliterator<String> spliterator = new ResultSetSpliterator<>(rs, r -> r.getString("table_name"));
            assertFalse(spliterator.tryAdvance(s->{}));
        }
    }

    @Test(expected = AdvanceFailedException.class)
    public void exceptionInAdvance() throws Exception {
        try(final Connection connection = db.getConnection();
            final PreparedStatement ps = connection.prepareStatement("SELECT * FROM INFORMATION_SCHEMA.TABLES");
            final ResultSet rs = ps.executeQuery()){
            final ResultSetSpliterator<String> spliterator = new ResultSetSpliterator<>(rs, r -> r.getString("table_name"));
            spliterator.tryAdvance(s -> {
                throw new NullPointerException("someone unboxed a null or something");
            });
        }
    }

    @Test
    public void someResults() throws Exception {
        final List<String> tableNames = new ArrayList<>();
        try(final Connection connection = db.getConnection();
            final PreparedStatement ps = connection.prepareStatement("SELECT * FROM INFORMATION_SCHEMA.TABLES ORDER BY table_name ASC");
            final ResultSet rs = ps.executeQuery()){
            final ResultSetSpliterator<String> spliterator = new ResultSetSpliterator<>(rs, r -> r.getString("table_name"));
            spliterator.forEachRemaining(tableNames::add);
        }
        assertEquals(29, tableNames.size());
        assertEquals(Arrays.asList("CATALOGS", "COLLATIONS"), tableNames.subList(0, 2));
    }

    @Test
    public void trySplit() throws Exception {
        try(final Connection connection = db.getConnection();
            final PreparedStatement ps = connection.prepareStatement("SELECT * FROM INFORMATION_SCHEMA.TABLES ORDER BY table_name ASC");
            final ResultSet rs = ps.executeQuery()){
            final ResultSetSpliterator<String> spliterator = new ResultSetSpliterator<>(rs, r -> r.getString("table_name"));
            assertNull(spliterator.trySplit());
        }
    }

    @Test
    public void estimateSize() throws Exception {
        try(final Connection connection = db.getConnection();
            final PreparedStatement ps = connection.prepareStatement("SELECT * FROM INFORMATION_SCHEMA.TABLES ORDER BY table_name ASC");
            final ResultSet rs = ps.executeQuery()){
            final ResultSetSpliterator<String> spliterator = new ResultSetSpliterator<>(rs, r -> r.getString("table_name"));
            assertEquals(Long.MAX_VALUE, spliterator.estimateSize());
        }
    }

    @Test
    public void characteristics() throws Exception {
        try(final Connection connection = db.getConnection();
            final PreparedStatement ps = connection.prepareStatement("SELECT * FROM INFORMATION_SCHEMA.TABLES ORDER BY table_name ASC");
            final ResultSet rs = ps.executeQuery()){
            final ResultSetSpliterator<String> spliterator = new ResultSetSpliterator<>(rs, r -> r.getString("table_name"));
            assertEquals(Spliterator.IMMUTABLE, spliterator.characteristics());
        }
    }
}