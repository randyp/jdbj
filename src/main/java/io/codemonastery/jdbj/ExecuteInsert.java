package io.codemonastery.jdbj;

import io.codemonastery.jdbj.lambda.ConnectionSupplier;
import io.codemonastery.jdbj.lambda.ResultMapper;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Perform a single sql insert statement, such as inserting a single row to the database. Example:
 * <pre>
 * {@code 
 * ExecuteInsert<Long> insert = JDBJ.insert("INSERT INTO student(first_name, last_name, gpa)" +
 *     " VALUES (:first_name, :last_name, :gpa)", rs -> rs.getLong(1));
 * NewStudent newStudent = new NewStudent("Ada", "Lovelace", new BigDecimal("4.00"));
 * final Optional<Long> generatedKey = insert
 *     .bind(newStudent::bindings())
 *     .execute(db).stream().findFirst();
 * }     
 * </pre>
 * Is {@link Immutable}, so you will need to (re)assign to a variable after every binding.
 * <p>     
 * Encapsulates the execution of {@link PreparedStatement#executeUpdate()} with {@link Statement#RETURN_GENERATED_KEYS} while adding most of the JDBJ features.
 * @param <K> key type returned by keyMapper
 * @see JDBJ#insert(String, ResultMapper)
 * @see ExecuteUpdate
 */
@Immutable
@ThreadSafe
public final class ExecuteInsert<K> extends PositionalBindingsBuilder<ExecuteInsert<K>> {

    private final ResultMapper<K> keysMapper;

    ExecuteInsert(NamedParameterStatement statement, ResultMapper<K> keysMapper) {
        this(statement, new PositionalBindings(), keysMapper);

    }

    ExecuteInsert(NamedParameterStatement statement, PositionalBindings bindings, ResultMapper<K> keysMapper) {
        super(statement, bindings, (s, b)-> new ExecuteInsert<>(s, b, keysMapper));
        Objects.requireNonNull(keysMapper, "keysMapper must not be null");
        this.keysMapper = keysMapper;
    }

    public BatchedExecuteInsert<K> asBatch(){
        return new BatchedExecuteInsert<>(statement, keysMapper);
    }

    public List<K> execute(DataSource db) throws SQLException {
        return execute(db::getConnection);
    }

    public List<K> execute(ConnectionSupplier db) throws SQLException {
        checkAllBindingsPresent();
        try(Connection connection = db.getConnection()){
            return execute(connection);
        }
    }

    public List<K> execute(Connection connection) throws SQLException {
        checkAllBindingsPresent();

        final List<K> keys = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(buildSql(), Statement.RETURN_GENERATED_KEYS)) {
            bindToStatement(ps);
            ps.executeUpdate();

            try(SmartResultSet generatedKeys = new SmartResultSet(ps.getGeneratedKeys())){
                while(generatedKeys.next()){
                    keys.add(keysMapper.map(generatedKeys));
                }
            }
        }
        return keys;
    }

    
}
