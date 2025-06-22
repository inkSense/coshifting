package org.coshift.b_application;

import org.coshift.a_domain.Person;
import org.coshift.a_domain.Shift;

import java.util.Objects;

/**
 * Use-Case: Fügt einer bestehenden Schicht eine Person hinzu.
 */
public class AddPersonToShiftUseCase {

    private final ShiftRepository  shiftRepo;
    private final PersonRepository personRepo;

    public AddPersonToShiftUseCase(ShiftRepository shiftRepo,
                                   PersonRepository personRepo) {
        this.shiftRepo  = Objects.requireNonNull(shiftRepo);
        this.personRepo = Objects.requireNonNull(personRepo);
    }

    /**
     * @param personId ID der Person
     * @param shiftId  ID der Schicht
     * @return die aktualisierte Schicht
     * @throws IllegalArgumentException wenn Person oder Schicht fehlen,
     *                                  die Schicht voll ist
     */
    public Shift add(long personId, long shiftId) {

        Shift  shift  = shiftRepo.findById(shiftId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Shift " + shiftId + " not found"));

        Person person = personRepo.findById(personId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Person " + personId + " not found"));

        /* --- fachliche Prüfungen ----------------------------------- */
        if (shift.isFull()) {
            throw new IllegalStateException("Shift " + shiftId + " is already full");
        }
        if (shift.getPersons().contains(person)) {
            // optional: ignorieren oder Fehler werfen
            return shift;                       // Person schon eingetragen
        }

        /* --- Aktion ------------------------------------------------- */
        shift.addPerson(person);

        /* --- Persistenz -------------------------------------------- */
        return shiftRepo.save(shift);           // aktualisierte Schicht zurück
    }
}