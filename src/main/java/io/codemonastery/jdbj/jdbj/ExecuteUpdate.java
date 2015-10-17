package io.codemonastery.jdbj.jdbj;

import io.codemonastery.jdbj.jdbj.lambda.ConnectionSupplier;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Perform a single sql update statement. 
 * <pre>
 * {@code
 * * <pre>
 * {@code
 * ExecuteUpdate updateGPA = JDBJ.update("UPDATE student set gpa = :gpa WHERE id = :id");
 * updateGPA.bindBigDecimal(":gpa", BigDecimal.ZERO)
 *     .bindLong(":id", 1L)
 *     .execute(db);
 * }     
 * </pre>
 * }    
 * </pre>
 * Is {@link Immutable}, so you will need to (re)assign to a variable after every binding.
 * <p>
 * Encapsulates the execution of {@link PreparedStatement#executeUpdate()} while adding JDBJ features.
 * @see JDBJ#update(String)
 * @see ExecuteInsert
 */
@Immutable
@ThreadSafe
public final class ExecuteUpdate extends PositionalBindingsBuilder<ExecuteUpdate> {

    ExecuteUpdate(NamedParameterStatement statement) {
        this(statement, new PositionalBindings());
    }

    ExecuteUpdate(NamedParameterStatement statement, PositionalBindings bindings) {
        super(statement, bindings, ExecuteUpdate::new);
    }

    public BatchedExecuteUpdate asBatch(){
        return new BatchedExecuteUpdate(statement);
    }

    public int execute(DataSource db) throws SQLException {
        return execute(db::getConnection);
    }

    private int execute(ConnectionSupplier db) throws SQLException {
        checkAllBindingsPresent();
        try(Connection connection = db.getConnection()){
            return execute(connection);
        }
    }

    public int execute(Connection connection) throws SQLException {
        checkAllBindingsPresent();

        try (PreparedStatement ps = connection.prepareStatement(buildSql())) {
            bindToStatement(ps);
            return ps.executeUpdate();
        }
    }
}
