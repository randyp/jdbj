package com.github.randyp.jdbj;

import java.util.Calendar;
import java.util.GregorianCalendar;

public abstract class HasExpectedTimeOfDay {

    final long expectedTime;

    public HasExpectedTimeOfDay() {
        final Calendar instance = GregorianCalendar.getInstance();
        instance.set(1970, Calendar.JANUARY, 1, 12, 11, 10);
        this.expectedTime = 1000 * (instance.getTimeInMillis() / 1000);
    }
}
