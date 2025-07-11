package org.coshift.c_adapters.persistence.json;

import org.coshift.a_domain.time.TimeAccount;
import org.coshift.b_application.ports.TimeAccountRepository;
import org.coshift.c_adapters.dto.TimeAccountDto;
import org.coshift.c_adapters.mapper.TimeAccountMapper;
import org.coshift.c_adapters.ports.TimeAccountJsonFileAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class TimeAccountJsonRepository implements TimeAccountRepository {

    private final TimeAccountJsonFileAccessor file;
    private final AtomicLong nextId = new AtomicLong(1);

    public TimeAccountJsonRepository(TimeAccountJsonFileAccessor file) {
        this.file = file;
        file.readAll().stream()
                .map(TimeAccountMapper::toDomain)
                .map(TimeAccount::getId)
                .max(Long::compare)
                .ifPresent(nextId::set);
    }

    @Override
    public TimeAccount save(TimeAccount acc) {
        long id = acc.getId() == 0 ? nextId.getAndIncrement() : acc.getId();
        TimeAccount withId = new TimeAccount(id, acc.getBalance());
        withId.getTransactions().addAll(acc.getTransactions());
        withId.refreshBalance(acc.getBalance().getPointInTime());

        List<TimeAccountDto> all = new ArrayList<>(file.readAll());
        all.removeIf(dto -> dto.id() == id);
        all.add(TimeAccountMapper.toDto(withId));
        if (!file.writeAll(all)) {
            throw new IllegalStateException("Unable to persist time-accounts JSON file");
        }
        return withId;
    }

    @Override
    public Optional<TimeAccount> findById(Long id) {
        return file.readAll().stream()
                .map(TimeAccountMapper::toDomain)
                .filter(acc -> acc.getId() == id)
                .findFirst();
    }

    @Override
    public List<TimeAccount> findAll() {
        return file.readAll().stream()
                .map(TimeAccountMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        List<TimeAccountDto> all = new ArrayList<>(file.readAll());
        all.removeIf(dto -> dto.id() == id);
        if (!file.writeAll(all)) {
            throw new IllegalStateException("Unable to persist time-accounts JSON file");
        }
    }
}