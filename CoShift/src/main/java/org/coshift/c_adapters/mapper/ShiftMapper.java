package org.coshift.c_adapters.mapper;

import org.coshift.a_domain.Shift;
import org.coshift.c_adapters.dto.ShiftDto;
import org.coshift.b_application.ports.PersonRepository;
import org.coshift.a_domain.person.Person;

import java.util.stream.Collectors;
import java.util.Collections;

public final class ShiftMapper {

    private ShiftMapper() {  } // undermines instantiation

    public static Shift toDomain(ShiftDto dto, PersonRepository repo) {
        var ids = dto.personIds() == null
                ? Collections.<Long>emptyList()
                : dto.personIds();

        var persons = ids.stream()
                         .map(repo::findById)
                         .flatMap(java.util.Optional::stream)
                         .collect(Collectors.toList());

        return new Shift(dto.id(),
                         dto.startTime(),
                         dto.durationInMinutes(),
                         dto.capacity(),
                         persons);
    }

    public static ShiftDto toDto(Shift shift) {
        return new ShiftDto(shift.getId(),
                            shift.getStartTime(),
                            shift.getDurationInMinutes(),
                            shift.getCapacity(),
                            shift.getPersons().stream()
                                 .map(Person::getId)
                                 .collect(Collectors.toList()));
    }
}





