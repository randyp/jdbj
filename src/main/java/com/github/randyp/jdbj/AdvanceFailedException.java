package com.github.randyp.jdbj;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Runtime exception to wrap any exception caught in {@link ResultSetSpliterator#tryAdvance(Consumer)}.
 * <p>
 * If you are using {@link StreamQuery} and need to catch all exceptions, be sure to catch this one. 
 * @see ResultSetSpliterator
 * @see StreamQuery
 */
public class AdvanceFailedException extends RuntimeException {

    public AdvanceFailedException(Throwable cause) {
        super(cause);
        Objects.requireNonNull(cause, "cause must not be null");
    }
}
