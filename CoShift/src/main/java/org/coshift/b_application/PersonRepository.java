package org.coshift.b_application;

import org.coshift.a_domain.Person;

import java.util.List;
import java.util.Optional;

public interface PersonRepository {

    Person save(Person person);

    Optional<Person> findById(Long id);

    Optional<Person> findByNickname(String nickname);

    List<Person> findAll();

    void deleteById(Long id);
}