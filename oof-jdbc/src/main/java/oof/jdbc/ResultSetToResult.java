package oof.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetToResult<R>{

    R from(ResultSet rs) throws SQLException;
}
