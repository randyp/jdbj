package oof.jdbc;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

@RunWith(Enclosed.class)
public class NamedParameterStatementTest {

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

    }


}