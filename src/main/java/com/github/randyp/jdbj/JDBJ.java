package com.github.randyp.jdbj;

import com.github.randyp.jdbj.lambda.*;

import javax.annotation.Nullable;
import javax.sql.DataSource;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Entry point for building and executing queries, transactions with JDBJ features.
 * <p>
 * If you can, prefer {@link JDBJ#resource(String)} over {@link JDBJ#string(String)}.
 * <p>
 * However, since {@link JDBJ#string(String)} is commonly used in tests, convenience methods are provided to build queries with a single string: {@link JDBJ#query(String)}, {@link JDBJ#update(String)}, {@link JDBJ#insert(String, ResultMapper)}, {@link JDBJ#statement(String)}, {@link JDBJ#script(String)}.
 */
public final class JDBJ {

    /**
     * Opens a connection from dataSource, starts a transaction, and calls runnable, closes connection. If an exception is thrown in runnable and not caught the transaction will be rolled back and exception re-thrown. Otherwise the transaction will be committed.
     * <p>
     * The runnable should not call {@link Connection#close()}, {@link Connection#setAutoCommit(boolean)}, {@link Connection#setTransactionIsolation(int)}.
     * <p>
     * If you need a different isolation than the default you can use {@link JDBJ#transaction(DataSource, int, ConnectionRunnable)}.
     *
     * @param db       db
     * @param runnable runnable
     * @throws SQLException re-thrown from runnable
     */
    public static void transaction(DataSource db, ConnectionRunnable runnable) throws SQLException {
        transaction(db::getConnection, runnable);
    }

    /**
     * Opens a connection from dataSource, starts a transaction, and calls runnable, closes connection. If an exception is thrown in runnable and not caught the transaction will be rolled back and exception re-thrown. Otherwise the transaction will be committed.
     * <p>
     * The runnable should not call {@link Connection#close()}, {@link Connection#setAutoCommit(boolean)}, {@link Connection#setTransactionIsolation(int)}.
     * <p>
     * If you need a different isolation than the default you can use {@link JDBJ#transaction(DataSource, int, ConnectionRunnable)}.
     *
     * @param db       db
     * @param runnable runnable
     * @throws SQLException re-thrown from runnable
     */
    public static void transaction(ConnectionSupplier db, ConnectionRunnable runnable) throws SQLException {
        returningTransactionOptionalIsolation(db, null, c -> {
            runnable.run(c);
            return null;
        });
    }

    /**
     * Opens a connection from dataSource, starts a transaction with the provided isolation, and calls runnable, closes connection. If an exception is thrown in runnable and not caught the transaction will be rolled back and exception re-thrown. Otherwise the transaction will be committed.
     * <p>
     * The runnable should not call {@link Connection#close()}, {@link Connection#setAutoCommit(boolean)}, {@link Connection#setTransactionIsolation(int)}.
     *
     * @param db       db
     * @param transactionIsolation transactionIsolation
     * @param runnable runnable
     * @throws SQLException re-thrown from runnable
     */
    public static void transaction(DataSource db, int transactionIsolation, ConnectionRunnable runnable) throws SQLException {
        transaction(db::getConnection, transactionIsolation, runnable);
    }

    /**
     * Opens a connection from dataSource, starts a transaction with the provided isolation, and calls runnable, closes connection. If an exception is thrown in runnable and not caught the transaction will be rolled back and exception re-thrown. Otherwise the transaction will be committed.
     * <p>
     * The runnable should not call {@link Connection#close()}, {@link Connection#setAutoCommit(boolean)}, {@link Connection#setTransactionIsolation(int)}.
     *
     * @param db       db
     * @param transactionIsolation transactionIsolation
     * @param runnable runnable
     * @throws SQLException re-thrown from runnable
     */
    public static void transaction(ConnectionSupplier db, int transactionIsolation, ConnectionRunnable runnable) throws SQLException {
        returningTransactionOptionalIsolation(db, transactionIsolation, c -> {
            runnable.run(c);
            return null;
        });
    }

    /**
     * Opens a connection from dataSource, starts a transaction, and calls runnable, closes connection, returns results. If an exception is thrown in callable and not caught the transaction will be rolled back and exception re-thrown. Otherwise the transaction will be committed.
     * <p>
     * The callable should not call {@link Connection#close()}, {@link Connection#setAutoCommit(boolean)}, {@link Connection#setTransactionIsolation(int)}.
     * <p>
     * If you need a different isolation than the default you can use {@link JDBJ#returningTransaction(DataSource, int, ConnectionCallable)}.
     *
     * @param db       db
     * @param callable callable
     * @param <R> return type
     * @return what callable returned
     * @throws SQLException re-thrown from callable
     */
    public static <R> R returningTransaction(DataSource db, ConnectionCallable<R> callable) throws SQLException {
        return returningTransaction(db::getConnection, callable);
    }

    /**
     * Opens a connection from dataSource, starts a transaction, and calls runnable, closes connection, returns results. If an exception is thrown in callable and not caught the transaction will be rolled back and exception re-thrown. Otherwise the transaction will be committed.
     * <p>
     * The callable should not call {@link Connection#close()}, {@link Connection#setAutoCommit(boolean)}, {@link Connection#setTransactionIsolation(int)}.
     * <p>
     * If you need a different isolation than the default you can use {@link JDBJ#returningTransaction(DataSource, int, ConnectionCallable)}.
     *
     * @param db       db
     * @param callable callable
     * @param <R> return type
     * @return what callable returned
     * @throws SQLException re-thrown from callable
     */
    public static <R> R returningTransaction(ConnectionSupplier db, ConnectionCallable<R> callable) throws SQLException {
        return returningTransactionOptionalIsolation(db, null, callable);
    }

    /**
     * Opens a connection from dataSource, starts a transaction with provided isolation, and calls runnable, closes connection, returns results. If an exception is thrown in callable and not caught the transaction will be rolled back and exception re-thrown. Otherwise the transaction will be committed.
     * <p>
     * The callable should not call {@link Connection#close()}, {@link Connection#setAutoCommit(boolean)}, {@link Connection#setTransactionIsolation(int)}.
     * <p>
     * If you need a different isolation than the default you can use {@link JDBJ#returningTransaction(DataSource, int, ConnectionCallable)}.
     *
     * @param db       db
     * @param transactionIsolation transactionIsolation
     * @param callable callable
     * @param <R> return type
     * @return what callable returned
     * @throws SQLException re-thrown from callable
     */
    public static <R> R returningTransaction(DataSource db, int transactionIsolation, ConnectionCallable<R> callable) throws SQLException {
        return returningTransaction(db::getConnection, transactionIsolation, callable);
    }

    /**
     * Opens a connection from dataSource, starts a transaction with provided isolation, and calls runnable, closes connection, returns results. If an exception is thrown in callable and not caught the transaction will be rolled back and exception re-thrown. Otherwise the transaction will be committed.
     * <p>
     * The callable should not call {@link Connection#close()}, {@link Connection#setAutoCommit(boolean)}, {@link Connection#setTransactionIsolation(int)}.
     * <p>
     * If you need a different isolation than the default you can use {@link JDBJ#returningTransaction(DataSource, int, ConnectionCallable)}.
     *
     * @param db       db
     * @param transactionIsolation transactionIsolation                   
     * @param callable callable
     * @param <R> return type
     * @return what callable returned
     * @throws SQLException re-thrown from callable
     */
    public static <R> R returningTransaction(ConnectionSupplier db, int transactionIsolation, ConnectionCallable<R> callable) throws SQLException {
        return returningTransactionOptionalIsolation(db::getConnection, transactionIsolation, callable);
    }

    /**
     * See {@link QueryStringBuilder} for documentation.
     *
     * @param string string
     * @return builder
     */
    public static QueryStringBuilder string(String string) {
        return QueryStringBuilder.fromString(string);
    }

    /**
     * See {@link QueryStringBuilder} for documentation.
     *
     * @param supplier supplier
     * @return builder
     */
    public static QueryStringBuilder reader(IOSupplier<Reader> supplier) {
        return QueryStringBuilder.fromReader(supplier);
    }

    /**
     * See {@link QueryStringBuilder} for documentation.
     *
     * @param supplier supplier
     * @return builder
     */
    public static QueryStringBuilder stream(IOSupplier<InputStream> supplier) {
        return QueryStringBuilder.fromStream(supplier);
    }

    /**
     * See {@link QueryStringBuilder} for documentation.
     *
     * @param path path
     * @return builder
     */
    public static QueryStringBuilder path(Path path) {
        return QueryStringBuilder.fromPath(path);
    }

    /**
     * See {@link QueryStringBuilder} for documentation.
     *
     * @param resourceName resourceName
     * @return builder
     */
    public static QueryStringBuilder resource(String resourceName) {
        return resource(JDBJ.class, resourceName);
    }

    /**
     * See {@link QueryStringBuilder} for documentation.
     *
     * @param klass        will use klass's classloader to load resource
     * @param resourceName resourceName
     * @return builder
     */
    public static QueryStringBuilder resource(Class klass, String resourceName) {
        return QueryStringBuilder.fromResource(klass, resourceName);
    }

    /**
     * See {@link ReturnsQuery} for documentation.
     *
     * @param query query
     * @return instance of {@link ReturnsQuery}
     */
    public static ReturnsQuery query(String query) {
        return string(query).query();
    }

    /**
     * See {@link ExecuteUpdate} for documentation.
     *
     * @param query query
     * @return instance of {@link ExecuteUpdate}
     */
    public static ExecuteUpdate update(String query) {
        return string(query).update();
    }

    /**
     * See {@link ExecuteInsert} for documentation.
     *
     * @param query     query
     * @param keyMapper keyMapper
     * @param <R> return type
     * @return instance of {@link ExecuteInsert}
     */
    public static <R> ExecuteInsert<R> insert(String query, ResultMapper<R> keyMapper) {
        return string(query).insert(keyMapper);
    }

    /**
     * See {@link ExecuteStatement} for documentation.
     *
     * @param query query
     * @return instance of {@link ExecuteStatement}
     */
    public static ExecuteStatement statement(String query) {
        return string(query).statement();
    }

    /**
     * See {@link ExecuteScript} for documentation.
     *
     * @param script script
     * @return instance of {@link ExecuteStatement}
     */
    public static ExecuteScript script(String script) {
        return string(script).script();
    }

    JDBJ() {
    }

    private static <R> R returningTransactionOptionalIsolation(ConnectionSupplier db, @Nullable Integer transactionIsolation, ConnectionCallable<R> callable) throws SQLException {

        Connection connection = null;
        Integer oldTransactionIsolation = null;
        try {
            connection = db.getConnection();

            if (!connection.getAutoCommit()) {
                throw new IllegalStateException("autocommit is already turned off, which means rollback point is not start of transaction");
            }
            if (transactionIsolation != null) {
                oldTransactionIsolation = connection.getTransactionIsolation();
                connection.setTransactionIsolation(transactionIsolation);
            }

            connection.setAutoCommit(false);

            R result = callable.call(connection);
            connection.commit();
            return result;
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException re) {
                //ignore
            }
            throw e;
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException e) {
                    //ignore
                }
                if (oldTransactionIsolation != null) {
                    try {
                        connection.setTransactionIsolation(oldTransactionIsolation);
                    } catch (SQLException e) {
                        //ignore
                    }
                }
                try {
                    connection.close();
                } catch (SQLException e) {
                    //ignore
                }
            }
        }
    }
}

