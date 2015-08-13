package oof.jdbc;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Enclosed.class)
public class NamedParameterStatementTest {

    public static class contains_parameter {
        @Test
        public void does() throws Exception {
            final NamedParameterStatement statement = NamedParameterStatement.make("select id from agent_secret WHERE status = :status");
            assertTrue(statement.containsParameter(":status"));
        }

        @Test
        public void doesNot() throws Exception {
            final NamedParameterStatement statement = NamedParameterStatement.make("select id from agent_secret WHERE status = :status");
            assertFalse(statement.containsParameter(":id"));
        }

        @Test(expected = IllegalArgumentException.class)
        public void Null() throws Exception {
            NamedParameterStatement.make("select id from agent_secret WHERE status = :status")
                    .containsParameter(null);
        }
    }

    public static class jdbc_sql {

        @Test
        public void noBindings() throws Exception {
            final String sql = "select id from agent_secret WHERE status = 'ACTIVE'";
            final NamedParameterStatement statement = NamedParameterStatement.make(sql);
            assertEquals(sql, statement.jdbcSql(Bindings.empty()));
        }

        @Test
        public void valueBinding() throws Exception {
            final NamedParameterStatement statement = NamedParameterStatement.make("select id from agent_secret WHERE status = :status");
            final Bindings bindings = Bindings.empty().addValueBinding(":status", pc -> pc.setString("ACTIVE"));

            assertEquals("select id from agent_secret WHERE status = ?", statement.jdbcSql(bindings));
        }

        @Test
         public void listBinding_empty() throws Exception {
            final NamedParameterStatement statement = NamedParameterStatement.make("select id from agent_secret WHERE status in :statuses");
            final Bindings bindings = Bindings.empty().addListBinding(":statuses", new ArrayList<>());

            assertEquals("select id from agent_secret WHERE status in ()", statement.jdbcSql(bindings));
        }

        @Test
        public void listBinding_singleton() throws Exception {
            final NamedParameterStatement statement = NamedParameterStatement.make("select id from agent_secret WHERE status in :statuses");
            final Bindings bindings = Bindings.empty().addListBinding(":statuses", Collections.singletonList(pc -> pc.setString("ACTIVE")));

            assertEquals("select id from agent_secret WHERE status in (?)", statement.jdbcSql(bindings));
        }

        @Test
        public void listBinding() throws Exception {
            final NamedParameterStatement statement = NamedParameterStatement.make("select id from agent_secret WHERE status in :statuses");
            final Bindings bindings = Bindings.empty().addListBinding(":statuses", Arrays.asList(pc -> pc.setString("ACTIVE"), pc->pc.setString("MIA")));

            assertEquals("select id from agent_secret WHERE status in (?,?)", statement.jdbcSql(bindings));
        }

        @Test(expected = IllegalArgumentException.class)
        public void Null() throws Exception {
            NamedParameterStatement.make("select id from agent_secret WHERE status in :statuses")
            .jdbcSql(null);
        }

    }

    public static class bind {

        @ClassRule
        public static final H2Rule db = new H2Rule();

        @Test(expected = IllegalArgumentException.class)
        public void preparedStatementNull() throws Exception {
            final NamedParameterStatement statement = NamedParameterStatement.make("SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE id = :id");
            final Bindings bindings = Bindings.empty().addValueBinding(":id", pc -> pc.setInt(-29));
            statement.bind(null, bindings);
        }

        @Test(expected = IllegalArgumentException.class)
        public void bindingsNull() throws Exception {
            final NamedParameterStatement statement = NamedParameterStatement.make("SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE id = :id");
            final Bindings bindings = Bindings.empty().addValueBinding(":id", pc -> pc.setInt(-29));
            try(Connection connection = db.getConnection();
                PreparedStatement ps = connection.prepareStatement(statement.jdbcSql(bindings))){
                statement.bind(ps, null);
            }
        }
    }

    public static class check {

        @Test
        public void presentAsValue() throws Exception {
            final NamedParameterStatement statement = NamedParameterStatement.make("SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE id = :id");
            final Bindings bindings = Bindings.empty().addValueBinding(":id", pc -> pc.setInt(-29));
            statement.checkAllBindingsPresent(bindings);
        }

        @Test
        public void presentAsList() throws Exception {
            final NamedParameterStatement statement = NamedParameterStatement.make("SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE id = :id");
            final Bindings bindings = Bindings.empty().addListBinding(":id", Collections.singletonList(pc -> pc.setInt(-29)));
            statement.checkAllBindingsPresent(bindings);
        }

        @Test(expected = IllegalStateException.class)
         public void notPresent() throws Exception {
            final NamedParameterStatement statement = NamedParameterStatement.make("SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE id = :id");
            final Bindings bindings = Bindings.empty();
            statement.checkAllBindingsPresent(bindings);
        }

        @Test(expected = IllegalArgumentException.class)
        public void bindingsNull() throws Exception {
            NamedParameterStatement.make("SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE id = :id")
                    .checkAllBindingsPresent(null);
        }
    }
}