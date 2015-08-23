package com.github.randyp.jdbj;

import javax.annotation.Nullable;
import java.sql.SQLException;

interface ResultSetToResult<R>{

    @Nullable R from(SmartResultSet rs) throws SQLException;
}
