package org.coshift.c_adapters.mapper;

import org.coshift.a_domain.Shift;
import org.coshift.c_adapters.dto.ShiftPublicDetailDto;
import org.coshift.c_adapters.dto.ShiftSummeryDto;
import org.coshift.b_application.ports.PersonRepository;
import org.coshift.a_domain.person.Person;

import java.util.stream.Collectors;
import java.util.Collections;

public final class ShiftMapper {

    private ShiftMapper() {  } // undermines instantiation

    public static Shift toDomain(ShiftSummeryDto dto, PersonRepository repo) {
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

    public static ShiftSummeryDto toSummeryDto(Shift shift) {
        return new ShiftSummeryDto(shift.getId(),
                            shift.getStartTime(),
                            shift.getDurationInMinutes(),
                            shift.getCapacity(),
                            shift.getPersons().stream()
                                 .map(Person::getId)
                                 .collect(Collectors.toList()));
    }

    public static ShiftPublicDetailDto toPublicDetailDto(Shift shift) {
        return new ShiftPublicDetailDto(
                shift.getId(),
                shift.getStartTime(),
                shift.getDurationInMinutes(),
                shift.getCapacity(),
                shift.getPersons().stream()
                        .map(PersonMapper::toPublicDto)
                        .collect(Collectors.toList()));
    }


}





