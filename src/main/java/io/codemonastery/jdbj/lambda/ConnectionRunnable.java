package io.codemonastery.jdbj.lambda;

import io.codemonastery.jdbj.JDBJ;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Similar to {@link Runnable}, except receives {@link Connection} as a parameter and possibly throws {@link SQLException}.
 * <p>
 * It is expected that the instance of {@link ConnectionRunnable} will not call {@link Connection#close()}.
 * <p>
 * When being used as part of a {@link JDBJ#transaction(ConnectionRunnable)} it is additionally expected not to tamper with {@link Connection#setAutoCommit(boolean)}, {@link Connection#commit()}, {@link Connection#rollback()}, and sometimes {@link Connection#setTransactionIsolation(int)}.
 * <p>
 * Example:
 * <pre>
 * {@code
 * JDBJ.transaction(db, connection->{
 *     insertStudents.execute(connection);
 *     insertStudentSchedules.execute(connection);
 * });
 * }
 * </pre>
 * @see ConnectionCallable
 * @see JDBJ#transaction(ConnectionRunnable)
 */
public interface ConnectionRunnable {

    void run(Connection connection) throws SQLException;

}
