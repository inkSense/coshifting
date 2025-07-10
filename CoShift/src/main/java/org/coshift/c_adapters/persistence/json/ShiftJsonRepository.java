package org.coshift.c_adapters.persistence.json;

import org.coshift.a_domain.Shift;
import org.coshift.b_application.ports.ShiftRepository;
import org.coshift.b_application.ports.PersonRepository;
import org.coshift.c_adapters.dto.ShiftDto;
import org.coshift.c_adapters.mapper.ShiftMapper;
import org.coshift.c_adapters.ports.ShiftJsonFileAccessor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Primärer Adapter:
 *  – implementiert das Anwendungs-Port {@link ShiftRepository}. <br>
 *  – delegiert reinen Datei-Zugriff an {@link ShiftJsonFileAccessor}. <br>
 *  – enthält die Such- und ID-Vergabelogik. <br>
 *  – bleibt vollständig JSON-Bibliotheksfrei.
 */
@Component
public class ShiftJsonRepository implements ShiftRepository {

    private final ShiftJsonFileAccessor fileAccessor;
    private final PersonRepository personRepo;
    private final AtomicLong nextId = new AtomicLong(1); // AtomicLong for thread safety

    public ShiftJsonRepository(ShiftJsonFileAccessor fileAccessor,
                               PersonRepository personRepo) {
        this.fileAccessor = fileAccessor;
        this.personRepo  = personRepo;
        // initial max-Id bestimmen
        fileAccessor.readAll().stream()
                .map(dto -> ShiftMapper.toDomain(dto, personRepo))
                .map(Shift::getId)
                .filter(Objects::nonNull)
                .mapToLong(Long::longValue)
                .max()
                .ifPresent(nextId::set);
    }

    /* ------------------- Speichern -------------------- */

    @Override
    public Shift save(Shift shift) {
        Long id = Optional.ofNullable(shift.getId())
                          .orElseGet(nextId::getAndIncrement);

        /* ———  neuer Aufruf: Personenliste wird mitgegeben  ——— */
        Shift withId = new Shift(id,
                                 shift.getStartTime(),
                                 shift.getDurationInMinutes(),
                                 shift.getCapacity(),
                                 shift.getPersons());          

        List<ShiftDto> dtos = new ArrayList<>(fileAccessor.readAll());
        dtos.removeIf(dto -> Objects.equals(dto.id(), id));
        dtos.add(ShiftMapper.toDto(withId));

        if (!fileAccessor.writeAll(dtos)) {
            throw new IllegalStateException("Unable to persist shifts JSON file.");
        }
        return withId;
    }

    /* ------------------- Lesen ------------------------ */

    @Override
    public Optional<Shift> findById(Long id) {
        return fileAccessor.readAll().stream()
                .filter(dto -> Objects.equals(dto.id(), id))
                .findFirst()
                .map(dto -> ShiftMapper.toDomain(dto, personRepo));
    }

    @Override
    public List<Shift> findAll() {
        return fileAccessor.readAll().stream()
                .map(dto -> ShiftMapper.toDomain(dto, personRepo))
                .collect(Collectors.toList());
    }

    @Override
    public List<Shift> findByDate(LocalDate date) {
        return fileAccessor.readAll().stream()
                .map(dto -> ShiftMapper.toDomain(dto, personRepo))
                .filter(s -> s.getStartTime().toLocalDate().equals(date))
                .collect(Collectors.toList());
    }

    @Override
    public List<Shift> findByDate(LocalDate start, LocalDate end) {
        return fileAccessor.readAll().stream()
                .map(dto -> ShiftMapper.toDomain(dto, personRepo))
                .filter(s -> {
                    LocalDate d = s.getStartTime().toLocalDate();
                    return (d.isEqual(start) || d.isAfter(start))
                        && (d.isEqual(end)   || d.isBefore(end));
                })
                .collect(Collectors.toList());
    }

    /* ------------------- Löschen ---------------------- */

    @Override
    public void deleteById(Long id) {
        List<ShiftDto> dtos = new ArrayList<>(fileAccessor.readAll());
        boolean removed = dtos.removeIf(dto -> Objects.equals(dto.id(), id));
        if (removed && !fileAccessor.writeAll(dtos)) {
            throw new IllegalStateException("Unable to persist shifts JSON file.");
        }
    }
}