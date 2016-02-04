package io.codemonastery.jdbj;

import io.codemonastery.jdbj.lambda.ConnectionCallable;
import io.codemonastery.jdbj.lambda.ConnectionRunnable;
import io.codemonastery.jdbj.lambda.IOSupplier;
import io.codemonastery.jdbj.lambda.ResultMapper;

import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Path;

/**
 * Entry point for building and executing queries, transactions with JDBJ features.
 * <p>
 * If you can, prefer {@link JDBJ#resource(String)} over {@link JDBJ#string(String)}.
 * <p>
 * However, since {@link JDBJ#string(String)} is commonly used in tests, convenience methods are provided to build queries with a single string: {@link JDBJ#query(String)}, {@link JDBJ#update(String)}, {@link JDBJ#insert(String, ResultMapper)}, {@link JDBJ#statement(String)}, {@link JDBJ#script(String)}.
 */
public final class JDBJ {

    /**
     * See {@link Transaction} for documentation.
     * @param runnable runnable
     * @return transaction builder
     */
    public static Transaction transaction(ConnectionRunnable runnable){
        return new Transaction(runnable);
    }

    /**
     * See {@link ReturningTransaction} for documentation.
     * @param callable callable
     * @param <R> callable return type
     * @return transaction builder
     */
    public static <R> ReturningTransaction<R> returningTransaction(ConnectionCallable<R> callable){
        return new ReturningTransaction<>(callable);
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
}

