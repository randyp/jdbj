package oof.jdbc;


import oof.jdbc.lambda.ResultSetRunnable;

import javax.annotation.concurrent.Immutable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Phase 3 Builder
 */
@Immutable
public final class ExecuteQueryNoResult extends DecoratesBindingBuilder<ExecuteQueryNoResult> {

    private final ResultSetRunnable runnable;

    ExecuteQueryNoResult(NamedParameterStatement statement, ResultSetRunnable runnable) {
        this(new BindingsBuilder(statement), runnable);
    }

    ExecuteQueryNoResult(BindingsBuilder bindingsBuilder, ResultSetRunnable runnable) {
        super(bindingsBuilder);
        this.runnable = runnable;
    }

    /**
     * phase 4 method
     * @param connection
     * @return
     * @throws SQLException
     */
    public void execute(Connection connection) throws SQLException {
        try(PreparedStatement ps = connection.prepareStatement(bindingsBuilder.buildSql())){
            bindingsBuilder.bindToStatement(ps);
            try(ResultSet rs = ps.executeQuery()){
                runnable.run(rs);
            }
        }
    }

    @Override
    ExecuteQueryNoResult prototype(BindingsBuilder newBindings) {
        return new ExecuteQueryNoResult(newBindings, runnable);
    }
}
