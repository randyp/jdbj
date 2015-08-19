package com.github.randyp.jdbj;

import java.util.Calendar;
import java.util.GregorianCalendar;

public abstract class HasExpectedTimeSinceEpoch {

    protected final long expectedTime;

    public HasExpectedTimeSinceEpoch() {
        final Calendar instance = GregorianCalendar.getInstance();
        instance.set(2015, Calendar.JANUARY, 1, 0, 0, 0);
        this.expectedTime = 1000 * (instance.getTimeInMillis() / 1000);
    }
}
