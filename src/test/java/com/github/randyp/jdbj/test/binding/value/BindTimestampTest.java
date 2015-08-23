package com.github.randyp.jdbj.test.binding.value;

import com.github.randyp.jdbj.HasExpectedTimeSinceEpoch;
import com.github.randyp.jdbj.SimpleBuilder;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public abstract class BindTimestampTest extends HasExpectedTimeSinceEpoch implements DBSupplier {

    @Test
    public void value() throws Exception {
        final Timestamp selected = builder()
                .bindTimestamp(":binding", expectedTimestamp)
                .execute(db(), rs -> rs.getTimestamp(1));
        assertEquals(expectedTimestamp, selected);
    }

    @Test
    public void valueNull() throws Exception {
        final Timestamp selected = builder()
                .bindTimestamp(":binding", null)
                .execute(db(), rs -> rs.getTimestamp(1));
        assertNull(selected);
    }

    @Test
    public void valueCalendar() throws Exception {
        final Timestamp selected = builder()
                .bindTimestamp(":binding", expectedTimestamp, GregorianCalendar.getInstance())
                .execute(db(), rs -> rs.getTimestamp(1, GregorianCalendar.getInstance()));
        assertEquals(expectedTimestamp, selected);
    }

    @Test
    public void valueCalendarNull() throws Exception {
        final Timestamp selected = builder()
                .bindTimestamp(":binding", null, GregorianCalendar.getInstance())
                .execute(db(), rs -> rs.getTimestamp(1, GregorianCalendar.getInstance()));
        assertNull(selected);
    }

    public SimpleBuilder builder() {
        return new SimpleBuilder();
    }
}
