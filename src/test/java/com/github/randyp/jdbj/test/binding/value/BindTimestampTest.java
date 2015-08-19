package com.github.randyp.jdbj.test.binding.value;

import com.github.randyp.jdbj.HasExpectedTimeSinceEpoch;
import com.github.randyp.jdbj.test.SimpleBuilder;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public abstract class BindTimestampTest extends HasExpectedTimeSinceEpoch implements DBSupplier {

    @Test
    public void value() throws Exception {
        final Timestamp expected = new Timestamp(expectedTime);
        final Timestamp selected = new SimpleBuilder()
                .bindTimestamp(":binding", expected)
                .execute(db(), rs -> rs.getTimestamp(1));
        assertEquals(expected, selected);
    }

    @Test
    public void valueNull() throws Exception {
        final Timestamp selected = new SimpleBuilder()
                .bindTimestamp(":binding", null)
                .execute(db(), rs -> rs.getTimestamp(1));
        assertNull(selected);
    }

    @Test
    public void valueCalendar() throws Exception {
        final Timestamp expected = new Timestamp(expectedTime);
        final Timestamp selected = new SimpleBuilder()
                .bindTimestamp(":binding", expected, GregorianCalendar.getInstance())
                .execute(db(), rs -> rs.getTimestamp(1, GregorianCalendar.getInstance()));
        assertEquals(expected, selected);
    }

    @Test
    public void valueCalendarNull() throws Exception {
        final Timestamp selected = new SimpleBuilder()
                .bindTimestamp(":binding", null, GregorianCalendar.getInstance())
                .execute(db(), rs -> rs.getTimestamp(1, GregorianCalendar.getInstance()));
        assertNull(selected);
    }
}
