package com.github.randyp.jdbj;

import org.junit.Test;


public class AdvanceFailedExceptionTest {
    @Test(expected = NullPointerException.class)
    public void causeNotNull() throws Exception {
        //noinspection ThrowableInstanceNeverThrown
        new AdvanceFailedException(null);
    }
}
