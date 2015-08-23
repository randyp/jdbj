package com.github.randyp.jdbj.lambda;

import com.github.randyp.jdbj.PositionalBindings;

import java.sql.SQLException;

public interface HasBindings {
    PositionalBindings bindings() throws SQLException;
}
