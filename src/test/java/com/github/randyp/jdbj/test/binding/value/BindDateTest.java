package com.github.randyp.jdbj.test.binding.value;

import com.github.randyp.jdbj.HasExpectedTimeSinceEpoch;
import com.github.randyp.jdbj.test.SimpleBuilder;
import org.junit.Test;

import java.sql.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public abstract class BindDateTest extends HasExpectedTimeSinceEpoch implements DBSupplier {

    @Test
    public void value() throws Exception {
        final Date selected = builder()
                .bindDate(":binding", expectedDate)
                .execute(db(), rs -> rs.getDate(1));
        assertEquals(expectedDate, selected);
    }

    @Test
    public void valueNull() throws Exception {
        final Date selected = builder()
                .bindDate(":binding", null)
                .execute(db(), rs -> rs.getDate(1));
        assertNull(selected);
    }

    @Test
    public void valueCalendar() throws Exception {
        final Date selected = builder()
                .bindDate(":binding", expectedDate, GregorianCalendar.getInstance())
                .execute(db(), rs -> rs.getDate(1, GregorianCalendar.getInstance()));
        assertEquals(expectedDate, selected);
    }

    @Test
    public void valueCalendarNull() throws Exception {
        final Date selected = builder()
                .bindDate(":binding", null, GregorianCalendar.getInstance())
                .execute(db(), rs -> rs.getDate(1, GregorianCalendar.getInstance()));
        assertNull(selected);
    }

    public SimpleBuilder builder() {
        return new SimpleBuilder();
    }

}
