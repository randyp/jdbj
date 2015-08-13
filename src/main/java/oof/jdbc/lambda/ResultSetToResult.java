package oof.jdbc.lambda;

import javax.annotation.Nullable;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetToResult<R>{

    @Nullable R from(ResultSet rs) throws SQLException;
}
