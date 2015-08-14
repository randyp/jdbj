package com.github.randyp.jdbj.lambda;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionCallable <R> {

    R call(Connection connection) throws SQLException;

}
