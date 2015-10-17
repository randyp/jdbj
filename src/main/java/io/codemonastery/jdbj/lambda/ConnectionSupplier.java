package io.codemonastery.jdbj.lambda;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Alternative to {@link javax.sql.DataSource} within the JDBJ api.
 * <p>
 * Does not require that a new connection is opened, but is expected that after calling {@link ConnectionSupplier#getConnection()} the caller will eventually call {@link Connection#close()}
 * <p>
 * Example:
 * <pre>
 * {@code
 * ConnectionSupplier db = //something that will open a connection, perhaps even a method reference to {@link DataSource#getConnection()}
 * List<Student> students = JDBJ.query("SELECT * FROM student").map(Student::from).toList().execute(db);
 * }
 * </pre>
 */
public interface ConnectionSupplier {
    
    Connection getConnection() throws SQLException;
    
}
