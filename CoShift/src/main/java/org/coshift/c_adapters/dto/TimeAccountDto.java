package org.coshift.c_adapters.dto;

import java.util.List;
import java.time.LocalDateTime;

public record TimeAccountDto(
        long                   id,
        long                   balanceInMinutes,
        LocalDateTime          balanceTimestamp,
        List<TimeTransactionDto> transactions) {}



