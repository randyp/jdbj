package io.codemonastery.jdbj;

import java.sql.Time;
import java.util.Calendar;
import java.util.GregorianCalendar;

public abstract class HasExpectedTimeOfDay {

    protected final long expectedMillis;
    protected final Time expectedTime;

    public HasExpectedTimeOfDay() {
        final Calendar instance = GregorianCalendar.getInstance();
        instance.set(1970, Calendar.JANUARY, 1, 12, 11, 10);
        this.expectedMillis = 1000 * (instance.getTimeInMillis() / 1000);
        this.expectedTime = new Time(expectedMillis);
    }
}
