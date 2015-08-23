package com.github.randyp.jdbj;

import com.github.randyp.jdbj.lambda.ConnectionSupplier;
import com.github.randyp.jdbj.lambda.ResultMapper;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Insert multiple rows into the database using jdbc batch functionality. Does not allow binding of collections, since generated sql must be same for all batches. Example:
 * <pre>
 * {@code
 * ExecuteInsert<Long> insert = JDBJ.insert("INSERT INTO student(first_name, last_name, gpa)" +
 * " VALUES (:first_name, :last_name, :gpa)", rs -> rs.getLong(1));
 *
 * List<NewStudent> newStudents = Arrays.asList(
 * new NewStudent("Ada", "Lovelace", new BigDecimal("4.00")),
 * new NewStudent("Haskell", "Curry", new BigDecimal("4.00"))
 * );
 * BatchedExecuteInsert<Long> batchInsert = insert.asBatch();
 * for (NewStudent newStudent : newStudents) {
 *     batchInsert.startBatch()
 *     .bindValues(newStudent::bindings())
 *     .addBatch();
 * }
 * List<Long> generatedKeys = batchInsert.execute(db);
 * }
 * </pre>
 * Encapsulates the execution of {@link PreparedStatement#executeBatch()} with {@link Statement#RETURN_GENERATED_KEYS} while adding most of the JDBJ features.
 * <p>
 * Worth noting: {@link BatchedExecuteInsert} is Mutable, but individual batches {@link com.github.randyp.jdbj.BatchedExecute.Batch} are {@link Immutable}.
 * <p>
 * Also worth noting: returning keys during batch execution is generally not supported.
 * Often times only the keys from the last batch will be returned (Oracle, H2).
 * However, this class is safe to use with current versions of postgres, mysql, and hsql.
 * @param <K> type of the returned keys
 * @see ExecuteInsert#asBatch() 
 * @see BatchedExecuteUpdate
 * @see ResultMapper
 */
@NotThreadSafe
public class BatchedExecuteInsert<K> extends BatchedExecute<BatchedExecuteInsert<K>> {

    private final ResultMapper<K> keysMapper;

    BatchedExecuteInsert(NamedParameterStatement statement, ResultMapper<K> keysMapper) {
        super(statement);
        this.keysMapper = keysMapper;
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

    @Override
    BatchedExecuteInsert<K> chainThis() {
        return this;
    }
}
