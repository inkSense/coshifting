package org.coshift.b_application.useCases;

import org.coshift.a_domain.Shift;
import org.coshift.b_application.ports.ShiftRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Use case for creating, updating and deleting shifts.
 *
 *  – Creates a new shift including a simple overlap check.
 *  – Updates an existing shift with the same overlap check.
 *  – Persists via {@link ShiftRepository}.
 */
public class ConfigureShiftUseCase {

    private final ShiftRepository repository;

    public ConfigureShiftUseCase(ShiftRepository repository) {
        this.repository = repository;
    }

    /**
     * Creates a new shift.
     *
     * @param startTime         start of the shift
     * @param durationInMinutes length in minutes
     * @param capacity          maximum number of participants
     * @return persisted shift with generated id
     * @throws IllegalArgumentException if there are overlapping shifts
     */
    public Shift add(LocalDateTime startTime,
                     long durationInMinutes,
                     int capacity) {

        Shift candidate = new Shift(null, startTime, durationInMinutes, capacity);
        ensureNoOverlap(candidate);
        return repository.save(candidate);
    }


    public List<Shift> getShifts() {
        return repository.findAll();
    }

    public List<Shift> getShiftsBetween(LocalDate startDate, LocalDate endDate) {
        return repository.findByDate(startDate, endDate);
    }

    /**
     * Updates an existing shift.
     *
     * @param id                id of the shift
     * @param startTime         new start time
     * @param durationInMinutes new duration
     * @param capacity          new capacity
     * @return persisted updated shift
     * @throws IllegalArgumentException if there are overlapping shifts
     */
    public Shift update(long id,
                        LocalDateTime startTime,
                        long durationInMinutes,
                        int capacity) {

        Shift candidate = new Shift(id, startTime, durationInMinutes, capacity);
        ensureNoOverlap(candidate, id);
        return repository.save(candidate);
    }

    /**
     * Deletes a shift by id.
     */
    public void delete(long id) {
        repository.deleteById(id);
    }

    private void ensureNoOverlap(Shift candidate) {
        ensureNoOverlap(candidate, null);
    }

    private void ensureNoOverlap(Shift candidate, Long ignoreId) {
        LocalDate day = candidate.getStartTime().toLocalDate();
        List<Shift> sameDay = repository.findByDate(day);
        boolean overlaps = sameDay.stream()
                .filter(existing -> ignoreId == null || !ignoreId.equals(existing.getId()))
                .anyMatch(existing -> existing.overlaps(candidate));
        if (overlaps) {
            throw new IllegalArgumentException(
                    "Shift overlaps with an existing shift on " + day);
        }
    }
}
