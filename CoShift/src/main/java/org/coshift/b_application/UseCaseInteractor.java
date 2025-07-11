package org.coshift.b_application;

import org.coshift.a_domain.Shift;
import org.coshift.a_domain.person.Person;
import org.coshift.a_domain.time.TimeAccount;
import org.coshift.b_application.ports.PersonRepository;
import org.coshift.b_application.ports.ShiftRepository;
import org.coshift.b_application.ports.TimeAccountRepository;
import org.coshift.b_application.ports.PresenterInputPort;
import org.coshift.b_application.ports.PasswordChecker;
import org.coshift.b_application.useCases.AddPersonToShiftUseCase;
import org.coshift.b_application.useCases.AddPersonUseCase;
import org.coshift.b_application.useCases.AddShiftUseCase;
import org.coshift.b_application.useCases.AuthenticateUserUseCase;
import org.coshift.b_application.useCases.ViewShiftUseCase;
import org.coshift.b_application.useCases.ViewTimeAccountUseCase;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


/**
 * Fassade / Interactor, die sämtliche Shift-bezogenen Use-Cases bündelt.
 *
 *  – Keine eigene Business-Logik<br>
 *  – Delegiert an die spezialisierten Use-Cases
 */

public class UseCaseInteractor {

    private final AddShiftUseCase addShiftUC;
    private final ViewShiftUseCase viewShiftUC;
    private final AddPersonUseCase addPersonUC;
    private final AddPersonToShiftUseCase addPersonToShiftUC;
    private final PresenterInputPort presenter;
    private final AuthenticateUserUseCase authenticateUserUC;
    private final ViewTimeAccountUseCase viewTimeAccountUC;

    public UseCaseInteractor(ShiftRepository repository, PersonRepository personRepository, TimeAccountRepository timeAccountRepository, PasswordChecker passwordChecker, PresenterInputPort presenter) {
        // Alle Use-Cases teilen sich dasselbe Repository (Falls erwünscht)
        this.addShiftUC  = new AddShiftUseCase(repository);
        this.viewShiftUC = new ViewShiftUseCase(repository);
        this.addPersonUC = new AddPersonUseCase(personRepository);
        this.addPersonToShiftUC = new AddPersonToShiftUseCase(repository, personRepository);
        this.authenticateUserUC = new AuthenticateUserUseCase(personRepository, passwordChecker);
        this.viewTimeAccountUC = new ViewTimeAccountUseCase(timeAccountRepository, personRepository);
        this.presenter = presenter;
    }

    /* ------------ Delegierte Methoden ---------------- */

    public Shift addShift(LocalDateTime startTime,
                          long durationInMinutes,
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

    public void showWeeks(LocalDate monday, int weeks) {
        LocalDate end = monday.plusDays(weeks * 7L - 1);
        List<Shift> shifts = viewShiftUC.getShiftsBetween(monday, end);
        presenter.showShifts(monday, weeks, shifts);
    }

    public Person authenticateUser(String nickname, String password) {
        return authenticateUserUC.authenticate(nickname, password);
    }

    public TimeAccount getTimeAccount(long personId) {
        return viewTimeAccountUC.getByPersonId(personId);
    }
}
