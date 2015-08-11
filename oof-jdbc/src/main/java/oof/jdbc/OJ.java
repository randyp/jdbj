package oof.jdbc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class OJ {

    public static ReturnsBuilder query(String resource)  {
        final URL url = OJ.class.getClassLoader().getResource(resource);
        if(url == null){
            throw new IllegalArgumentException("resource not found: " + resource);
        }

        try {
            final StringBuilder queryString = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
                String line;
                while((line = br.readLine()) != null){
                    queryString.append(line).append('\n');
                }
            }

            return queryString(queryString.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ReturnsBuilder queryString(String queryString) {
        final NamedParameterStatement statement = NamedParameterStatement.make(queryString);
        return new ReturnsBuilder(statement);
    }

    private OJ() {
    }
}

