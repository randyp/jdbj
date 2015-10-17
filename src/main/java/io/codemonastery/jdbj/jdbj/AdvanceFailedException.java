package io.codemonastery.jdbj.jdbj;

import java.util.function.Consumer;

/**
 * Runtime exception to wrap any exception caught in {@link ResultSetSpliterator#tryAdvance(Consumer)}.
 * <p>
 * If you are using {@link StreamQuery} and need to catch all exceptions, be sure to catch this one.
 * <p>
 * Cause will never be null;
 * @see ResultSetSpliterator
 * @see StreamQuery
 */
public class AdvanceFailedException extends RuntimeException {

    public AdvanceFailedException(Throwable cause) {
        super(cause);
    }
}
