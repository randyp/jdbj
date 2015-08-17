package com.github.randyp.jdbj.consumer;

import org.junit.Test;

import static org.junit.Assert.*;

public class ShortConsumerTest {
    @Test
    public void andThen() throws Exception {
        final StringBuilder actual = new StringBuilder();
        final ShortConsumer consumer = ((ShortConsumer) actual::append)
                .andThen(value -> actual.append(-value));

        consumer.accept((short) 1);
        assertEquals("1-1", actual.toString());
    }
}