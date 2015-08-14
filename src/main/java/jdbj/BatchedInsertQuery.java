package jdbj;

import jdbj.lambda.Binding;

import javax.annotation.concurrent.Immutable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Worth noting: BatchedInsertQuery is Mutable
 */
public class BatchedInsertQuery {

    private final List<ValueBindings> batches = new ArrayList<>();
    private final NamedParameterStatement statement;


    BatchedInsertQuery(NamedParameterStatement statement) {
        this.statement = statement;

    }

    public Batch startBatch(){
        return new Batch();
    }

    public int[] execute(Connection connection) throws SQLException {
        if(batches.isEmpty()){
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

        public BatchedInsertQuery endBatch(){
            statement.checkAllBindingsPresent(batch);
            batches.add(batch);
            batch = null;
            return BatchedInsertQuery.this;
        }
    }
}
