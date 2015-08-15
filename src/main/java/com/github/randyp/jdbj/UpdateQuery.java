package com.github.randyp.jdbj;

import javax.annotation.concurrent.Immutable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Phase 2 Builder
 */
@Immutable
public final class UpdateQuery extends PositionalBindingsBuilder<UpdateQuery> {

    UpdateQuery(NamedParameterStatement statement) {
        this(statement, PositionalBindings.empty());
    }

    UpdateQuery(NamedParameterStatement statement, PositionalBindings bindings) {
        super(statement, bindings, (UpdateQuery::new));
    }

    public BatchedUpdateQuery asBatch(){
        return new BatchedUpdateQuery(statement);
    }

    public int execute(Connection connection) throws SQLException {
        checkAllBindingsPresent();

        try (PreparedStatement ps = connection.prepareStatement(buildSql())) {
            bindToStatement(ps);
            return ps.executeUpdate();
        }
    }
}
