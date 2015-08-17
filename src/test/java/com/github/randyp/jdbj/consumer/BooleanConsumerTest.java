package com.github.randyp.jdbj.consumer;

import org.junit.Test;

import static org.junit.Assert.*;

public class BooleanConsumerTest {

    @Test
    public void andThen() throws Exception {
        final StringBuilder actual = new StringBuilder();
        final BooleanConsumer consumer = ((BooleanConsumer) actual::append)
                .andThen(value -> actual.append(!value));

        consumer.accept(true);
        assertEquals("truefalse", actual.toString());
    }
}