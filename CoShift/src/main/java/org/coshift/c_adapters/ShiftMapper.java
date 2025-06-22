package org.coshift.c_adapters;

import org.coshift.a_domain.Shift;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import java.util.Collections;

public final class ShiftMapper {

    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME; // ISO 8601 format

    private ShiftMapper() {  } // undermines instantiation

    public static Shift toDomain(ShiftDto dto) {
        LocalDateTime start = LocalDateTime.parse(dto.startTime(), ISO);

        var persons = dto.persons() == null
                ? Collections.<PersonDto>emptyList()
                : dto.persons();

        return new Shift(dto.id(),
                         start,
                         dto.durationInMinutes(),
                         dto.capacity(),
                         persons.stream()
                                .map(PersonMapper::toDomain)
                                .collect(Collectors.toList()));
    }

    public static ShiftDto toDto(Shift shift) {
        String start = ISO.format(shift.getStartTime());
        return new ShiftDto(shift.getId(),
                            start,
                            (int) shift.getDurationInMinutes(),
                            shift.getCapacity(),
                            shift.getPersons().stream()
                                 .map(PersonMapper::toDto)
                                 .collect(Collectors.toList()));
    }
}





