package com.github.randyp.jdbj;

import com.github.randyp.jdbj.lambda.ResultSetMapper;

import javax.annotation.concurrent.Immutable;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Phase 2 Builder
 */
@Immutable
public final class ExecuteInsert<R> extends PositionalBindingsBuilder<ExecuteInsert<R>> {

    private final ResultSetMapper<R> keysMapper;

    ExecuteInsert(NamedParameterStatement statement, ResultSetMapper<R> keysMapper) {
        this(statement, PositionalBindings.empty(), keysMapper);

    }

    ExecuteInsert(NamedParameterStatement statement, PositionalBindings bindings, ResultSetMapper<R> keysMapper) {
        super(statement, bindings, (s, b)-> new ExecuteInsert<>(s, b, keysMapper));
        this.keysMapper = keysMapper;
    }

    @SuppressWarnings("deprecation")
    public BatchedExecuteInsert<R> asBatch(){
        return new BatchedExecuteInsert<>(statement, keysMapper);
    }

    public List<R> execute(DataSource db) throws SQLException {
        checkAllBindingsPresent();
        try(Connection connection = db.getConnection()){
            return execute(connection);
        }
    }

    public List<R> execute(Connection connection) throws SQLException {
        checkAllBindingsPresent();

        final List<R> keys = new ArrayList<>();
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
