package oof.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

public interface ResultSetToVoid {

    void run(ResultSet rs) throws SQLException;
}
