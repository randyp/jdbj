[![Build Status](https://travis-ci.org/randyp/jdbj.svg?branch=master)](https://travis-ci.org/randyp/jdbj) [![Coverage Status](https://coveralls.io/repos/randyp/jdbj/badge.svg?branch=master&service=github)](https://coveralls.io/github/randyp/jdbj?branch=master)

jdbj is a jdbc fluent interface for capturing query intent long before query execution

#### Why?
Other jdbc convenience libraries follow the "create statement, bind parameters, execute query, map results" pattern that we inherited from older procedural code. For many web applications we've found a better pattern: "specify query, specify results mapper, bind parameters, execute query". We get to reuse steps 1-2 across most requests and only do the minimum amount of query building for each request.

#### Example Main

``` java
package oof.jdbc;

import org.h2.jdbcx.JdbcConnectionPool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Stream;

public class PrototypeMain {

    private static final MapQuery<Table> QueryReadyToGo = JDBJ
            .queryString("SELECT ID, TABLE_SCHEMA, TABLE_NAME from information_schema.tables " +
                    "WHERE TABLE_SCHEMA = :table_schema")
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
            try (Stream<Table> stream = QueryReadyToGo.stream()
                    .bindString(":table_schema", "INFORMATION_SCHEMA")
                    .execute(connection)) {

                stream.filter(table -> table.id < 0)
                        .forEach(System.out::println);
            }
        }finally {
            db.dispose();
        }
    }

    public static class Table {

        public static Table from(ResultSet rs) throws SQLException {
            return new Table(rs.getLong("id"), rs.getString("table_schema"), rs.getString("table_name"));
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
```

#### Example DAO (connection as construction parameter)

``` java
package oof.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Stream;

public class StudentDAO {

    private static final MapQuery<Student> studentsByID = JDBJ
            .queryString("SELECT id, first_name, last_name, gpa FROM students WHERE id in :ids")
            .map(Student::from);

    private final Connection connection;


    public StudentDAO(Connection connection) {
        this.connection = connection;
    }

    public Student byId(long id) throws SQLException {
        return studentsByID.first().bindLongs(":ids", id).execute(connection);
    }

    public List<Student> byIds(long[] ids) throws SQLException {
        return studentsByID.toList().bindLongs(":ids", ids).execute(connection);
    }

    public Stream<Student> streamByIds(long[] ids) throws SQLException {
        return studentsByID.stream().bindLongs(":ids", ids).execute(connection); //close me when done!!!
    }
}
```


#### Guiding Principles
* no *connection handles* - just use the Connection as an argument to execute methods immutable objects
* where possible, bubble up the SQLException (not possible in Stream.tryAdvance)
* use immutable builders to capture the query intent long before query execution
* hide the PreparedStatement as much as possible during the binding phase so that we can...
* use named parameters only

#### Credits
To the [jdbi team](http://jdbi.org/) for authoring jdbi. Much of jdbj is based on jdbi.
