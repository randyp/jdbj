package com.github.randyp.jdbj;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SmartResultSet extends SmartResult implements AutoCloseable {

    private final ResultSet rs;

    public SmartResultSet(ResultSet rs) {
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
