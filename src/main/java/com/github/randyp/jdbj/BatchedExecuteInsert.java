package com.github.randyp.jdbj;

import com.github.randyp.jdbj.lambda.Binding;
import com.github.randyp.jdbj.lambda.ConnectionSupplier;
import com.github.randyp.jdbj.lambda.ResultMapper;

import javax.annotation.concurrent.NotThreadSafe;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates {@link PreparedStatement#executeBatch()} with {@link Statement#RETURN_GENERATED_KEYS} while adding most of the JDBJ features.
 * <p>
 * Worth noting: {@link BatchedExecuteInsert} is Mutable, unlike most other query builders. 
 * <p>
 * Also worth noting: returning keys during batch execution is generally not supported.
 * Often times only the keys from the last batch will be returned (Oracle, H2).
 * However, this class is safe to use with current versions of postgres, mysql, and hsql.
 * @param <K> type of the returned keys
 * @see BatchedExecuteUpdate if you do not need generated keys
 * @see ResultMapper
 */
@SuppressWarnings("deprecation")
@NotThreadSafe
@Deprecated //no plans to remove class, just wanted you to read the above documentation about lack of support
public class BatchedExecuteInsert<K> {

    private final List<ValueBindings> batches = new ArrayList<>();
    private final NamedParameterStatement statement;
    private final ResultMapper<K> keysMapper;

    BatchedExecuteInsert(NamedParameterStatement statement, ResultMapper<K> keysMapper) {
        this.statement = statement;
        this.keysMapper = keysMapper;
    }

    public Batch startBatch(){
        return new Batch();
    }

    public List<K> execute(DataSource db) throws SQLException {
        return execute(db::getConnection);
    }

    public List<K> execute(ConnectionSupplier db) throws SQLException {
        checkNotEmpty();
        try(Connection connection = db.getConnection()){
            return execute(connection);
        }
    }

    public List<K> execute(Connection connection) throws SQLException {
        checkNotEmpty();

        final String sql = statement.jdbcSql(batches.get(0));

        final List<K> keys = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (ValueBindings batch : batches) {
                statement.bind(ps, batch);
                ps.addBatch();
            }
            ps.executeBatch();
            try(SmartResultSet generatedKeys = new SmartResultSet(ps.getGeneratedKeys())){
                while(generatedKeys.next()){
                    keys.add(keysMapper.map(generatedKeys));
                }
            }
        }
        return keys;
    }

    private void checkNotEmpty() {
        if(batches.isEmpty()){
            throw new IllegalStateException("no batches to insert");
        }
    }

    public class Batch implements ValueBindingsBuilder<Batch> {

        private ValueBindings batch;

        Batch(){
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

        public BatchedExecuteInsert<K> endBatch(){
            statement.checkAllBindingsPresent(batch);
            batches.add(batch);
            batch = null;
            return BatchedExecuteInsert.this;
        }

        private void checkBatchNotEnded() {
            if(batch == null){
                throw new IllegalStateException("batch already ended, use BatchedInsertQuery#startBatch to create a new batch");
            }
        }
    }
}
