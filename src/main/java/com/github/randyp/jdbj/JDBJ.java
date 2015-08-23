package com.github.randyp.jdbj;

import com.github.randyp.jdbj.lambda.ConnectionCallable;
import com.github.randyp.jdbj.lambda.ConnectionRunnable;
import com.github.randyp.jdbj.lambda.IOSupplier;
import com.github.randyp.jdbj.lambda.ResultMapper;

import javax.annotation.Nullable;
import javax.sql.DataSource;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;

public final class JDBJ {

    public static void transaction(DataSource dataSource, ConnectionRunnable runnable) throws SQLException {
        returningTransactionOptionalIsolation(dataSource, null, c -> {
            runnable.run(c);
            return null;
        });
    }

    public static void transaction(DataSource dataSource, int transactionIsolation, ConnectionRunnable runnable) throws SQLException {
        returningTransactionOptionalIsolation(dataSource, transactionIsolation, c -> {
            runnable.run(c);
            return null;
        });
    }

    public static <R> R returningTransaction(DataSource dataSource, ConnectionCallable<R> callable) throws SQLException {
        return returningTransactionOptionalIsolation(dataSource, null, callable);
    }

    public static <R> R returningTransaction(DataSource dataSource, int transactionIsolation, ConnectionCallable<R> callable) throws SQLException {
        return returningTransactionOptionalIsolation(dataSource, transactionIsolation, callable);
    }

    private static <R> R returningTransactionOptionalIsolation(DataSource dataSource, @Nullable Integer transactionIsolation, ConnectionCallable<R> callable) throws SQLException {

        Connection connection = null;
        Integer oldTransactionIsolation = null;
        try {
            connection = dataSource.getConnection();

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

    public static JDBJBuilder string(String string) {
        return JDBJBuilder.fromString(string);
    }

    public static JDBJBuilder reader(IOSupplier<Reader> supplier) {
        return JDBJBuilder.fromReader(supplier);
    }

    public static JDBJBuilder stream(IOSupplier<InputStream> supplier) {
        return JDBJBuilder.fromStream(supplier);
    }

    public static JDBJBuilder path(Path path) {
        return JDBJBuilder.fromPath(path);
    }

    public static JDBJBuilder resource(String resourceName) {
        return JDBJBuilder.fromResource(resourceName);
    }

    public static ReturnsQuery query(String query) {
        return string(query).query();
    }

    public static ExecuteUpdate update(String query) {
        return string(query).update();
    }

    public static <R> ExecuteInsert<R> insert(String query, ResultMapper<R> keyMapper) {
        return string(query).insert(keyMapper);
    }

    public static ExecuteStatement statement(String query) {
        return string(query).statement();
    }

    public static ExecuteScript script(String script) {
        return string(script).script();
    }

    JDBJ() {
    }
}

