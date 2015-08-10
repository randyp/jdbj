package oof.jdbc;

import java.sql.Connection;
import java.util.List;
import java.util.stream.Stream;

public class PrototypeMain {

    public static void main(String[] args) throws Exception {
        try (final H2DB db = new H2DB("test");
             final Connection connection = db.getConnection()) {

            final MapReturnsBuilder<String> readyToGo = OJ.query("tables_by_schema.sql")
                    .map(rs -> rs.getString("TABLE_SCHEMA") + "." + rs.getString("TABLE_NAME") + " " + rs.getLong("id"));

            //good old list
            {
                final List<String> results = readyToGo.toList()
                        .bindString(":table_schema", "INFORMATION_SCHEMA")
                        .execute(connection);
                System.out.println(results);
            }

            //use streams
            try (Stream<String> stream = readyToGo.stream()
                    .bindString(":table_schema", "INFORMATION_SCHEMA")
                    .execute(connection)) {

                stream.filter(s->s.length() > 5).forEach(System.out::println);
            }
        }
    }
}
