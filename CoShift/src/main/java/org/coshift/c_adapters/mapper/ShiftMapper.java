package org.coshift.c_adapters.mapper;

import org.coshift.a_domain.Shift;
import org.coshift.c_adapters.dto.PersonDto;
import org.coshift.c_adapters.dto.ShiftDto;

import java.util.stream.Collectors;
import java.util.Collections;

public final class ShiftMapper {

    private ShiftMapper() {  } // undermines instantiation

    public static Shift toDomain(ShiftDto dto) {
        var persons = dto.persons() == null
                ? Collections.<PersonDto>emptyList()
                : dto.persons();

        return new Shift(dto.id(),
                         dto.startTime(),
                         dto.durationInMinutes(),
                         dto.capacity(),
                         persons.stream()
                                .map(PersonMapper::toDomain)
                                .collect(Collectors.toList()));
    }

    public static ShiftDto toDto(Shift shift) {
        return new ShiftDto(shift.getId(),
                            shift.getStartTime(),
                            (int) shift.getDurationInMinutes(),
                            shift.getCapacity(),
                            shift.getPersons().stream()
                                 .map(PersonMapper::toDto)
                                 .collect(Collectors.toList()));
    }
}





