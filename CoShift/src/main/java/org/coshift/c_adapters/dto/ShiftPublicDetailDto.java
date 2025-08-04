package org.coshift.c_adapters.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ShiftPublicDetailDto(
    long id,
    LocalDateTime startTime,
    long durationInMinutes,
    int capacity,
    List<PersonPublicDto> persons
) {
}
