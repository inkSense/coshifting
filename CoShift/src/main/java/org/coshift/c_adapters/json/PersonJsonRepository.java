package org.coshift.c_adapters.json;

import org.coshift.a_domain.Person;
import org.coshift.b_application.PersonRepository;
import org.coshift.c_adapters.PersonDto;
import org.coshift.c_adapters.PersonMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Primärer Adapter für Personen-Persistenz.
 *
 *  – Implementiert {@link PersonRepository}.<br>
 *  – Nutzt {@link PersonJsonFileAccessor} für reines Datei-Handling.<br>
 *  – Enthält Such- sowie ID-Vergabe-Logik, bleibt aber JSON-Bibliotheksfrei.
 */
@Component
public class PersonJsonRepository implements PersonRepository {

    private final PersonJsonFileAccessor fileAccessor;
    private final AtomicLong nextId = new AtomicLong(1);

    public PersonJsonRepository(PersonJsonFileAccessor fileAccessor) {
        this.fileAccessor = fileAccessor;

        /* ----- maximale bestehende Id ermitteln -------------------- */
        fileAccessor.readAll().stream()
                .map(PersonMapper::toDomain)
                .map(Person::getId)
                .filter(Objects::nonNull)
                .mapToLong(Long::longValue)
                .max()
                .ifPresent(nextId::set);
    }

    /* ---------------- Speichern ----------------------------------- */

    @Override
    public Person save(Person person) {
        Long id = Optional.ofNullable(person.getId())
                          .orElseGet(nextId::getAndIncrement);

        Person withId = new Person(id,
                                   person.getNickname(),
                                   person.getPassword());
        withId.setTimeAccountId(person.getTimeAccountId());

        List<PersonDto> dtos = new ArrayList<>(fileAccessor.readAll());
        dtos.removeIf(dto -> Objects.equals(dto.id(), id));
        dtos.add(PersonMapper.toDto(withId));

        if (!fileAccessor.writeAll(dtos)) {
            throw new IllegalStateException("Unable to persist persons JSON file.");
        }
        return withId;
    }

    /* ---------------- Lesen --------------------------------------- */

    @Override
    public Optional<Person> findById(Long id) {
        return fileAccessor.readAll().stream()
                .filter(dto -> Objects.equals(dto.id(), id))
                .findFirst()
                .map(PersonMapper::toDomain);
    }

    @Override
    public Optional<Person> findByNickname(String nickname) {
        return fileAccessor.readAll().stream()
                .filter(dto -> dto.nickname().equalsIgnoreCase(nickname))
                .findFirst()
                .map(PersonMapper::toDomain);
    }

    @Override
    public List<Person> findAll() {
        return fileAccessor.readAll().stream()
                .map(PersonMapper::toDomain)
                .collect(Collectors.toList());
    }

    /* ---------------- Löschen ------------------------------------- */

    @Override
    public void deleteById(Long id) {
        List<PersonDto> dtos = new ArrayList<>(fileAccessor.readAll());
        boolean removed = dtos.removeIf(dto -> Objects.equals(dto.id(), id));

        if (removed && !fileAccessor.writeAll(dtos)) {
            throw new IllegalStateException("Unable to persist persons JSON file.");
        }
    }
}