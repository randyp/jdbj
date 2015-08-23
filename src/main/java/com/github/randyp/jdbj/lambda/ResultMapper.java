package com.github.randyp.jdbj.lambda;

import com.github.randyp.jdbj.SmartResult;

import javax.annotation.Nullable;
import java.sql.SQLException;

/**
 * Encapsulates how to map a {@link SmartResult} to an instance of {@link R}.
 * @param <R> return type
 * @see com.github.randyp.jdbj.ReturnsQuery#map(ResultMapper)
 * @see com.github.randyp.jdbj.JDBJBuilder#insert(ResultMapper)
 */
public interface ResultMapper<R> {

    @Nullable R map(SmartResult result) throws SQLException;

}
