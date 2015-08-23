package com.github.randyp.jdbj;

import java.util.function.Consumer;

/**
 * Runtime exception which wraps any exception caught in {@link ResultSetSpliterator#tryAdvance(Consumer)}.
 * @see ResultSetSpliterator
 * @see StreamQuery
 */
public class AdvanceFailedException extends RuntimeException {

    public AdvanceFailedException(Throwable cause) {
        super(cause);
    }
}
