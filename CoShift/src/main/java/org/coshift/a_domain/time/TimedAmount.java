package org.coshift.a_domain.time;

import java.time.LocalDateTime;

public abstract class TimedAmount {
    protected final long amountInMinutes;
    protected final LocalDateTime pointInTime;

    public TimedAmount(long amountInMinutes, LocalDateTime pointInTime) {
        this.amountInMinutes = amountInMinutes;
        this.pointInTime = pointInTime;
    }

    public long getAmountInMinutes() {
        return amountInMinutes;
    }

    public LocalDateTime getPointInTime() {
        return pointInTime;
    }
}

