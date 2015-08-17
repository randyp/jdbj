package com.github.randyp.jdbj;


import com.github.randyp.jdbj.lambda.ResultSetToResult;

import javax.annotation.concurrent.Immutable;
import java.sql.*;

@Immutable
public final class ExecuteQuery<R> extends PositionalBindingsBuilder<ExecuteQuery<R>> {

    private final ResultSetToResult<R> toResult;

    ExecuteQuery(NamedParameterStatement statement, ResultSetToResult<R> toResult) {
        this(statement, PositionalBindings.empty(), toResult);
    }

    public ExecuteQuery(NamedParameterStatement statement, PositionalBindings bindings, ResultSetToResult<R> toResult) {
        super(statement, bindings, (s, b) -> new ExecuteQuery<>(s, b, toResult));
        this.toResult = toResult;
    }

    public R execute(Connection connection) throws SQLException {
        checkAllBindingsPresent();

        try (PreparedStatement ps = connection.prepareStatement(
                buildSql(),
                ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY
        )) {
            bindToStatement(ps);
            try (SmartResultSet rs = new SmartResultSet(ps.executeQuery())) {
                return toResult.from(rs);
            }
        }
    }

}
