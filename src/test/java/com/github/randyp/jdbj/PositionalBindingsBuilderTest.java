package com.github.randyp.jdbj;

import com.github.randyp.jdbj.lambda.ResultSetMapper;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.*;
import java.sql.Date;
import java.util.*;

import static org.junit.Assert.*;

@RunWith(Enclosed.class)
public class PositionalBindingsBuilderTest {

    @ClassRule
    public static final H2Rule db = new H2Rule();

    private static final NamedParameterStatement statement =
            NamedParameterStatement.make("SELECT :binding as bound");

    public static class BindingNotFound {
        @Test(expected = IllegalArgumentException.class)
        public void value() throws Exception {
            new TestBuilder().bind(":not_the_mama", pc -> pc.setInt(1));
        }

        @Test(expected = IllegalArgumentException.class)
        public void list() throws Exception {
            new TestBuilder().bindList(":not_the_mama", new ArrayList<>());
        }
    }

    public static class CheckBinding {
        @Test
        public void value() throws Exception {
            new TestBuilder().bindList(":binding", new ArrayList<>()).checkAllBindingsPresent();
        }

        @Test
        public void list() throws Exception {
            new TestBuilder().bindList(":binding", new ArrayList<>()).checkAllBindingsPresent();
        }

        @Test(expected = IllegalStateException.class)
        public void missing() throws Exception {
            new TestBuilder().checkAllBindingsPresent();
        }
    }

    public static class Bind {
        @Test
        public void bind() throws Exception {
            final String expected = "1";

            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bind(":binding", pc -> pc.setString(expected))
                        .execute(connection, rs -> rs.getString(1));
            }
            assertEquals(expected, selected);
        }

        @Test
        public void valueNotSet() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bind(":binding", pc -> {
                        })
                        .execute(connection, rs -> rs.getString(1));
            }
            assertNull(selected);
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullBinding() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bind(":binding", null)
                        .execute(connection, rs -> rs.getString(1));
            }
            assertNull(selected);
        }
    }

    public static class BindList {
        @Test
        public void valueInListNotSet() throws Exception {
            final String selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindList(":binding", Collections.singletonList(pc -> {
                        }))
                        .execute(connection, rs -> rs.getString(1));
            }
            assertNull(selected);
        }
    }

    public static class strings {
        @Test
        public void values() throws Exception {
            final String[] expected = {"abc", "def"};

            final Object[] selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindStrings(":binding", Arrays.asList(expected))
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
            }
            assertArrayEquals(expected, selected);
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullArray() throws Exception {
            try (Connection connection = db.getConnection()) {
                new TestBuilder()
                        .bindStrings(":binding", (String[]) null)
                        .execute(connection, rs -> rs.getObject(1));
            }
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullList() throws Exception {
            try (Connection connection = db.getConnection()) {
                new TestBuilder()
                        .bindStrings(":binding", (List<String>) null)
                        .execute(connection, rs -> rs.getObject(1));
            }
        }
    }

    public static class longs {
        @Test
        public void values() throws Exception {
            final long[] input = {152L, 51L};
            final Long[] expected = {152L, 51L};

            final Object[] selected;
            try (Connection connection = db.getConnection()) {
                selected = new TestBuilder()
                        .bindLongs(":binding", input)
                        .execute(connection, rs -> (Object[]) rs.getObject(1));
            }
            assertArrayEquals(expected, selected);
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullArray() throws Exception {
            try (Connection connection = db.getConnection()) {
                new TestBuilder()
                        .bindLongs(":binding", (long[]) null)
                        .execute(connection, rs -> rs.getObject(1));
            }
        }
    }

    private static class TestBuilder extends PositionalBindingsBuilder<TestBuilder> {

        TestBuilder() {
            this(PositionalBindingsBuilderTest.statement, PositionalBindings.empty());
        }

        TestBuilder(NamedParameterStatement statement, PositionalBindings bindings) {
            super(statement, bindings, TestBuilder::new);
        }

        <R> R execute(Connection connection, ResultSetMapper<R> mapper) throws SQLException {
            checkAllBindingsPresent();

            final R value;
            try (PreparedStatement ps = connection.prepareStatement(buildSql())) {
                bindToStatement(ps);
                try (ResultSet rs = ps.executeQuery()) {
                    assertTrue(rs.next());
                    value = mapper.map(rs);
                    assertFalse(rs.next());
                }
            }
            return value;
        }
    }
}