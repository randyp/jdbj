package com.github.randyp.jdbj;

import javax.annotation.concurrent.Immutable;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Phase 2 Builder
 */
@Immutable
public final class ExecuteUpdate extends PositionalBindingsBuilder<ExecuteUpdate> {

    ExecuteUpdate(NamedParameterStatement statement) {
        this(statement, PositionalBindings.empty());
    }

    ExecuteUpdate(NamedParameterStatement statement, PositionalBindings bindings) {
        super(statement, bindings, ExecuteUpdate::new);
    }

    public BatchedExecuteUpdate asBatch(){
        return new BatchedExecuteUpdate(statement);
    }

    public int execute(DataSource db) throws SQLException {
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
