package oof.jdbc.lambda;

import javax.annotation.Nullable;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetMapper<R> {

    @Nullable R map(ResultSet rs) throws SQLException;

}
