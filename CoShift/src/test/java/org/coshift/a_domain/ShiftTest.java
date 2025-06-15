package org.coshift.a_domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShiftTest {

    @Test
    void durationIsCalculatedCorrectly() {
        Shift s = new Shift(null, LocalDate.now(),
                LocalTime.of(18,0), LocalTime.of(20,0));
        assertEquals(120, s.durationInMinutes());
    }

    @Test
    void overlapsDetectsCollision() {
        Shift a = new Shift(null, LocalDate.of(2025,6,15),
                LocalTime.of(18,0), LocalTime.of(20,0));
        Shift b = new Shift(null, LocalDate.of(2025,6,15),
                LocalTime.of(19,0), LocalTime.of(21,0));
        assertTrue(a.overlaps(b));
    }
}
