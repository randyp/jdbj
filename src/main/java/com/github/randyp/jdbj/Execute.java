package com.github.randyp.jdbj;

import javax.annotation.concurrent.Immutable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Phase 2 Builder
 */
@Immutable
public final class Execute extends PositionalBindingsBuilder<Execute> {

    Execute(NamedParameterStatement statement) {
        this(statement, PositionalBindings.empty());
    }

    Execute(NamedParameterStatement statement, PositionalBindings bindings) {
        super(statement, bindings, Execute::new);
    }

    public boolean execute(Connection connection) throws SQLException {
        checkAllBindingsPresent();

        try (PreparedStatement ps = connection.prepareStatement(buildSql())) {
            bindToStatement(ps);
            return ps.execute();
        }
    }
}
