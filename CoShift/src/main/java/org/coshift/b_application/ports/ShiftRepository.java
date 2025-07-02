package org.coshift.b_application.ports;

import org.coshift.a_domain.Shift;
import java.time.LocalDate;
import java.util.*;

public interface ShiftRepository {

    Shift save(Shift shift);

    Optional<Shift> findById(Long id);

    List<Shift> findAll();

    List<Shift> findByDate(LocalDate date);

    List<Shift> findByDate(LocalDate startDate, LocalDate endDate);

    void deleteById(Long id);
}
