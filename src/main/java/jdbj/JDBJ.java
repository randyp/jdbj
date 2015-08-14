package jdbj;

import jdbj.lambda.ConnectionRunnable;
import jdbj.lambda.ResultSetMapper;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public final class JDBJ {

    /**
     * @param resource
     * @return a phase 2 builder
     */
    public static ReturnsQuery query(String resource) {
        final URL url = JDBJ.class.getClassLoader().getResource(resource);
        if (url == null) {
            throw new IllegalArgumentException("resource not found: " + resource);
        }
        return queryString(readQueryResource(url));
    }

    /**
     * @param queryString
     * @return a phase 2 builder
     */
    public static ReturnsQuery queryString(String queryString) {
        final NamedParameterStatement statement = NamedParameterStatement.make(queryString);
        return new ReturnsQuery(statement);
    }

    /**
     * @param queryString
     * @return a phase 2 builder
     */
    public static InsertQuery insertQueryString(String queryString) {
        final NamedParameterStatement statement = NamedParameterStatement.make(queryString);
        return new InsertQuery(statement);
    }

    /**
     * @param queryString
     * @return a phase 2 builder
     */
    public static <R> InsertReturnKeysQuery<R> insertQueryStringGetKeys(String queryString, ResultSetMapper<R> keyMapper) {
        final NamedParameterStatement statement = NamedParameterStatement.make(queryString);
        return new InsertReturnKeysQuery<>(statement, keyMapper);
    }

    public static void transaction(DataSource dataSource, ConnectionRunnable runnable) throws SQLException {
        int transactionIsolation = Connection.TRANSACTION_READ_COMMITTED;
        transaction(dataSource, transactionIsolation, runnable);
    }

    public static void transaction(DataSource dataSource, int transactionIsolation, ConnectionRunnable runnable) throws SQLException {
        Connection connection = null;
        Integer oldTransactionIsolation = null;
        try {
            connection = dataSource.getConnection();

            if(!connection.getAutoCommit()){
                throw new IllegalStateException("autocommit is already turned off, which means rollback point is not start of transaction");
            }
            oldTransactionIsolation = connection.getTransactionIsolation();

            connection.setTransactionIsolation(transactionIsolation);
            connection.setAutoCommit(false);

            runnable.run(connection);
            connection.commit();
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

    private static String readQueryResource(URL url) {
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

