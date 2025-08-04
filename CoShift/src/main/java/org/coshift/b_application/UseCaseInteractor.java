package org.coshift.b_application;

import org.coshift.a_domain.Shift;
import org.coshift.a_domain.person.Person;
import org.coshift.a_domain.person.PersonRole;
import org.coshift.a_domain.time.TimeAccount;
import org.coshift.b_application.ports.PersonRepository;
import org.coshift.b_application.ports.ShiftRepository;
import org.coshift.b_application.ports.TimeAccountRepository;
import org.coshift.b_application.ports.PresenterInputPort;
import org.coshift.b_application.ports.PasswordChecker;
import org.coshift.b_application.useCases.*;

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
    private final ConfigurePersonUseCase configurePersonUC;
    private final ConfigurePersonsInShiftUseCase configurePersonsInShiftUseCase;
    private final PresenterInputPort presenter;
    private final AuthenticateUserUseCase authenticateUserUC;
    private final ViewTimeAccountUseCase viewTimeAccountUC;

    public UseCaseInteractor(
            ShiftRepository shiftRepository,
            PersonRepository personRepository,
            TimeAccountRepository timeAccountRepository,
            PasswordChecker passwordChecker,
            PresenterInputPort presenter
    ) {
        // Alle Use-Cases teilen sich dasselbe Repository (Falls erwünscht)
        this.addShiftUC  = new AddShiftUseCase(shiftRepository);
        this.viewShiftUC = new ViewShiftUseCase(shiftRepository);
        this.configurePersonUC = new ConfigurePersonUseCase(personRepository, timeAccountRepository);
        this.configurePersonsInShiftUseCase = new ConfigurePersonsInShiftUseCase(shiftRepository, personRepository);
        this.authenticateUserUC = new AuthenticateUserUseCase(personRepository, passwordChecker);
        this.viewTimeAccountUC = new ViewTimeAccountUseCase(timeAccountRepository, personRepository);
        this.presenter = presenter;
    }

    /* ---- Person ---- */

    public Person addPerson(String nickname, String password) {
        return configurePersonUC.add(nickname, password, PersonRole.USER);
    }

    public Person addPerson(String nickname, String password, PersonRole role) {
        return configurePersonUC.add(nickname, password, role);
    }

    public Person updatePerson(long id, String nick, String pw, PersonRole role){
        return configurePersonUC.update(id, nick, pw, role);
    }

    public void deletePerson(long id) {
        configurePersonUC.delete(id);
    }

    public Person updatePersonRole(long id, PersonRole role) {
        return configurePersonUC.update(id, role);
    }

    public Person authenticateUser(String nickname, String password) {
        return authenticateUserUC.authenticate(nickname, password);
    }


    /* ---- Shift ---- */

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


    /* ---- Person AND Shift ---- */

    public Shift addPersonToShift(long personId, long shiftId) {
        return configurePersonsInShiftUseCase.add(personId, shiftId);
    }

    public Shift removePersonFromShift(long personId, long shiftId){
        return configurePersonsInShiftUseCase.remove(personId, shiftId);
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


    /* ---- Time Account ---- */

    public TimeAccount getTimeAccount(long personId) {
        return viewTimeAccountUC.getByPersonId(personId);
    }


}
