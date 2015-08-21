package com.github.randyp.jdbj;


import com.github.randyp.jdbj.lambda.ConnectionSupplier;
import com.github.randyp.jdbj.lambda.ResultSetRunnable;

import javax.annotation.concurrent.Immutable;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Phase 3 Builder
 */
@Immutable
public final class ExecuteQueryRunnable extends PositionalBindingsBuilder<ExecuteQueryRunnable> {

    private final ResultSetRunnable runnable;

    ExecuteQueryRunnable(NamedParameterStatement statement, PositionalBindings bindings, ResultSetRunnable runnable) {
        super(statement, bindings, ((s, b) -> new ExecuteQueryRunnable(s, b, runnable)));
        this.runnable = runnable;
    }

    public void execute(DataSource db) throws SQLException {
        execute(db::getConnection);
    }

    public void execute(ConnectionSupplier db) throws SQLException {
        checkAllBindingsPresent();
        try(Connection connection = db.getConnection()){
            execute(connection);
        }
    }

    public void execute(Connection connection) throws SQLException {
        checkAllBindingsPresent();

        try (PreparedStatement ps = connection.prepareStatement(
                buildSql(),
                ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY)) {
            bindToStatement(ps);
            try (ResultSet rs = ps.executeQuery()) {
                runnable.run(rs);
            }
        }
    }
}
