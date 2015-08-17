package com.github.randyp.jdbj.lambda;

import com.github.randyp.jdbj.SmartResultSet;

import javax.annotation.Nullable;
import java.sql.SQLException;

public interface ResultSetMapper<R> {

    @Nullable R map(SmartResultSet rs) throws SQLException;

}
