package com.github.randyp.jdbj.consumer;

import org.junit.Test;

import static org.junit.Assert.*;

public class FloatConsumerTest {

    @Test
    public void andThen() throws Exception {
        final StringBuilder actual = new StringBuilder();
        final FloatConsumer consumer = ((FloatConsumer) actual::append)
                .andThen(value -> actual.append(-value));

        consumer.accept(1.0f);
        assertEquals("1.0-1.0", actual.toString());
    }
}