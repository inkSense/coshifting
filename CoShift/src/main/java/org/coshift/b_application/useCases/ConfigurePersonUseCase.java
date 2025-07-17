package org.coshift.b_application.useCases;

import org.coshift.a_domain.person.Person;
import org.coshift.a_domain.time.TimeAccount;
import org.coshift.a_domain.time.TimeBalance;
import org.coshift.a_domain.person.PersonRole;
import org.coshift.b_application.ports.PersonRepository;
import org.coshift.b_application.ports.TimeAccountRepository;

import java.util.Objects;
import java.time.LocalDateTime;

/**
 * CRUD »Person«.
 */
public class ConfigurePersonUseCase {

    private final PersonRepository personRepository;
    private final TimeAccountRepository timeAccountRepository;

    public ConfigurePersonUseCase(PersonRepository personRepository, TimeAccountRepository timeAccountRepository) {
        this.personRepository = Objects.requireNonNull(personRepository);
        this.timeAccountRepository = Objects.requireNonNull(timeAccountRepository);
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
        return add(nickname, password, PersonRole.USER);
    }

    public Person add(String nickname, String password, PersonRole role) {
        personRepository.findByNickname(nickname)
                  .ifPresent(p -> {
                      throw new IllegalArgumentException(
                              "Nickname »" + nickname + "« already in use");
                  });
        TimeAccount account = new TimeAccount(0, new TimeBalance(0L, LocalDateTime.now()));
        account = timeAccountRepository.save(account);
        Person candidate = new Person(0, nickname, password, account.getId(), role);
        return personRepository.save(candidate);
    }

    public Person update(long id, PersonRole role) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Person not found"));
        person.setRole(role);
        return personRepository.save(person);
    }

    public Person update(long id, String nickname, String password, PersonRole role){
        Person p = personRepository.findById(id)
                   .orElseThrow(() -> new IllegalArgumentException("Person not found"));
        if(nickname != null && !nickname.isBlank()) p.setNickname(nickname);
        if(password != null && !password.isBlank()) p.setPassword(password);
        if(role != null) p.setRole(role);
        return personRepository.save(p);
    }

    public void delete(long id) {
        personRepository.deleteById(id);
    }
}