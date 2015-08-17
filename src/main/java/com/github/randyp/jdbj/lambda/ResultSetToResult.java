package com.github.randyp.jdbj.lambda;

import com.github.randyp.jdbj.SmartResultSet;

import javax.annotation.Nullable;
import java.sql.SQLException;

public interface ResultSetToResult<R>{

    @Nullable R from(SmartResultSet rs) throws SQLException;
}
