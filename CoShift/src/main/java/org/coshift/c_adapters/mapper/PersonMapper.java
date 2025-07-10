package org.coshift.c_adapters.mapper;

import org.coshift.a_domain.person.Person;
import org.coshift.a_domain.person.PersonRole;
import org.coshift.c_adapters.dto.PersonDto;

public final class PersonMapper {

    private PersonMapper() { } // prevents from instantiation

    public static Person toDomain(PersonDto dto) {
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

    public static PersonDto toDto(Person person) {
        return new PersonDto(
            person.getId(),
            person.getNickname(),
            person.getPassword(),
            person.getTimeAccountId(),
            person.getRole().name()
        );
    }
}