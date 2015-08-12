jdbj is a jdbc fluent interface for capturing query intent long before query execution

#### Why?
Other jdbc convenience libraries follow the "create statement, bind parameters, execute query, map results" pattern that we inherited from older procedural code. For many web applications we've found a better pattern: "specify query, specify results mapper, bind parameters, execute query". We get to reuse steps 1-2 across all requests and only do the minimum amount of query building for each request.

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
* hide the PreparedStatement as much as possible during the binding phase
