package io.codemonastery.jdbj;

import io.codemonastery.jdbj.db.h2_1_4.H2Rule;
import io.codemonastery.jdbj.lambda.ResultMapper;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.*;

@RunWith(Enclosed.class)
public class PositionalBindingsBuilderTest {

    @ClassRule
    public static final H2Rule db = new H2Rule();

    private static final NamedParameterStatement testStatement =
            NamedParameterStatement.make("SELECT :binding as bound");

    public static class BindingNotFound {
        @Test(expected = IllegalArgumentException.class)
        public void value() throws Exception {
            new TestBuilder().bind(":not_the_mama", pc -> pc.setInt(1));
        }

        @Test(expected = IllegalArgumentException.class)
        public void list() throws Exception {
            new TestBuilder().bindCollection(":not_the_mama", new ArrayList<>());
        }
    }

    public static class CheckBinding {
        @Test
        public void value() throws Exception {
            new TestBuilder().bindCollection(":binding", new ArrayList<>()).checkAllBindingsPresent();
        }

        @Test
        public void list() throws Exception {
            new TestBuilder().bindCollection(":binding", new ArrayList<>()).checkAllBindingsPresent();
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

        @Test(expected = NullPointerException.class)
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

    private static class TestBuilder extends PositionalBindingsBuilder<TestBuilder> {

        TestBuilder() {
            this(testStatement, new PositionalBindings());
        }

        TestBuilder(NamedParameterStatement statement, PositionalBindings bindings) {
            super(statement, bindings, TestBuilder::new);
        }

        <R> R execute(Connection connection, ResultMapper<R> mapper) throws SQLException {
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
