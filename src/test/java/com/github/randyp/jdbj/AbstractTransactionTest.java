package com.github.randyp.jdbj;

import com.github.randyp.jdbj.lambda.ConnectionCallable;
import org.junit.Test;

public class AbstractTransactionTest {

    @Test(expected = IllegalArgumentException.class)
    public void invalidIsolation() throws Exception {
        new AbstractTransaction<Void>(Integer.MIN_VALUE){

            @Override
            ConnectionCallable<Void> callable() {
                return connection -> null;
            }
        };

    }
}
