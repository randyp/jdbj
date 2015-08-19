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
        final Date expected = new Date(expectedTime);
        final Date selected = new SimpleBuilder()
                .bindDate(":binding", expected)
                .execute(db(), rs -> rs.getDate(1));
        assertEquals(expected, selected);
    }

    @Test
    public void valueNull() throws Exception {
        final Date selected = new SimpleBuilder()
                .bindDate(":binding", null)
                .execute(db(), rs -> rs.getDate(1));
        assertNull(selected);
    }

    @Test
    public void valueCalendar() throws Exception {
        final Date expected = new Date(expectedTime);
        final Date selected = new SimpleBuilder()
                .bindDate(":binding", expected, GregorianCalendar.getInstance())
                .execute(db(), rs -> rs.getDate(1, GregorianCalendar.getInstance()));
        assertEquals(expected, selected);
    }

    @Test
    public void valueCalendarNull() throws Exception {
        final Date selected = new SimpleBuilder()
                .bindDate(":binding", null, GregorianCalendar.getInstance())
                .execute(db(), rs -> rs.getDate(1, GregorianCalendar.getInstance()));
        assertNull(selected);
    }

}
