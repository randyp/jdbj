package io.codemonastery.jdbj.jdbj;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;

public abstract class HasExpectedTimeSinceEpoch {

    protected final long expectedTime;
    protected final Date expectedDate;
    protected final Timestamp expectedTimestamp;

    public HasExpectedTimeSinceEpoch() {
        final Calendar instance = GregorianCalendar.getInstance();
        instance.set(2015, Calendar.JANUARY, 1, 0, 0, 0);
        this.expectedTime = 1000 * (instance.getTimeInMillis() / 1000);
        this.expectedDate = new Date(expectedTime);
        this.expectedTimestamp = new Timestamp(expectedTime);
    }
}
