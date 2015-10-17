package io.codemonastery.jdbj;

import io.codemonastery.jdbj.lambda.IOSupplier;
import io.codemonastery.jdbj.lambda.ResultMapper;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

/**
 * Build a query string from various resources, turn them into query objects with JDBJ features.
 * <p>
 * Is {@link Immutable}, so you will need to (re)assign to a variable after every binding.
 * @see JDBJ
 */
@Immutable
@ThreadSafe
public class QueryStringBuilder {
    
    static QueryStringBuilder fromReader(IOSupplier<Reader> supplier) {
        final StringBuilder builder = new StringBuilder();
        appendReaderToBuilder(supplier, builder);
        return new QueryStringBuilder(builder);
    }

    static QueryStringBuilder fromStream(IOSupplier<InputStream> supplier) {
        final StringBuilder builder = new StringBuilder();
        appendStreamToBuilder(supplier, builder);
        return new QueryStringBuilder(builder);
    }

    static QueryStringBuilder fromPath(Path path) {
        final StringBuilder builder = new StringBuilder();
        appendPathToBuilder(path, builder);
        return new QueryStringBuilder(builder);
    }

    static QueryStringBuilder fromResource(Class klass, String resourceName){
        final StringBuilder builder = new StringBuilder();
        appendResourceToBuilder(klass, resourceName, builder);
        return new QueryStringBuilder(builder);
    }

    static QueryStringBuilder fromString(String queryString){
        Objects.requireNonNull(queryString, "queryString must not be null");
        return new QueryStringBuilder(new StringBuilder(queryString));
    }

    private final StringBuilder stringBuilder;

    private QueryStringBuilder(StringBuilder stringBuilder) {
        this.stringBuilder = stringBuilder;
    }

    public QueryStringBuilder string(String string){
        Objects.requireNonNull(string, "queryString must not be null");
        return new QueryStringBuilder(clone(stringBuilder).append(string));
    }

    public QueryStringBuilder reader(IOSupplier<Reader> supplier){
        final StringBuilder newBuilder = clone(stringBuilder);
        appendReaderToBuilder(supplier, newBuilder);
        return new QueryStringBuilder(newBuilder);
    }

    public QueryStringBuilder stream(IOSupplier<InputStream> supplier){
        final StringBuilder newBuilder = clone(stringBuilder);
        appendStreamToBuilder(supplier, newBuilder);
        return new QueryStringBuilder(newBuilder);
    }

    public QueryStringBuilder path(Path path){
        final StringBuilder newBuilder = clone(stringBuilder);
        appendPathToBuilder(path, newBuilder);
        return new QueryStringBuilder(newBuilder);
    }

    public QueryStringBuilder resource(String resourceName){
        return resource(JDBJ.class, resourceName);
    }
    
    public QueryStringBuilder resource(Class klass, String resourceName){
        final StringBuilder newBuilder = clone(stringBuilder);
        appendResourceToBuilder(klass, resourceName, newBuilder);
        return new QueryStringBuilder(newBuilder);
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
        builder.append(" -- ").append(path.toString()).append('\n');
        appendStreamToBuilder(() -> Files.newInputStream(path, StandardOpenOption.READ), builder);
    }

    private static void appendResourceToBuilder(Class jdbjClass, String resourceName, StringBuilder builder) {
        final URL url = jdbjClass.getClassLoader().getResource(resourceName);
        if (url == null) {
            throw new IllegalArgumentException("resource not found: " + resourceName);
        }
        builder.append(" -- ").append(resourceName).append('\n');
        appendStreamToBuilder(url::openStream, builder);
    }
}
