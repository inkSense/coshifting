package org.coshift.a_domain;

import java.time.LocalDateTime;

/**
 * Soll eine aktuelle Endsumme eines Zeitkontos repräsentieren.
 */
public class TimeBalance extends TimedAmount {
    public TimeBalance(long amountInMinutes, LocalDateTime pointInTime) {
        super(amountInMinutes, pointInTime);
    }
}
