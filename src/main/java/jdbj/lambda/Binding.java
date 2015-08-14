package jdbj.lambda;

import jdbj.PreparedColumn;

import java.sql.SQLException;

public interface Binding {

    void bind(PreparedColumn preparedColumn) throws SQLException;

}
