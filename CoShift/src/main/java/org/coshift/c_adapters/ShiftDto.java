package org.coshift.c_adapters;

import java.util.List;


public record ShiftDto(Long   id,
                           String startTime,          // ISO-8601, z. B. „2025-06-22T14:00“
                           int    durationInMinutes,
                           int    capacity,
                           List<PersonDto> persons) {
}