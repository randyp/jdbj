package oof.jdbc;

import java.sql.Connection;
import java.util.List;
import java.util.stream.Stream;

public class PrototypeMain {

    private static final ReturnsBuilder query = JDBJ.query("tables_by_schema.sql");

    private static final MapReturnsBuilder<String> QueryReadyToGo = query
            .map(rs -> rs.getLong("id") + " " + rs.getString("TABLE_SCHEMA") + "." + rs.getString("TABLE_NAME"));

    public static void main(String[] args) throws Exception {
        try (final H2DB db = new H2DB("test");
             final Connection connection = db.getConnection()) {

            //can call arbitrary code against result set
            query.runnable(rs -> System.out.println(rs.getMetaData().getColumnCount() + " columns")).execute(connection);

            //of course can return list
            {
                final List<String> results = QueryReadyToGo.toList()
                        .bindString(":table_schema", "INFORMATION_SCHEMA")
                        .execute(connection);
                System.out.println(results);
            }

            //or use streams, but be sure to close stream
            try (Stream<String> stream = QueryReadyToGo.stream()
                    .bindString(":table_schema", "INFORMATION_SCHEMA")
                    .execute(connection)) {

                stream.filter(s->s.length() > 5).forEach(System.out::println);
            }
            
        }
    }
}
