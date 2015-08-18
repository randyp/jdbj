package com.github.randyp.jdbj;

import com.github.randyp.jdbj.lambda.ResultSetMapper;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Enclosed.class)
public class DefaultCollectionBindingsBuilderTest {

    @ClassRule
    public static final H2Rule db = new H2Rule();

    private static final NamedParameterStatement statement =
            NamedParameterStatement.make("SELECT :binding as bound");

    public static class DefaultBindList {
        @Test(expected = IllegalArgumentException.class)
        public void nullName() throws Exception {
            new TestBuilder().bindDefaultList(null, Collections.singletonList(pc -> pc.setInt(1)));
        }

        @Test(expected = IllegalArgumentException.class)
        public void nulList() throws Exception {
            new TestBuilder().bindDefaultList(":binding", null);
        }
    }

    public static class DefaultStrings {
        @Test
        public void values() throws Exception {
            final String[] expected = {"abc", "def"};

            final Object[] selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultStrings(":binding", Arrays.asList(expected))
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
            }
            assertArrayEquals(expected, selected);
        }

        @Test
        public void valuesInArray() throws Exception {
            final String[] expected = {"abc", "def"};

            final Object[] selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultStrings(":binding", expected)
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
            }
            assertArrayEquals(expected, selected);
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullArray() throws Exception {
            try (Connection connection = db.getConnection()) {
                new TestBuilder()
                        .bindDefaultStrings(":binding", (String[]) null)
                        .execute(connection, rs -> rs.getObject(1));
            }
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullList() throws Exception {
            try (Connection connection = db.getConnection()) {
                new TestBuilder()
                        .bindDefaultStrings(":binding", (List<String>) null)
                        .execute(connection, rs -> rs.getObject(1));
            }
        }
    }

    public static class DefaultLongs {
        @Test
        public void values() throws Exception {
            final long[] input = {152L, 51L};
            final Long[] expected = {152L, 51L};

            final Object[] selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindDefaultLongs(":binding", input)
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
            }
            assertArrayEquals(expected, selected);
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullArray() throws Exception {
            try (Connection connection = db.getConnection()) {
                new TestBuilder()
                        .bindDefaultLongs(":binding", (long[]) null)
                        .execute(connection, rs -> rs.getObject(1));
            }
        }
    }

    private static class TestBuilder extends PositionalBindingsBuilder<TestBuilder> {

        TestBuilder() {
            this(DefaultCollectionBindingsBuilderTest.statement, PositionalBindings.empty());
        }

        TestBuilder(NamedParameterStatement statement, PositionalBindings bindings) {
            super(statement, bindings, TestBuilder::new);
        }

        <R> R execute(Connection connection, ResultSetMapper<R> mapper) throws SQLException {
            checkAllBindingsPresent();

            final R value;
            try (PreparedStatement ps = connection.prepareStatement(buildSql())) {
                bindToStatement(ps);
                try (SmartResultSet rs = new SmartResultSet(ps.executeQuery())) {
                    assertTrue(rs.next());
                    value = mapper.map(rs);
                    assertFalse(rs.next());
                }
            }
            return value;
        }
    }

}