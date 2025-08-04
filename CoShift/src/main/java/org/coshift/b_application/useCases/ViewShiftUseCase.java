package org.coshift.b_application.useCases;

import org.coshift.a_domain.Shift;
import org.coshift.b_application.ports.ShiftRepository;
import java.util.List;
import java.time.LocalDate;

public class ViewShiftUseCase {
    /**
     * returns Information about shifts to the User Interface
     */
    // ToDo: Dies mit ViewShiftUseCase vereinigen
    private final ShiftRepository repository;

    public ViewShiftUseCase(ShiftRepository repository) {
        this.repository = repository;
    }

    public List<Shift> getShifts() {
        return repository.findAll();
    }
    
    public List<Shift> getShiftsBetween(LocalDate startDate, LocalDate endDate) {
        return repository.findByDate(startDate, endDate);
    }
    
    
}