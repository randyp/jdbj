package jdbj;

import jdbj.lambda.Binding;
import jdbj.lambda.ResultSetMapper;

import javax.annotation.concurrent.Immutable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Worth noting: BatchedInsertReturnKeysQuery is Mutable
 *
 * Also worth noting: returning keys during batch execution is generally not supported.
 * Usually (Oracle, H2) only the keys from the last batch will be returned.
 */
@Deprecated
public class BatchedInsertReturnKeysQuery<R> {

    private final List<ValueBindings> batches = new ArrayList<>();
    private final NamedParameterStatement statement;
    private final ResultSetMapper<R> keysMapper;


    BatchedInsertReturnKeysQuery(NamedParameterStatement statement, ResultSetMapper<R> keysMapper) {
        this.statement = statement;
        this.keysMapper = keysMapper;
    }

    public Batch startBatch(){
        return new Batch();
    }

    public List<R> execute(Connection connection) throws SQLException {
        if(batches.isEmpty()){
            throw new IllegalStateException("no batches to insert");
        }

        final String sql = statement.jdbcSql(batches.get(0));

        final List<R> keys = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (ValueBindings batch : batches) {
                statement.bind(ps, batch);
                ps.addBatch();
            }
            ps.executeBatch();
            try(ResultSet generatedKeys = ps.getGeneratedKeys()){
                while(generatedKeys.next()){
                    keys.add(keysMapper.map(generatedKeys));
                }
            }
        }
        return keys;
    }

    @Immutable
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
            if(batch == null){
                throw new IllegalStateException("batch already ended, use BatchedInsertQuery#startBatch to create a new batch");
            }
            return new Batch(batch.addValueBinding(name, binding));
        }

        public BatchedInsertReturnKeysQuery<R> endBatch(){
            statement.checkAllBindingsPresent(batch);
            batches.add(batch);
            batch = null;
            return BatchedInsertReturnKeysQuery.this;
        }
    }
}
