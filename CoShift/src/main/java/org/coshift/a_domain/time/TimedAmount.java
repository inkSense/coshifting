package org.coshift.a_domain.time;

import java.time.LocalDateTime;

abstract class TimedAmount {
    private long amountInMinutes;
    private LocalDateTime pointInTime;

    public TimedAmount(long amountInMinutes, LocalDateTime pointInTime) {
        this.amountInMinutes = amountInMinutes;
        this.pointInTime = pointInTime;
    }

    public long getAmountInMinutes() {
        return amountInMinutes;
    }

    public void setAmountInMinutes(long amountInMinutes) {
        this.amountInMinutes = amountInMinutes;
    }

    public LocalDateTime getPointInTime() {
        return pointInTime;
    }

    public void setPointInTime(LocalDateTime pointInTime) {
        this.pointInTime = pointInTime;
    }

    @Override
    public String toString() {
        return "TimedAmount{" +
                "amountInMinutes=" + amountInMinutes +
                ", pointInTime=" + pointInTime +
                '}';
    }
}

