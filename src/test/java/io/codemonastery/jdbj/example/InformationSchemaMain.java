package io.codemonastery.jdbj.example;

import io.codemonastery.jdbj.JDBJ;
import io.codemonastery.jdbj.MapQuery;
import io.codemonastery.jdbj.SmartResult;
import org.h2.jdbcx.JdbcConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Stream;

public class InformationSchemaMain {


    private static final MapQuery<Table> QueryReadyToGo = JDBJ.string("SELECT ID, TABLE_SCHEMA, TABLE_NAME from information_schema.tables " +
            "WHERE TABLE_SCHEMA = :table_schema").query()
            .map(Table::from);

    public static void main(String[] args) throws Exception {
        final JdbcConnectionPool db = JdbcConnectionPool.create("jdbc:h2:mem:jdbj_example;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE", "sa", "sa");
        try (final Connection connection = db.getConnection()) {
            //can return list
            final List<Table> results = QueryReadyToGo.toList()
                    .bindString(":table_schema", "INFORMATION_SCHEMA")
                    .execute(connection);
            System.out.println(results);


            //or use streams, but be sure to close stream
            try (Stream<Table> stream = QueryReadyToGo.toStream()
                    .bindString(":table_schema", "INFORMATION_SCHEMA")
                    .execute(connection)) {

                stream.filter(table -> table.id < 0)
                        .forEach(System.out::println);
            }
        } finally {
            db.dispose();
        }
    }

    public static class Table {

        public static Table from(SmartResult rs) throws SQLException {
            return new Table(rs.getLongPrimitive("id"), rs.getString("table_schema"), rs.getString("table_name"));
        }

        private final long id;
        private final String schema;
        private final String name;

        public Table(long id, String name, String schema) {
            this.id = id;
            this.name = name;
            this.schema = schema;
        }

        @Override
        public String toString() {
            return "Table{" +
                    "id=" + id +
                    ", schema='" + schema + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
