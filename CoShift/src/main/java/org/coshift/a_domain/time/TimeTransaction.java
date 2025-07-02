package org.coshift.a_domain.time;

import java.time.LocalDateTime;

/**
 * Soll die Veränderung eines Zeitkontos an einem Zeitpunkt repräsentieren.
 */
public class TimeTransaction extends TimedAmount {
    public TimeTransaction(long amountInMinutes, LocalDateTime pointInTime) {
        super(amountInMinutes, pointInTime);
    }

}


