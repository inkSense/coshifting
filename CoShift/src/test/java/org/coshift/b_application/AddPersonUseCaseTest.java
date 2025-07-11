package org.coshift.b_application;

import org.coshift.a_domain.person.Person;
import org.coshift.b_application.ports.PersonRepository;
import org.coshift.b_application.ports.TimeAccountRepository;
import org.coshift.b_application.useCases.AddPersonUseCase;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit-Tests für {@link AddPersonUseCase}.
 */
class AddPersonUseCaseTest {

    @Test
    void addPerson_saves_and_returns_person_with_id() {
        // Arrange ------------------------------------------------------
        PersonRepository repo = mock(PersonRepository.class);
        TimeAccountRepository timeAccountRepo = mock(TimeAccountRepository.class);
        AddPersonUseCase useCase = new AddPersonUseCase(repo, timeAccountRepo);

        String nick = "Alice";
        String pw   = "secret";

        when(repo.findByNickname(nick))
                .thenReturn(Optional.empty());

        Person persisted = new Person(1L, nick, pw);
        when(repo.save(any(Person.class))).thenReturn(persisted);

        // Act ----------------------------------------------------------
        Person result = useCase.add(nick, pw);

        // Assert -------------------------------------------------------
        verify(repo).findByNickname(nick);
        verify(repo).save(any(Person.class));

        assertEquals(1L, result.getId());
        assertEquals(nick, result.getNickname());
    }

    @Test
    void addPerson_duplicateNickname_throws_exception() {
        // Arrange ------------------------------------------------------
        PersonRepository repo = mock(PersonRepository.class);
        TimeAccountRepository timeAccountRepo = mock(TimeAccountRepository.class);
        AddPersonUseCase useCase = new AddPersonUseCase(repo, timeAccountRepo);

        String nick = "Bob";
        String pw   = "pwd";

        when(repo.findByNickname(nick))
                .thenReturn(Optional.of(new Person(99L, nick, "ignored")));

        // Act & Assert -------------------------------------------------
        assertThrows(IllegalArgumentException.class,
                     () -> useCase.add(nick, pw));

        verify(repo).findByNickname(nick);
        verify(repo, never()).save(any());
    }
}