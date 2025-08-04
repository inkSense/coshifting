package org.coshift.b_application;

import org.coshift.a_domain.Shift;
import org.coshift.a_domain.person.Person;
import org.coshift.b_application.ports.PersonRepository;
import org.coshift.b_application.ports.ShiftRepository;
import org.coshift.b_application.useCases.ConfigurePersonsInShiftUseCase;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link ConfigurePersonsInShiftUseCase}.
 */
class ConfigurePersonsInShiftUseCaseTest {

    @Test
    void addPerson_persists_updated_shift() {
        // Arrange ----------------------------------------------------------
        ShiftRepository shiftRepo = mock(ShiftRepository.class);
        PersonRepository personRepo = mock(PersonRepository.class);
        ConfigurePersonsInShiftUseCase useCase = new ConfigurePersonsInShiftUseCase(shiftRepo, personRepo);

        long shiftId = 1L;
        long personId = 42L;
        Shift shift = new Shift(shiftId, LocalDateTime.of(2025, 6, 22, 14, 0), 120, 2);
        Person person = new Person(personId, "Bob", "pwd");

        when(shiftRepo.findById(shiftId)).thenReturn(Optional.of(shift));
        when(personRepo.findById(personId)).thenReturn(Optional.of(person));
        when(shiftRepo.save(any(Shift.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act --------------------------------------------------------------
        Shift result = useCase.add(personId, shiftId);

        // Assert -----------------------------------------------------------
        verify(shiftRepo).findById(shiftId);
        verify(personRepo).findById(personId);
        verify(shiftRepo).save(shift);

        assertTrue(shift.getPersons().contains(person));
        assertSame(shift, result);
    }


    @Test
    void addPerson_full_shift_throws_exception() {
        // Arrange ----------------------------------------------------------
        ShiftRepository shiftRepo = mock(ShiftRepository.class);
        PersonRepository personRepo = mock(PersonRepository.class);
        ConfigurePersonsInShiftUseCase useCase = new ConfigurePersonsInShiftUseCase(shiftRepo, personRepo);

        long shiftId = 2L;
        long personId = 7L;
        Shift shift = new Shift(shiftId, LocalDateTime.of(2025, 6, 23, 18, 0), 60, 1);
        // Schicht mit einer Person füllen
        shift.addPerson(new Person(99L, "Alice", "pwd"));
        Person person = new Person(personId, "Bob", "pwd");

        when(shiftRepo.findById(shiftId)).thenReturn(Optional.of(shift));
        when(personRepo.findById(personId)).thenReturn(Optional.of(person));

        // Act & Assert -----------------------------------------------------
        assertThrows(IllegalStateException.class, () -> useCase.add(personId, shiftId));

        verify(shiftRepo).findById(shiftId);
        verify(personRepo).findById(personId);
        verify(shiftRepo, never()).save(any());
    }


    @Test
    void removePerson_persists_updated_shift() {
        // Arrange ----------------------------------------------------------
        ShiftRepository shiftRepo = mock(ShiftRepository.class);
        PersonRepository personRepo = mock(PersonRepository.class);
        ConfigurePersonsInShiftUseCase useCase = new ConfigurePersonsInShiftUseCase(shiftRepo, personRepo);

        long shiftId = 3L;
        long personId = 8L;
        Person person = new Person(personId, "Eve", "pwd");
        Shift shift = new Shift(shiftId, LocalDateTime.of(2025, 6, 24, 10, 0), 90, 2);
        shift.addPerson(person);

        when(shiftRepo.findById(shiftId)).thenReturn(Optional.of(shift));
        when(personRepo.findById(personId)).thenReturn(Optional.of(person));
        when(shiftRepo.save(any(Shift.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act --------------------------------------------------------------
        Shift result = useCase.remove(personId, shiftId);

        // Assert -----------------------------------------------------------
        verify(shiftRepo).findById(shiftId);
        verify(personRepo).findById(personId);
        verify(shiftRepo).save(shift);

        assertFalse(shift.getPersons().contains(person));
        assertSame(shift, result);
    }


}
