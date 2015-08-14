package jdbj;

import javax.annotation.concurrent.Immutable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Phase 2 Builder
 */
@Immutable
public final class InsertQuery extends PositionalBindingsBuilder<InsertQuery> {

    InsertQuery(NamedParameterStatement statement) {
        this(statement, PositionalBindings.empty());
    }

    InsertQuery(NamedParameterStatement statement, PositionalBindings bindings) {
        super(statement, bindings, (InsertQuery::new));
    }

    public int execute(Connection connection) throws SQLException {
        checkAllBindingsPresent();

        try (PreparedStatement ps = connection.prepareStatement(buildSql())) {
            bindToStatement(ps);
            return ps.executeUpdate();
        }
    }
}
