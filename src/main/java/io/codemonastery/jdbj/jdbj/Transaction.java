package io.codemonastery.jdbj.jdbj;

import io.codemonastery.jdbj.jdbj.lambda.ConnectionRunnable;
import io.codemonastery.jdbj.jdbj.lambda.ConnectionCallable;
import io.codemonastery.jdbj.jdbj.lambda.ConnectionSupplier;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

/**
 * {@link Immutable} transaction builder. Only build operation is isolation. Example:
 * <pre>
 * {@code 
 * JDBJ.transaction(connection->System.out.println(connection.getSchema()))
 *     .execute(db);
 * }
 * </pre>
 * <p>
 * Exceptions are grouped using the {@link SQLException#setNextException(SQLException)}, so one can get all exceptions. Example:
 * <pre>
 * {@code
 * try{
 *     JDBJ.transaction(connection->System.out.println(connection.getSchema()))
 *         .execute(db);
 * }catch(SQLException e){
 *     while(e != null){
 *         e.printStackTrace();
 *         e = e.getNextException();
 *     }
 * }         
 * }
 * </pre>
 */
@Immutable
@ThreadSafe
public class Transaction extends AbstractTransaction<Void> {

    private final ConnectionRunnable runnable;

    Transaction(ConnectionRunnable runnable) {
        this(runnable, null);
    }
    
    Transaction(ConnectionRunnable runnable, Integer isolation) {
        super(isolation);
        Objects.requireNonNull(runnable, "runnable must not be null");
        this.runnable = runnable;
    }
    
    public Transaction isolation(int isolation){
        return new Transaction(runnable, isolation);
    }
    
    public void execute(DataSource db) throws SQLException {
        Objects.requireNonNull(db, "db must not be null");
        execute(db::getConnection);
    }
    
    public void execute(ConnectionSupplier db) throws SQLException {
        super.executeInternal(db);
    }

    public void execute(Connection connection) throws SQLException {
        super.executeInternal(connection);
    }

    @Override
    ConnectionCallable<Void> callable() {
        return connection -> {
            runnable.run(connection);
            return null;
        };
    }
}
