package oof.jdbc;

import java.sql.SQLException;

public interface Binding {

    void bind(PreparedColumn preparedColumn) throws SQLException;

}
