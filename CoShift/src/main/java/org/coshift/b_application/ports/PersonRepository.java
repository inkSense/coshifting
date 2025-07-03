package org.coshift.b_application.ports;

import java.util.List;
import java.util.Optional;

import org.coshift.a_domain.person.Person;

public interface PersonRepository {

    Person save(Person person);

    Optional<Person> findById(Long id);

    Optional<Person> findByNickname(String nickname);

    List<Person> findAll();

    void deleteById(Long id);
}