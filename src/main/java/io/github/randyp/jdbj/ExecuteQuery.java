package io.github.randyp.jdbj;


import io.github.randyp.jdbj.lambda.ConnectionSupplier;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Runs jdbc query, returns result(s). Example:
 * <pre>
 * {@code
 * List<Student> students = JDBJ.query("SELECT * FROM student").map(Student::from).toList().execute(db);
 * }     
 * </pre>
 * Is {@link Immutable}, so you will need to (re)assign to a variable after every binding.
 * <p>
 * Encapsulates execution of {@link PreparedStatement#executeQuery()} for read only result sets, while adding JDBJ features.
 * @param <R> return type
 * @see JDBJ#query(String)
 * @see ReturnsQuery
 * @see MapQuery
 */
@Immutable
@ThreadSafe
public final class ExecuteQuery<R> extends PositionalBindingsBuilder<ExecuteQuery<R>> {

    private final ResultSetToResult<R> toResult;

    public ExecuteQuery(NamedParameterStatement statement, PositionalBindings bindings, ResultSetToResult<R> toResult) {
        super(statement, bindings, (s, b) -> new ExecuteQuery<>(s, b, toResult));
        Objects.requireNonNull(toResult, "toResult must not be null");
        this.toResult = toResult;
    }

    public R execute(DataSource db) throws SQLException {
        return execute(db::getConnection);
    }
    
    public R execute(ConnectionSupplier db) throws SQLException {
        checkAllBindingsPresent(); //might as well, not need to open connection
        try(Connection connection = db.getConnection()){
            return execute(connection);
        }
    }

    public R execute(Connection connection) throws SQLException {
        checkAllBindingsPresent();

        try (PreparedStatement ps = connection.prepareStatement(
                buildSql(),
                ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY)) {
            bindToStatement(ps);
            try (SmartResultSet rs = new SmartResultSet(ps.executeQuery())) {
                return toResult.from(rs);
            }
        }
    }

}
