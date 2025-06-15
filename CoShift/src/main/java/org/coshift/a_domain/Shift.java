package org.coshift.a_domain;

import java.time.*;

public final class Shift {

    private final Long       id;
    private final LocalDate  date;
    private final LocalTime  startTime;
    private final LocalTime  endTime;

    public Shift(Long id, LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.id        = id;
        this.date      = date;
        this.startTime = startTime;
        this.endTime   = endTime;
    }

    public Long       getId()        { return id; }
    public LocalDate  getDate()      { return date; }
    public LocalTime  getStartTime() { return startTime; }
    public LocalTime  getEndTime()   { return endTime; }

    public int durationInMinutes() {
        return (int) Duration.between(startTime, endTime).toMinutes();
    }

    public boolean overlaps(Shift other) {
        return this.date.equals(other.date) &&
                !(this.endTime.isBefore(other.startTime) ||
                        this.startTime.isAfter(other.endTime));
    }

}
