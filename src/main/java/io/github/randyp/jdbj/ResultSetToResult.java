package io.github.randyp.jdbj;

import javax.annotation.Nullable;
import java.sql.SQLException;

/**
 * Not intended for external use, at least yet.
 * <p>
 * Used to create a single {@link R} from an entire {@link SmartResultSet}.
 * <p>
 * Should not call {@link SmartResultSet#close()}.
 * @param <R> return type
 */
interface ResultSetToResult<R>{

    @Nullable R from(SmartResultSet rs) throws SQLException;
}
