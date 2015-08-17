package com.github.randyp.jdbj.consumer;

import org.junit.Test;

import static org.junit.Assert.*;

public class ByteConsumerTest {
    @Test
    public void andThen() throws Exception {
        final StringBuilder actual = new StringBuilder();
        final ByteConsumer consumer = ((ByteConsumer) actual::append)
                .andThen(value -> actual.append(-value));

        consumer.accept((byte)1);
        assertEquals("1-1", actual.toString());
    }
}