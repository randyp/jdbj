package io.codemonastery.jdbj.test.binding.value;

import io.codemonastery.jdbj.SimpleBuilder;
import io.codemonastery.jdbj.HasExpectedTimeOfDay;
import org.junit.Test;

import java.sql.Time;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public abstract class BindTimeTest extends HasExpectedTimeOfDay implements DBSupplier {

    @Test
    public void value() throws Exception {
        final Time expected = new Time(expectedMillis);
        final Time selected = builder()
                .bindTime(":binding", expected)
                .execute(db(), rs -> rs.getTime(1));
        assertEquals(expected, selected);
    }

    @Test
    public void valueNull() throws Exception {
        final Time selected = builder()
                .bindDate(":binding", null)
                .execute(db(), rs -> rs.getTime(1));
        assertNull(selected);
    }

    @Test
    public void valueCalendar() throws Exception {
        final Time expected = new Time(expectedMillis);
        final Time selected = builder()
                .bindTime(":binding", expected, GregorianCalendar.getInstance())
                .execute(db(), rs -> rs.getTime(1, GregorianCalendar.getInstance()));
        assertEquals(expected, selected);
    }

    @Test
    public void valueCalendarNull() throws Exception {
        final Time selected = builder()
                .bindDate(":binding", null, GregorianCalendar.getInstance())
                .execute(db(), rs -> rs.getTime(1, GregorianCalendar.getInstance()));
        assertNull(selected);
    }

    public SimpleBuilder builder() {
        return new SimpleBuilder();
    }
}
