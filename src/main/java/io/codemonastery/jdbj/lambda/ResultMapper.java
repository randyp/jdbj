package io.codemonastery.jdbj.lambda;

import io.codemonastery.jdbj.QueryStringBuilder;
import io.codemonastery.jdbj.ReturnsQuery;
import io.codemonastery.jdbj.SmartResult;

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
