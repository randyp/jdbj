package com.github.randyp.jdbj;

import com.github.randyp.jdbj.lambda.Binding;
import com.github.randyp.jdbj.lambda.ConnectionSupplier;
import com.github.randyp.jdbj.lambda.ResultMapper;

import javax.annotation.concurrent.NotThreadSafe;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Updates multiple rows in the database using jdbc batch functionality.
 * <p>
 * Encapsulates the execution of {@link PreparedStatement#executeBatch()} while adding most of the JDBJ features.
 * <p>
 * Worth noting: {@link BatchedExecuteUpdate} is Mutable, unlike most other query builders. 
 * <p>
 * @see ExecuteUpdate#asBatch() 
 * @see BatchedExecuteInsert
 */
@NotThreadSafe
public class BatchedExecuteUpdate {

    private final List<ValueBindings> batches = new ArrayList<>();
    private final NamedParameterStatement statement;


    BatchedExecuteUpdate(NamedParameterStatement statement) {
        this.statement = statement;

    }

    public Batch startBatch() {
        return new Batch();
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

    public class Batch implements ValueBindingsBuilder<Batch> {

        private ValueBindings batch;

        Batch() {
            this(PositionalBindings.empty());
        }

        Batch(ValueBindings batch) {
            this.batch = batch;
        }

        @Override
        public Batch bind(String name, Binding binding) {
            checkBatchNotEnded();
            return new Batch(batch.valueBinding(name, binding));
        }

        public BatchedExecuteUpdate endBatch() {
            statement.checkAllBindingsPresent(batch);
            batches.add(batch);
            batch = null;
            return BatchedExecuteUpdate.this;
        }

        private void checkBatchNotEnded() {
            if (batch == null) {
                throw new IllegalStateException("batch already ended, use BatchedInsertQuery#startBatch to create a new batch");
            }
        }
    }
}
