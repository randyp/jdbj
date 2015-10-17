package io.codemonastery.jdbj;

import io.codemonastery.jdbj.SmartResultSet;
import io.codemonastery.jdbj.db.h2_1_4.H2Rule;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLWarning;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(Enclosed.class)
public class ValueBindingsBuilderTest {

    @ClassRule
    public static final H2Rule db = new H2Rule();

    public static class GetMetadata {
        @Test
        public void get() throws Exception {
            try (Connection connection = db.getConnection();
                 PreparedStatement ps = connection.prepareStatement("SELECT id, table_name FROM INFORMATION_SCHEMA.TABLES");
                 SmartResultSet rs = new SmartResultSet(ps.executeQuery())) {
                assertEquals(2, rs.getMetaData().getColumnCount());
            }
        }
    }

    public static class FindColumn {

        @Test
        public void find() throws Exception {
            try (Connection connection = db.getConnection();
                 PreparedStatement ps = connection.prepareStatement("SELECT id, table_name FROM INFORMATION_SCHEMA.TABLES");
                 SmartResultSet rs = new SmartResultSet(ps.executeQuery())) {
                assertEquals(1, rs.findColumn("id"));
                assertEquals(2, rs.findColumn("table_name"));
            }
        }
    }

    public static class GetWarnings {

        @Test
        public void none() throws Exception {
            try (Connection connection = db.getConnection();
                 PreparedStatement ps = connection.prepareStatement("SELECT id, table_name FROM INFORMATION_SCHEMA.TABLES");
                 SmartResultSet rs = new SmartResultSet(ps.executeQuery())) {
                final SQLWarning warnings = rs.getWarnings();
                assertNull(warnings);
            }
        }
    }

}
