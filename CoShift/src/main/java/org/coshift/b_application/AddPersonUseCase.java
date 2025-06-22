package org.coshift.b_application;

import org.coshift.a_domain.Person;

import java.util.Objects;

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

        Person candidate = new Person(null, nickname, password);

        return repository.save(candidate);
    }
}