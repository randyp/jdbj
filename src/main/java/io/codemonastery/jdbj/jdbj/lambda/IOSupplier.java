package io.codemonastery.jdbj.jdbj.lambda;

import java.io.IOException;

/**
 * Similar to {@link java.util.function.Supplier} except {@link IOSupplier#get()} might throw an IOException and we would prefer to catch/handle that exception further up the call stack.
 * <p>
 * Typically used in lambda form.
 * @param <R> return type
 */
public interface IOSupplier <R> {

    R get() throws IOException;

}
