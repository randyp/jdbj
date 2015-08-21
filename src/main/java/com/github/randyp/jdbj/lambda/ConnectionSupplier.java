package com.github.randyp.jdbj.lambda;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionSupplier {
    
    Connection getConnection() throws SQLException;
    
}
