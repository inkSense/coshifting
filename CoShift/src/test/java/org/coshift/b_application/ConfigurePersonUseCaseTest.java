package org.coshift.b_application;

import org.coshift.a_domain.person.Person;
import org.coshift.a_domain.person.PersonRole;
import org.coshift.b_application.ports.PersonRepository;
import org.coshift.b_application.ports.TimeAccountRepository;
import org.coshift.b_application.useCases.ConfigurePersonUseCase;
import org.coshift.c_adapters.persistence.json.PersonJsonRepository;
import org.coshift.d_frameworks.gson.PersonGsonFileAccessor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit-Tests für {@link ConfigurePersonUseCase}.
 */
class ConfigurePersonUseCaseTest {
    @TempDir static Path tmp;
    PersonRepository personRepository = mock(PersonRepository.class);
    TimeAccountRepository timeAccountRepo = mock(TimeAccountRepository.class);
    ConfigurePersonUseCase useCase = new ConfigurePersonUseCase(personRepository, timeAccountRepo);

    @BeforeAll
    static void setUp() {
        // ToDo: Hier sollten die Testdaten für die Personen-Datei erstellt werden
    }

    @Test
    void addPerson_saves_and_returns_person_with_id() {
        String nick = "Alice";
        String pw   = "secret";
        when(personRepository.findByNickname(nick))
                .thenReturn(Optional.empty());
        Person persisted = new Person(1L, nick, pw);
        when(personRepository.save(any(Person.class))).thenReturn(persisted);
        when(timeAccountRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act ----------------------------------------------------------
        Person result = useCase.add(nick, pw);

        // Assert -------------------------------------------------------
        verify(personRepository).findByNickname(nick);
        verify(personRepository).save(any(Person.class));

        assertEquals(1L, result.getId());
        assertEquals(nick, result.getNickname());
    }

    @Test
    void addPerson_duplicateNickname_throws_exception() {
        String nick = "Bob";
        String pw   = "pwd";
        when(personRepository.findByNickname(nick))
                .thenReturn(Optional.of(new Person(99L, nick, "ignored")));
        // Act & Assert -------------------------------------------------
        assertThrows(IllegalArgumentException.class,
                     () -> useCase.add(nick, pw));
        verify(personRepository).findByNickname(nick);
        verify(personRepository, never()).save(any());
    }

    @Test
    void update_changes_role_and_persists_it() {

        Path file = tmp.resolve("persons.json");
        String json = """
            [{
                "id": 1,
                "nickname": "anton",
                "password": "secret",
                "timeAccountId": 1,
                "role": "USER"
            }]
        """;
        try {
            Files.writeString(file, json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        var accessor = new PersonGsonFileAccessor(file);
        PersonRepository personRepo = new PersonJsonRepository(accessor);

        Person anton = personRepo.findById(1L).orElseThrow(() -> new IllegalArgumentException("Person not found")); //save(new Person(1, "anton", "secret"));
        var useCase = new ConfigurePersonUseCase(personRepo, timeAccountRepo);

        /* --- When  ------------------------------------------------ */
        Person updated = useCase.update(anton.getId(), PersonRole.ADMIN);

        /* --- Then  ------------------------------------------------ */
        assertEquals(PersonRole.ADMIN, updated.getRole());

        // Neu laden, um Persistenz zu prüfen
        Person reloaded = personRepo.findById(anton.getId()).orElseThrow();
        assertEquals(PersonRole.ADMIN, reloaded.getRole());
    }

}