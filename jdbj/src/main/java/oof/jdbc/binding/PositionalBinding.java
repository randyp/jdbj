package oof.jdbc.binding;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface PositionalBinding  {


    /**
     * Returns the new parameter index you should use
     * @param ps
     * @param parameterIndex
     * @return
     */
    int bind(PreparedStatement ps, int parameterIndex) throws SQLException;

    void appendPositionalParametersToQueryString(StringBuilder builder);
}
