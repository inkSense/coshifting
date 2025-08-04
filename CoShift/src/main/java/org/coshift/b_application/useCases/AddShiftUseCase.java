package org.coshift.b_application.useCases;

import org.coshift.a_domain.Shift;
import org.coshift.b_application.ports.ShiftRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Anwendungsfall »Schicht anlegen«.
 *
 *  – Erstellt eine neue Schicht inkl. Plausibilitäts-Checks.<br>
 *  – Persistiert sie über {@link ShiftRepository}.<br>
 *  – Gibt die gespeicherte (inkl. ID versehene) Schicht zurück.
 */
public class AddShiftUseCase {

    // ToDo: Dies mit ViewShiftUseCase vereinigen

    private final ShiftRepository repository;

    public AddShiftUseCase(ShiftRepository repository) {
        this.repository = repository;
    }

    /**
     * Legt eine neue Schicht an.
     *
     * @param startTime          Beginn der Schicht
     * @param durationInMinutes  Länge in Minuten
     * @param capacity           maximale Teilnehmerzahl
     * @return die gespeicherte Schicht (mit vergebener ID)
     * @throws IllegalArgumentException falls es Terminüberschneidungen gibt
     */
    public Shift add(LocalDateTime startTime,
                     long durationInMinutes,
                     int capacity) {

        Shift candidate = new Shift(null, startTime, durationInMinutes, capacity);

        /* --- einfache Kollisionserkennung ---------------------------- */
        LocalDate day = startTime.toLocalDate();
        List<Shift> sameDay = repository.findByDate(day);

        boolean overlaps = sameDay.stream()
                                  .anyMatch(existing -> existing.overlaps(candidate));

        if (overlaps) {
            throw new IllegalArgumentException(
                    "Shift overlaps with an existing shift on " + day);
        }

        /* --- persistieren ------------------------------------------- */
        return repository.save(candidate);
    }
}