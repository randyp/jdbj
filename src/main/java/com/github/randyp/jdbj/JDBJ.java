package com.github.randyp.jdbj;

import com.github.randyp.jdbj.lambda.ConnectionCallable;
import com.github.randyp.jdbj.lambda.ConnectionRunnable;
import com.github.randyp.jdbj.lambda.ResultSetMapper;

import javax.annotation.Nullable;
import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;

public final class JDBJ {

    public static ReturnsQuery query(String queryResource) {
        return queryString(readResource(queryResource));
    }

    public static ReturnsQuery queryString(String queryString) {
        final NamedParameterStatement statement = NamedParameterStatement.make(queryString);
        return new ReturnsQuery(statement);
    }

    public static ExecuteUpdate insertQuery(String queryResource) {
        return updateQueryString(readResource(queryResource));
    }

    public static ExecuteUpdate updateQueryString(String queryString) {
        final NamedParameterStatement statement = NamedParameterStatement.make(queryString);
        return new ExecuteUpdate(statement);
    }

    public static <R> InsertQuery<R> insertQueryGetKeys(String queryResource, ResultSetMapper<R> keyMapper) {
        return insertQueryStringGetKeys(readResource(queryResource), keyMapper);
    }

    public static <R> InsertQuery<R> insertQueryStringGetKeys(String queryString, ResultSetMapper<R> keyMapper) {
        final NamedParameterStatement statement = NamedParameterStatement.make(queryString);
        return new InsertQuery<>(statement, keyMapper);
    }

    public static Execute executeString(String queryString) {
        final NamedParameterStatement statement = NamedParameterStatement.make(queryString);
        return new Execute(statement);
    }

    public static ExecuteScript script(String scriptResource) {
        return scriptString(readResource(scriptResource));
    }

    public static ExecuteScript scriptString(String scriptString) {
        return ExecuteScript.from(scriptString);
    }

    public static Execute execute(String queryResource) {
        return executeString(readResource(queryResource));
    }

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

    private static <R> R returningTransactionOptionalIsolation(DataSource dataSource, @Nullable Integer transactionIsolation, ConnectionCallable<R> callable) throws SQLException{

        Connection connection = null;
        Integer oldTransactionIsolation = null;
        try {
            connection = dataSource.getConnection();

            if(!connection.getAutoCommit()){
                throw new IllegalStateException("autocommit is already turned off, which means rollback point is not start of transaction");
            }
            if(transactionIsolation != null){
                oldTransactionIsolation = connection.getTransactionIsolation();
                connection.setTransactionIsolation(transactionIsolation);
            }

            connection.setAutoCommit(false);

            R result = callable.call(connection);
            connection.commit();
            return result;
        } catch (SQLException e ) {
            try {
                if(connection != null){
                    connection.rollback();
                }
            } catch (SQLException re) {
                //ignore
            }
            throw e;
        } finally {
            if(connection != null){
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException e) {
                    //ignore
                }
                if(oldTransactionIsolation != null){
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

    JDBJ() {

    }

    private static String readResource(String queryResource) {
        final URL url = JDBJ.class.getClassLoader().getResource(queryResource);
        if (url == null) {
            throw new IllegalArgumentException("resource not found: " + queryResource);
        }
        final StringBuilder queryString = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {


            String line;
            while ((line = br.readLine()) != null) {
                queryString.append(line).append('\n');
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return queryString.toString();
    }
}

