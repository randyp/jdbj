package com.github.randyp.jdbj.binding;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface PositionalBinding  {

    int bind(PreparedStatement ps, int parameterIndex) throws SQLException;

    void appendPositionalParametersToQueryString(StringBuilder builder);
}
