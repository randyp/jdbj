package jdbj.binding;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface PositionalBinding  {


    /**
     * @param ps
     * @param parameterIndex to start at
     * @return parameterIndex for next Positional Binding
     */
    int bind(PreparedStatement ps, int parameterIndex) throws SQLException;

    void appendPositionalParametersToQueryString(StringBuilder builder);
}
