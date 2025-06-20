package org.coshift.a_domain;

import java.time.LocalDateTime;

public final class Shift {

    private final Long            id;
    private final LocalDateTime   startTime;
    private final LocalDateTime   endTime;
    private final int             durationInMinutes;

    public Shift(Long id, LocalDateTime startTime, int durationInMinutes) {
        this.id                = id;
        this.startTime         = startTime;
        this.durationInMinutes = durationInMinutes;
        this.endTime           = startTime.plusMinutes(durationInMinutes);
    }

    public Long            getId()               { return id; }
    public LocalDateTime   getStartTime()        { return startTime; }
    public LocalDateTime   getEndTime()          { return endTime; }
    public int             getDurationInMinutes(){ return durationInMinutes; }

    /* -------- Geschäftslogik -------- */
    public boolean overlaps(Shift other) {
        return !(this.endTime.isBefore(other.startTime) ||
                 this.startTime.isAfter(other.endTime));
    }
}