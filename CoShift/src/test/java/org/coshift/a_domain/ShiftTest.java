package org.coshift.a_domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ShiftTest {

    /* ---------- Dauerberechnung ------------------------------------ */

    @Nested
    @DisplayName("Dauerberechnung")
    class Duration {

        @Test
        void sameDay_returnsExactMinutes() {
            Shift s = new Shift(null,
                                LocalDateTime.of(2025, 7, 3, 18, 0),
                                120,
                                10);
            assertThat(s.getDurationInMinutes()).isEqualTo(120);
        }

        @Test
        void overMidnight_keepsDuration() {
            Shift s = new Shift(null,
                                LocalDateTime.of(2025, 7, 3, 23, 0),
                                120,
                                10);
            assertThat(s.getDurationInMinutes()).isEqualTo(120);
        }
    }

    /* ---------- Überschneidung ------------------------------------- */

    @Nested
    @DisplayName("Überschneidung")
    class Overlap {

        @Test
        void overlappingIntervals_returnTrue() {
            Shift a = new Shift(null, LocalDateTime.of(2025, 7, 3, 18, 0), 120, 10);
            Shift b = new Shift(null, LocalDateTime.of(2025, 7, 3, 19, 0), 120, 10);

            assertThat(a.overlaps(b)).isTrue();
            assertThat(b.overlaps(a)).isTrue();
        }

        @Test
        void touchingIntervals_areConsideredOverlappingByCurrentLogic() {
            Shift a = new Shift(null, LocalDateTime.of(2025, 7, 3, 18, 0), 120, 10); // endet 20:00
            Shift b = new Shift(null, LocalDateTime.of(2025, 7, 3, 20, 0), 120, 10); // startet 20:00

            assertThat(a.overlaps(b)).isTrue();
        }

        @Test
        void differentDays_returnFalse() {
            Shift a = new Shift(null, LocalDateTime.of(2025, 7, 3, 18, 0), 120, 10);
            Shift b = new Shift(null, LocalDateTime.of(2025, 7, 4, 18, 0), 120, 10);

            assertThat(a.overlaps(b)).isFalse();
        }
    }
}