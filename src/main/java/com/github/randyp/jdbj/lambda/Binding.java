package com.github.randyp.jdbj.lambda;

import com.github.randyp.jdbj.PreparedColumn;

import java.sql.SQLException;

public interface Binding {

    void bind(PreparedColumn preparedColumn) throws SQLException;

}
