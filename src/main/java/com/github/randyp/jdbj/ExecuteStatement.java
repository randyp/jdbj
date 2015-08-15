package com.github.randyp.jdbj;

import javax.annotation.concurrent.Immutable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Phase 2 Builder
 */
@Immutable
public final class ExecuteStatement extends PositionalBindingsBuilder<ExecuteStatement> {

    ExecuteStatement(NamedParameterStatement statement) {
        this(statement, PositionalBindings.empty());
    }

    ExecuteStatement(NamedParameterStatement statement, PositionalBindings bindings) {
        super(statement, bindings, ExecuteStatement::new);
    }

    public boolean execute(Connection connection) throws SQLException {
        checkAllBindingsPresent();

        try (PreparedStatement ps = connection.prepareStatement(buildSql())) {
            bindToStatement(ps);
            return ps.execute();
        }
    }
}
