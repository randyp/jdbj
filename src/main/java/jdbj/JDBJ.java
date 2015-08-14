package jdbj;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

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

