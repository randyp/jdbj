package io.github.randyp.jdbj.lambda;

import io.github.randyp.jdbj.QueryStringBuilder;
import io.github.randyp.jdbj.ReturnsQuery;
import io.github.randyp.jdbj.SmartResult;

import javax.annotation.Nullable;
import java.sql.SQLException;

/**
 * Maps a {@link SmartResult} to an instance of {@link R}.
 * @param <R> return type
 * @see ReturnsQuery#map(ResultMapper)
 * @see QueryStringBuilder#insert(ResultMapper)
 */
public interface ResultMapper<R> {

    @Nullable R map(SmartResult result) throws SQLException;

}
