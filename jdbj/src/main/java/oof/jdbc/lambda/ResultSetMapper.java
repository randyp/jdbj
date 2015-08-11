package oof.jdbc.lambda;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetMapper<R> {

    R map(ResultSet rs) throws SQLException;

}
