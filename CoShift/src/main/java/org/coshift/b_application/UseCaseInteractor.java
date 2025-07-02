package org.coshift.b_application;

import org.coshift.a_domain.Person;
import org.coshift.a_domain.Shift;
import org.coshift.b_application.ports.PersonRepository;
import org.coshift.b_application.ports.ShiftRepository;
import org.coshift.b_application.ports.UseCasesOutputPort;
import org.coshift.b_application.useCases.AddPersonToShiftUseCase;
import org.coshift.b_application.useCases.AddPersonUseCase;
import org.coshift.b_application.useCases.AddShiftUseCase;
import org.coshift.b_application.useCases.ViewShiftUseCase;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


/**
 * Fassade / Interactor, die sämtliche Shift-bezogenen Use-Cases bündelt.
 *
 *  – Keine eigene Business-Logik<br>
 *  – Delegiert an die spezialisierten Use-Cases
 */

@Component
public class UseCaseInteractor {

    private final AddShiftUseCase addShiftUC;
    private final ViewShiftUseCase viewShiftUC;
    private final AddPersonUseCase addPersonUC;
    private final AddPersonToShiftUseCase addPersonToShiftUC;
    private final UseCasesOutputPort presenter;

    public UseCaseInteractor(ShiftRepository repository, PersonRepository personRepository, UseCasesOutputPort presenter) {
        // Alle Use-Cases teilen sich dasselbe Repository (Falls erwünscht)
        this.addShiftUC  = new AddShiftUseCase(repository);
        this.viewShiftUC = new ViewShiftUseCase(repository);
        this.addPersonUC = new AddPersonUseCase(personRepository);
        this.addPersonToShiftUC = new AddPersonToShiftUseCase(repository, personRepository);
        this.presenter = presenter;
    }

    /* ------------ Delegierte Methoden ---------------- */

    public Shift addShift(LocalDateTime startTime,
                          int durationInMinutes,
                          int capacity)  {
        return addShiftUC.add(startTime, durationInMinutes, capacity);
    }

    public List<Shift> getAllShifts() {
        return viewShiftUC.getShifts();
    }

    public List<Shift> getShiftsBetween(LocalDate start, LocalDate end) {
        return viewShiftUC.getShiftsBetween(start, end);
    }

    public Person addPerson(String nickname, String password) {
        return addPersonUC.add(nickname, password);
    }

    public Shift addPersonToShift(long personId, long shiftId) {
        return addPersonToShiftUC.add(personId, shiftId);
    }

    public void showCurrentWeek(LocalDate monday) {
        List<Shift> shifts = viewShiftUC.getShiftsBetween(monday, monday.plusDays(6));
        presenter.showShiftsInThisWeek(shifts);
    }

}
