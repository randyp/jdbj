package com.github.randyp.jdbj;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Not intended for external use.
 * <p>
 * {@link PositionalBinding} and its implementations {@link ValueBinding} and {@link ListBinding} know how to to 
 * generate sql fragments to replace named parameters in the sql statements, and know how to to bind to a 
 * {@link PreparedStatement} at a specific parameter index.
 */
interface PositionalBinding  {

    /**
     * @param ps the prepared statement
     * @param parameterIndex index of prepared statement to bind to
     * @return new parameter index
     * @throws SQLException
     */
    int bind(PreparedStatement ps, int parameterIndex) throws SQLException;

    /**
     * Either appends a {@code '?'} representing a single binding or appends {@code "(?,?,...,?)"} for a collection binding 
     * @param builder
     */
    void appendPositionalParametersToQueryString(StringBuilder builder);
}
