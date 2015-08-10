package oof.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetForConnection {

    ResultSet executeQuery(Connection connection) throws SQLException;
}
