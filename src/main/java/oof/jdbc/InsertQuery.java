package oof.jdbc;

import javax.annotation.concurrent.Immutable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Phase 2 Builder
 */
@Immutable
public final class InsertQuery extends DecoratesBindingBuilder<InsertQuery> {

    InsertQuery(NamedParameterStatement statement) {
        this(new BindingsBuilder(statement));
    }

    InsertQuery(BindingsBuilder bindingsBuilder) {
        super(bindingsBuilder);
    }

    @Override
    InsertQuery prototype(BindingsBuilder newBindings) {
        return new InsertQuery(newBindings);
    }

    public int execute(Connection connection) throws SQLException {
        checkAllBindingsPresent();

        try(PreparedStatement ps = connection.prepareStatement(bindingsBuilder.buildSql())){
            bindingsBuilder.bindToStatement(ps);
            return ps.executeUpdate();
        }
    }
}
