package org.coshift.c_adapters.dto;

import java.time.LocalDateTime;

public record TimeTransactionDto(
long amountInMinutes,
LocalDateTime pointInTime) {}