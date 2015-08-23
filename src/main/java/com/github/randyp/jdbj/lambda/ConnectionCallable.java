package com.github.randyp.jdbj.lambda;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Similar to {@link java.util.concurrent.Callable}, except receives {@link Connection} as a parameter and possibly throws {@link SQLException}.
 * <p>
 * It is expected that the instance of {@link ConnectionCallable} will not call {@link Connection#close()}.
 * <p>
 * When being used as part of a {@link com.github.randyp.jdbj.JDBJ#returningTransaction(DataSource, ConnectionCallable)} it is additionally expected not to tamper with {@link Connection#setAutoCommit(boolean)}, {@link Connection#commit()}, {@link Connection#rollback()}, and sometimes {@link Connection#setTransactionIsolation(int)}.
 * <p>
 * Example:
 * <pre>
 * {@code
 * List<Students> students = JDBJ.returningTransaction(db, connection->{
 *     List<Long> ids = insertStudents.execute(connection);
 *     return studentsById.bindLongs(":ids", ids).execute(connection);
 * });
 * }
 * </pre>
 * @param <R> return type
 * @see ConnectionRunnable
 * @see com.github.randyp.jdbj.JDBJ#returningTransaction(DataSource, ConnectionCallable) 
 */
public interface ConnectionCallable <R> {

    R call(Connection connection) throws SQLException;

}
