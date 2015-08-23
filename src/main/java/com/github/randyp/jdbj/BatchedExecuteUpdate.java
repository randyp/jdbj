package com.github.randyp.jdbj;

import com.github.randyp.jdbj.lambda.ConnectionSupplier;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Updates multiple rows in the database using jdbc batch functionality. Does not allow binding of collections, since generated sql must be same for all batches. Example:
 * <pre>
 * {@code
 * ExecuteUpdate updateGPA = JDBJ.update("UPDATE student set gpa = :gpa WHERE id = :id");
 * updateGPA.asBatch()
 *     .startBatch()
 *     .bindBigDecimal(":gpa", BigDecimal.ZERO)
 *     .bindLong(":id", 1L)
 *     .addBatch()
 *     .startBatch()
 *     .bindBigDecimal(":gpa", BigDecimal.ZERO)
 *     .bindLong(":id", 2L)
 *     .addBatch()
 *     .execute(db);
 * }     
 * </pre>
 * Encapsulates the execution of {@link PreparedStatement#executeBatch()} while adding most of the JDBJ features.
 * <p>
 * Worth noting: {@link BatchedExecuteUpdate} is Mutable, but individual batches {@link com.github.randyp.jdbj.BatchedExecute.Batch} are {@link Immutable}. 
 * <p>
 * @see ExecuteUpdate#asBatch() 
 * @see BatchedExecuteInsert
 */
@NotThreadSafe
public class BatchedExecuteUpdate extends BatchedExecute<BatchedExecuteUpdate> {

    BatchedExecuteUpdate(NamedParameterStatement statement) {
        super(statement);

    }

    public int[] execute(DataSource db) throws SQLException {
        return execute(db::getConnection);
    }

    public int[] execute(ConnectionSupplier db) throws SQLException {
        try(Connection connection = db.getConnection()){
            return execute(connection);
        }
    }

    public int[] execute(Connection connection) throws SQLException {
        if (batches.isEmpty()) {
            throw new IllegalStateException("no batches to insert");
        }

        final String sql = statement.jdbcSql(batches.get(0));
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (ValueBindings batch : batches) {
                statement.bind(ps, batch);
                ps.addBatch();
            }
            return ps.executeBatch();
        }
    }

    @Override
    BatchedExecuteUpdate chainThis() {
        return this;
    }
}
