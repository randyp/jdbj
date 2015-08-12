package oof.jdbc.lambda;

import oof.jdbc.PreparedColumn;

import java.sql.SQLException;

public interface Binding {

    void bind(PreparedColumn preparedColumn) throws SQLException;

}
