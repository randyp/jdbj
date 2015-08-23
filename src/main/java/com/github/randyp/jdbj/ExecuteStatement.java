package com.github.randyp.jdbj;

import com.github.randyp.jdbj.lambda.ConnectionSupplier;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Immutable
@ThreadSafe
public final class ExecuteStatement extends PositionalBindingsBuilder<ExecuteStatement> {

    ExecuteStatement(NamedParameterStatement statement) {
        this(statement, PositionalBindings.empty());
    }

    ExecuteStatement(NamedParameterStatement statement, PositionalBindings bindings) {
        super(statement, bindings, ExecuteStatement::new);
    }

    public boolean execute(DataSource db) throws SQLException {
        return execute(db::getConnection);
    }

    public boolean execute(ConnectionSupplier db) throws SQLException {
        checkAllBindingsPresent();
        try(Connection connection = db.getConnection()){
            return execute(connection);
        }
    }

    public boolean execute(Connection connection) throws SQLException {
        checkAllBindingsPresent();

        try (PreparedStatement ps = connection.prepareStatement(buildSql())) {
            bindToStatement(ps);
            return ps.execute();
        }
    }
}
