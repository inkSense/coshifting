package org.coshift.b_application;

import org.coshift.a_domain.Shift;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ShiftServiceTest {

    ShiftRepository repo = mock(ShiftRepository.class);
    ShiftService service = new ShiftService(repo);

    @Test
    void upcomingShiftsDelegatesToRepo() {
        // given
        when(repo.findAll())
                .thenReturn(
                        List.of(
                            new Shift(
                                    null,
                                    LocalDate.of(2025, 6, 15),
                                    LocalTime.of(18,0),
                                    LocalTime.of(20,0)
                            )
                    )
        );
        // when / then
        assertEquals(1, service.upcomingShifts().size());
        verify(repo).findAll();
    }
}
