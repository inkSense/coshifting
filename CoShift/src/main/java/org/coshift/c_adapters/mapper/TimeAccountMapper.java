package org.coshift.c_adapters.mapper;

import org.coshift.a_domain.time.TimeAccount;
import org.coshift.a_domain.time.TimeBalance;
import org.coshift.a_domain.time.TimeTransaction;
import org.coshift.c_adapters.dto.TimeAccountDto;
import org.coshift.c_adapters.dto.TimeTransactionDto;
import java.util.List;

public final class TimeAccountMapper {
    private TimeAccountMapper() {}

    public static TimeAccount toDomain(TimeAccountDto dto) {
        TimeAccount acc = new TimeAccount(dto.id(), new TimeBalance(dto.balanceInMinutes(), dto.balanceTimestamp()));
        dto.transactions().forEach(t ->
            acc.addTransaction(new TimeTransaction(
                    t.amountInMinutes(),
                    t.pointInTime()))
        );
        acc.getBalance().setAmountInMinutes(dto.balanceInMinutes());
        acc.getBalance().setPointInTime(dto.balanceTimestamp());
        return acc;
    }

    public static TimeAccountDto toDto(TimeAccount acc) {
        List<TimeTransactionDto> tx =
            acc.getTransactions().stream()
               .map(t -> new TimeTransactionDto(
                          t.getAmountInMinutes(),
                          t.getPointInTime()))
               .toList();
        return new TimeAccountDto(
                acc.getId(),
                acc.getBalance().getAmountInMinutes(),
                acc.getBalance().getPointInTime(),
                tx);
    }
}