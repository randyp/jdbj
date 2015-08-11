package oof.jdbc.lambda;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetRunnable {

    void run(ResultSet rs) throws SQLException;

}
