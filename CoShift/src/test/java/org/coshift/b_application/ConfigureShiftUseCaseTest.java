package org.coshift.b_application;

import org.coshift.a_domain.Shift;
import org.coshift.b_application.ports.ShiftRepository;
import org.coshift.b_application.useCases.ConfigureShiftUseCase;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit test for {@link ConfigureShiftUseCase}.
 */
class ConfigureShiftUseCaseTest {

    @Test
    void addShift_persists_and_returns_shift_with_id() {
        // Arrange ----------------------------------------------------------
        ShiftRepository repo = mock(ShiftRepository.class);
        ConfigureShiftUseCase useCase = new ConfigureShiftUseCase(repo);

        LocalDateTime start = LocalDateTime.of(2025, 6, 22, 14, 0);

        // Repository soll keine Kollisionen melden
        when(repo.findByDate(start.toLocalDate()))
                .thenReturn(List.of());

        // Repository gibt gespeicherte Schicht mit vergebener Id zurück
        Shift persisted = new Shift(1L, start, 120, 10);
        when(repo.save(any(Shift.class))).thenReturn(persisted);

        // Act --------------------------------------------------------------
        Shift result = useCase.add(start, 120, 10);

        // Assert -----------------------------------------------------------
        verify(repo).findByDate(start.toLocalDate());
        verify(repo).save(any(Shift.class));

        assertEquals(1L, result.getId());
        assertEquals(120, result.getDurationInMinutes());
        assertEquals(start, result.getStartTime());
    }
}