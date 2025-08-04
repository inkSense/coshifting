package org.coshift.c_adapters.mapper;

import org.coshift.a_domain.person.Person;
import org.coshift.a_domain.person.PersonRole;
import org.coshift.c_adapters.dto.PersonDetailsDto;
import org.coshift.c_adapters.dto.PersonPublicDto;

public final class PersonMapper {

    private PersonMapper() { } // Verhindert die Instanzierung

    public static Person toDomain(PersonDetailsDto dto) {
        Person person = new Person(
            dto.id(),
            dto.nickname(),
            dto.password(),
            dto.timeAccountId(),
            PersonRole.valueOf(dto.role())
        );
        person.setTimeAccountId(dto.timeAccountId());
        return person;
    }

    public static PersonDetailsDto toDetailDto(Person person) {
        return new PersonDetailsDto(
            person.getId(),
            person.getNickname(),
            person.getPassword(),
            person.getTimeAccountId(),
            person.getRole().name()
        );
    }

    public static PersonPublicDto toPublicDto(Person person) {
        return new PersonPublicDto(
                person.getId(),
                person.getNickname(),
                person.getRole().name()
        );
    }
}