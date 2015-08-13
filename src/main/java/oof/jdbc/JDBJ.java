package oof.jdbc;

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

        try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {

            final StringBuilder queryString = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                queryString.append(line).append('\n');
            }
            return queryString(queryString.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param queryString
     * @return a phase 2 builder
     */
    public static ReturnsQuery queryString(String queryString) {
        final NamedParameterStatement statement = NamedParameterStatement.make(queryString);
        return new ReturnsQuery(statement);
    }

    JDBJ() {

    }
}

