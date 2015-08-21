package com.github.randyp.jdbj;


import com.github.randyp.jdbj.lambda.ConnectionSupplier;
import com.github.randyp.jdbj.lambda.ResultSetToResult;

import javax.annotation.concurrent.Immutable;
import javax.sql.DataSource;
import java.sql.*;

@Immutable
public final class ExecuteQuery<R> extends PositionalBindingsBuilder<ExecuteQuery<R>> {

    private final ResultSetToResult<R> toResult;

    public ExecuteQuery(NamedParameterStatement statement, PositionalBindings bindings, ResultSetToResult<R> toResult) {
        super(statement, bindings, (s, b) -> new ExecuteQuery<>(s, b, toResult));
        this.toResult = toResult;
    }

    public R execute(DataSource db) throws SQLException {
        return execute(db::getConnection);
    }
    
    public R execute(ConnectionSupplier db) throws SQLException {
        checkAllBindingsPresent(); //might as well, not need to open connection
        try(Connection connection = db.getConnection()){
            return execute(connection);
        }
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
