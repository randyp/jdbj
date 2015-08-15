package com.github.randyp.jdbj;

import com.github.randyp.jdbj.lambda.ResultSetMapper;

import javax.annotation.concurrent.Immutable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Phase 2 Builder
 */
@Immutable
public final class InsertQuery<R> extends PositionalBindingsBuilder<InsertQuery<R>> {

    private final ResultSetMapper<R> keysMapper;

    InsertQuery(NamedParameterStatement statement, ResultSetMapper<R> keysMapper) {
        this(statement, PositionalBindings.empty(), keysMapper);

    }

    InsertQuery(NamedParameterStatement statement, PositionalBindings bindings, ResultSetMapper<R> keysMapper) {
        super(statement, bindings, (s, b)-> new InsertQuery<>(s, b, keysMapper));
        this.keysMapper = keysMapper;
    }

    public BatchedInsertQuery<R> asBatch(){
        return new BatchedInsertQuery<>(statement, keysMapper);
    }

    public List<R> execute(Connection connection) throws SQLException {
        checkAllBindingsPresent();

        final List<R> keys = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(buildSql(), Statement.RETURN_GENERATED_KEYS)) {
            bindToStatement(ps);
            ps.executeUpdate();

            try(ResultSet generatedKeys = ps.getGeneratedKeys()){
                while(generatedKeys.next()){
                    keys.add(keysMapper.map(generatedKeys));
                }
            }
        }
        return keys;
    }
}