package org.coshift.c_adapters.dto;

import java.time.LocalDateTime;
import java.util.List;


public record ShiftDto(
    Long   id,
    LocalDateTime startTime,          // ISO-8601, z. B. „2025-06-22T14:00“
    int    durationInMinutes,
    int    capacity,
    List<Long> personIds                  // geändert: nur noch IDs statt PersonDto
) 
{}