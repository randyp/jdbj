package com.github.randyp.jdbj;

import java.sql.PreparedStatement;
import java.sql.SQLException;

interface PositionalBinding  {

    int bind(PreparedStatement ps, int parameterIndex) throws SQLException;

    void appendPositionalParametersToQueryString(StringBuilder builder);
}
