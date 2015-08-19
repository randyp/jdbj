package com.github.randyp.jdbj.test.binding.value;

import com.github.randyp.jdbj.HasExpectedTimeOfDay;
import com.github.randyp.jdbj.test.SimpleBuilder;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Time;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public abstract class BindTimeTest extends HasExpectedTimeOfDay implements DBSupplier {

    @Test
    public void value() throws Exception {
        final Time expected = new Time(expectedTime);
        final Time selected = new SimpleBuilder()
                .bindTime(":binding", expected)
                .execute(db(), rs -> rs.getTime(1));
        assertEquals(expected, selected);
    }

    @Test
    public void valueNull() throws Exception {
        final Time selected = new SimpleBuilder()
                .bindDate(":binding", null)
                .execute(db(), rs -> rs.getTime(1));
        assertNull(selected);
    }

    @Test
    public void valueCalendar() throws Exception {
        final Time expected = new Time(expectedTime);
        final Time selected = new SimpleBuilder()
                .bindTime(":binding", expected, GregorianCalendar.getInstance())
                .execute(db(), rs -> rs.getTime(1, GregorianCalendar.getInstance()));
        assertEquals(expected, selected);
    }

    @Test
    public void valueCalendarNull() throws Exception {
        final Time selected = new SimpleBuilder()
                .bindDate(":binding", null, GregorianCalendar.getInstance())
                .execute(db(), rs -> rs.getTime(1, GregorianCalendar.getInstance()));
        assertNull(selected);
    }
}
