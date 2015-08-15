package com.github.randyp.jdbj.lambda;

import java.io.IOException;

public interface IOSupplier <R> {

    R get() throws IOException;

}
