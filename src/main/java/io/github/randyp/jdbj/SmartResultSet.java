package io.github.randyp.jdbj;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * A table of data representing a database result set, which
 * is usually generated by executing a statement that queries the database.
 * Must be closed {@link SmartResultSet#close()}.
 * <p>
 * Wraps {@link java.sql.ResultSet} to accomplish this behaviour.
 * @see SmartResult
 */
public class SmartResultSet extends SmartResult implements AutoCloseable {

    private final ResultSet rs;

    public SmartResultSet(ResultSet rs) {
        Objects.requireNonNull(rs, "rs must not be null");
        this.rs = rs;
    }

    public int findColumn(String columnLabel) throws SQLException {
        return rs.findColumn(columnLabel);
    }

    public boolean next() throws SQLException {
        return rs.next();
    }

    @Override
    public void close() throws SQLException {
        rs.close();
    }

    @Override
    ResultSet rs() {
        return rs;
    }
}
