package com.github.randyp.jdbj;

import com.github.randyp.jdbj.lambda.IOSupplier;
import com.github.randyp.jdbj.lambda.ResultMapper;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * Build a query string from various resources, turn them into query objects with JDBJ features.
 * <p>
 * Is {@link Immutable}, so you will need to (re)assign to a variable after every binding.
 * @see JDBJ
 */
@Immutable
@ThreadSafe
public class JDBJBuilder {
    
    static JDBJBuilder fromReader(IOSupplier<Reader> supplier) {
        final StringBuilder builder = new StringBuilder();
        appendReaderToBuilder(supplier, builder);
        return new JDBJBuilder(builder);
    }

    static JDBJBuilder fromStream(IOSupplier<InputStream> supplier) {
        final StringBuilder builder = new StringBuilder();
        appendStreamToBuilder(supplier, builder);
        return new JDBJBuilder(builder);
    }

    static JDBJBuilder fromPath(Path path) {
        final StringBuilder builder = new StringBuilder();
        appendPathToBuilder(path, builder);
        return new JDBJBuilder(builder);
    }

    static JDBJBuilder fromResource(String resourceName){
        final StringBuilder builder = new StringBuilder();
        appendResourceToBuilder(resourceName, builder);
        return new JDBJBuilder(builder);
    }

    static JDBJBuilder fromString(String queryString){
        return new JDBJBuilder(new StringBuilder(queryString));
    }

    private final StringBuilder stringBuilder;

    private JDBJBuilder(StringBuilder stringBuilder) {
        this.stringBuilder = stringBuilder;
    }

    public JDBJBuilder string(String string){
        return new JDBJBuilder(clone(stringBuilder).append(string));
    }

    public JDBJBuilder reader(IOSupplier<Reader> supplier){
        final StringBuilder newBuilder = clone(stringBuilder);
        appendReaderToBuilder(supplier, newBuilder);
        return new JDBJBuilder(newBuilder);
    }

    public JDBJBuilder stream(IOSupplier<InputStream> supplier){
        final StringBuilder newBuilder = clone(stringBuilder);
        appendStreamToBuilder(supplier, newBuilder);
        return new JDBJBuilder(newBuilder);
    }

    public JDBJBuilder path(Path path){
        final StringBuilder newBuilder = clone(stringBuilder);
        appendPathToBuilder(path, newBuilder);
        return new JDBJBuilder(newBuilder);
    }

    public JDBJBuilder resource(String resourceName){
        final StringBuilder newBuilder = clone(stringBuilder);
        appendResourceToBuilder(resourceName, newBuilder);
        return new JDBJBuilder(newBuilder);
    }

    public ReturnsQuery query() {
        return new ReturnsQuery(NamedParameterStatement.make(stringBuilder.toString()));
    }

    public ExecuteUpdate update() {
        return new ExecuteUpdate(NamedParameterStatement.make(stringBuilder.toString()));
    }

    public <R> ExecuteInsert<R> insert(ResultMapper<R> keyMapper) {
        return new ExecuteInsert<>(NamedParameterStatement.make(stringBuilder.toString()), keyMapper);
    }

    public ExecuteStatement statement() {
        return new ExecuteStatement(NamedParameterStatement.make(stringBuilder.toString()));
    }

    public ExecuteScript script() {
        return ExecuteScript.from(stringBuilder.toString());
    }

    private static StringBuilder clone(StringBuilder builder) {
        try {
            byte[] payload;
            try (ByteArrayOutputStream bOut = new ByteArrayOutputStream()) {
                ObjectOutputStream out = new ObjectOutputStream(bOut);
                out.writeObject(builder);
                payload = bOut.toByteArray();
            }

            try (ByteArrayInputStream bIn = new ByteArrayInputStream(payload)) {
                try (ObjectInputStream in = new ObjectInputStream(bIn)) {
                    return (StringBuilder) in.readObject();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Did not expect serializing java.lang.StringBuilder to fail", e);
        }
    }

    private static void appendReaderToBuilder(IOSupplier<Reader> supplier, StringBuilder queryString) {
        try (BufferedReader br = new BufferedReader(supplier.get())) {
            String line;
            while ((line = br.readLine()) != null) {
                queryString.append(line).append('\n');
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    private static void appendStreamToBuilder(IOSupplier<InputStream> supplier, StringBuilder builder) {
        appendReaderToBuilder(() -> new InputStreamReader(supplier.get()), builder);
    }

    private static void appendPathToBuilder(Path path, StringBuilder builder) {
        appendStreamToBuilder(() -> Files.newInputStream(path, StandardOpenOption.READ), builder);
    }

    private static void appendResourceToBuilder(String resourceName, StringBuilder builder) {
        final URL url = JDBJ.class.getClassLoader().getResource(resourceName);
        if (url == null) {
            throw new IllegalArgumentException("resource not found: " + resourceName);
        }
        appendStreamToBuilder(url::openStream, builder);
    }
}
