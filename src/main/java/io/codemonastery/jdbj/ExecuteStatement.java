package io.codemonastery.jdbj;

import io.codemonastery.jdbj.lambda.ConnectionSupplier;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Execute a single sql statement. Example:
 * <pre>
 * {@code
 * JDBJ.statement("CREATE TABLE student(id SERIAL, first_name VARCHAR, last_name VARCHAR, gpa VARCHAR)")
 *     .execute(db);
 * }     
 * </pre>
 * Is {@link Immutable}, so you will need to (re)assign to a variable after every binding.
 * <p>
 * Encapsulates the execution of {@link PreparedStatement#execute()} while adding JDBJ features.
 * @see JDBJ#statement(String)
 * @see ExecuteScript
 */
@Immutable
@ThreadSafe
public final class ExecuteStatement extends PositionalBindingsBuilder<ExecuteStatement> {

    ExecuteStatement(NamedParameterStatement statement) {
        this(statement, new PositionalBindings());
    }

    ExecuteStatement(NamedParameterStatement statement, PositionalBindings bindings) {
        super(statement, bindings, ExecuteStatement::new);
    }

    public boolean execute(DataSource db) throws SQLException {
        return execute(db::getConnection);
    }

    public boolean execute(ConnectionSupplier db) throws SQLException {
        checkAllBindingsPresent();
        try(Connection connection = db.getConnection()){
            return execute(connection);
        }
    }

    public boolean execute(Connection connection) throws SQLException {
        checkAllBindingsPresent();

        try (PreparedStatement ps = connection.prepareStatement(buildSql())) {
            bindToStatement(ps);
            return ps.execute();
        }
    }
}
