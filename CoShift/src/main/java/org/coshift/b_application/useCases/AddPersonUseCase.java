package org.coshift.b_application.useCases;

import org.coshift.a_domain.person.Person;
import org.coshift.a_domain.time.TimeAccount;
import org.coshift.a_domain.time.TimeBalance;
import org.coshift.a_domain.person.PersonRole;
import org.coshift.b_application.ports.PersonRepository;

import java.util.Objects;
import java.time.LocalDateTime;

/**
 * Anwendungsfall »Person anlegen«.
 */
public class AddPersonUseCase {

    private final PersonRepository repository;

    public AddPersonUseCase(PersonRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    /**
     * Legt einen neuen Benutzer an.
     *
     * @param nickname eindeutiger Spitzname
     * @param password (vorerst) Klartext-Passwort
     * @return gespeicherte Person inkl. vergebener ID
     * @throws IllegalArgumentException falls Nickname schon existiert
     */
    public Person add(String nickname, String password) {

        repository.findByNickname(nickname)
                  .ifPresent(p -> {
                      throw new IllegalArgumentException(
                              "Nickname »" + nickname + "« already in use");
                  });

        TimeAccount account = new TimeAccount(0, new TimeBalance(0L,LocalDateTime.now()));

        Person candidate = new Person(0, nickname, password, account.getId(), PersonRole.USER);

        return repository.save(candidate);
    }
}