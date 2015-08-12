package oof.jdbc;


import oof.jdbc.lambda.ResultSetToResult;

import javax.annotation.concurrent.Immutable;
import java.sql.*;

/**
 * Phase 3 Builder
 * @param <R>
 */
@Immutable
public final class ExecuteQuery<R> extends DecoratesBindingBuilder<ExecuteQuery<R>> {

    private final ResultSetToResult<R> toResult;

    ExecuteQuery(NamedParameterStatement statement, ResultSetToResult<R> toResult) {
        this(new BindingsBuilder(statement), toResult);
    }

    ExecuteQuery(BindingsBuilder bindingsBuilder, ResultSetToResult<R> toResult) {
        super(bindingsBuilder);
        this.toResult = toResult;
    }

    /**
     * phase 4 method
     * @param connection
     * @return
     * @throws SQLException
     */
    public R execute(Connection connection) throws SQLException {
        try(PreparedStatement ps = connection.prepareStatement(bindingsBuilder.buildSql())){
            bindingsBuilder.bindToStatement(ps);
            try(ResultSet rs = ps.executeQuery()){
                return toResult.from(rs);
            }
        }
    }

    @Override
    ExecuteQuery<R> prototype(BindingsBuilder newBindings) {
        return new ExecuteQuery<>(newBindings, toResult);
    }
}
