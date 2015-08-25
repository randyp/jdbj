package com.github.randyp.jdbj;

import com.github.randyp.jdbj.lambda.ConnectionCallable;
import com.github.randyp.jdbj.lambda.ConnectionSupplier;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Not for external use.
 * <p>
 * Primary responsibility is to execute callable against connection, close resources, and properly group the exceptions.     
 * @param <R>
 */
@Immutable
@ThreadSafe
abstract class AbstractTransaction<R> {

    final Integer isolation;

    public AbstractTransaction(Integer isolation) {
        if (isolation != null) {
            switch (isolation) {
                case Connection.TRANSACTION_NONE:
                case Connection.TRANSACTION_READ_UNCOMMITTED:
                case Connection.TRANSACTION_READ_COMMITTED:
                case Connection.TRANSACTION_REPEATABLE_READ:
                case Connection.TRANSACTION_SERIALIZABLE:
                    break;
                default:
                    throw new IllegalArgumentException(isolation + " is not a valid transaction isolation constant");
            }
        }
        this.isolation = isolation;
    }

    R executeInternal(ConnectionSupplier db) throws SQLException {
        Objects.requireNonNull(db, "db must not be null");

        Connection connection = null;
        R toReturn = null;
        SQLException caught = null;
        try {
            connection = db.getConnection();
            toReturn = executeInternal(connection);
        }catch (SQLException e){
            caught = e;
        }finally {
            if(connection != null){
                try{
                    connection.close();
                }catch (SQLException e){
                    if(caught == null){
                        caught = e;
                    }else{
                        caught.setNextException(e);
                    }
                }
            }
        }
        if(caught != null) {
            throw caught;
        }
        return toReturn;
    }

    R executeInternal(Connection connection) throws SQLException {
        Objects.requireNonNull(connection, "connection must not be null");

        R toReturn = null;
        Integer oldTransactionIsolation = null;
        SQLException caught = null;
        try {
            if (!connection.getAutoCommit()) {
                throw new SQLException("autocommit is already turned off, which means rollback point is not start of transaction");
            }
            if (isolation != null) {
                oldTransactionIsolation = connection.getTransactionIsolation();
                connection.setTransactionIsolation(isolation);
            }

            connection.setAutoCommit(false);

            toReturn = callable().call(connection);
            connection.commit();
        } catch (SQLException e) {
            caught = e;
            try {
                connection.rollback();
            } catch (SQLException re) {
                caught.setNextException(re);
            }
            
        } catch(Exception e){
            caught = new SQLException("Uncaught exception in transaction", e);
        }finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                if(caught != null){
                    caught.setNextException(e);
                }else{
                    caught = e;
                }
            }
            if (oldTransactionIsolation != null) {
                try {
                    connection.setTransactionIsolation(oldTransactionIsolation);
                } catch (SQLException e) {
                    if(caught != null){
                        caught.setNextException(e);
                    }else{
                        caught = e;
                    }
                }
            }
        }
        if(caught != null) {
            throw caught;
        }
        return toReturn;
    }

    abstract ConnectionCallable<R> callable();

}
