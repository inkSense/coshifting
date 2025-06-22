package org.coshift.c_adapters;

import org.coshift.a_domain.Person;

public final class PersonMapper {

    private PersonMapper() { } // undermines instantiation

    public static Person toDomain(PersonDto dto) {
        Person p = new Person(dto.id(),
                              dto.nickname(),
                              dto.password());
        p.setTimeAccountId(dto.timeAccountId());
        return p;
    }

    public static PersonDto toDto(Person p) {
        return new PersonDto(p.getId(),
                                 p.getNickname(),
                                 p.getPassword(),
                                 p.getTimeAccountId());
    }
}