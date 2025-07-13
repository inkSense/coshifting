package org.coshift.c_adapters.web;

import org.coshift.b_application.UseCaseInteractor;
import org.coshift.b_application.ports.PersonRepository;
import org.coshift.b_application.ports.TimeAccountRepository;
import org.coshift.c_adapters.dto.PersonDto;
import org.coshift.c_adapters.dto.TimeAccountDto;
import org.coshift.c_adapters.mapper.PersonMapper;
import org.coshift.c_adapters.mapper.TimeAccountMapper;
import org.coshift.a_domain.person.Person;
import org.coshift.a_domain.person.PersonRole;
import org.coshift.a_domain.time.TimeAccount;

import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST-Controller für Personen-bezogene Funktionen
 *  – /api/persons
 *  – /api/persons/{id}
 *  – /api/persons/{id}/timeaccount
 */
@RestController
@RequestMapping("/api/persons")
public class PersonController {

    private final UseCaseInteractor      interactor; // für addPerson
    private final PersonRepository       persons;
    private final TimeAccountRepository  accounts;

    public PersonController(UseCaseInteractor interactor,
                            PersonRepository persons,
                            TimeAccountRepository accounts) {
        this.interactor = interactor;
        this.persons    = persons;
        this.accounts   = accounts;
    }

    /* ---------- CREATE ---------------------------------------------- */

    // Einfaches DTO fürs Anlegen neuer Personen
    public record NewPersonDto(String nickname, String password) {}

    @PostMapping
    public PersonDto create(@RequestBody NewPersonDto dto) {
        Person p = interactor.addPerson(dto.nickname(), dto.password());
        return PersonMapper.toDto(p);
    }

    /* ---------- READ ------------------------------------------------ */

    @GetMapping
    public List<PersonDto> all() {
        return persons.findAll().stream()
                      .map(PersonMapper::toDto)
                      .toList();
    }

    @GetMapping("/{id}")
    public PersonDto byId(@PathVariable long id) {
        Person p = persons.findById(id)
                          .orElseThrow(() -> new IllegalArgumentException("Person not found"));
        return PersonMapper.toDto(p);
    }

    @GetMapping("/{id}/timeaccount")
    public TimeAccountDto timeAccount(@PathVariable long id) {
        TimeAccount acc = interactor.getTimeAccount(id);
        return TimeAccountMapper.toDto(acc);
    }

    @GetMapping("/me/timeaccount")
    public TimeAccountDto myAccount(Authentication auth) {
        Object principal = auth.getPrincipal();
        if (!(principal instanceof Person user)) {
            throw new IllegalStateException("Principal is not a Person");
        }
        TimeAccount acc = accounts.findById(user.getTimeAccountId())
                                  .orElseThrow(() -> new IllegalStateException("Account missing"));
        return TimeAccountMapper.toDto(acc);
    }




    /* ---------- UPDATE ---------------------------------------------- */

    @PutMapping("/{id}/role")
    public PersonDto updateRole(@PathVariable long id, @RequestBody PersonRole role) {
        Person person = interactor.updatePersonRole(id, role);
        return PersonMapper.toDto(person);
    }

}