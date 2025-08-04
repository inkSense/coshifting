package org.coshift.b_application.useCases;

import org.coshift.a_domain.Shift;
import org.coshift.a_domain.person.Person;
import org.coshift.b_application.ports.PersonRepository;
import org.coshift.b_application.ports.ShiftRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Use-Case: Verwalte bestehende Schicht
 */
public class ConfigurePersonsInShiftUseCase {

    private final ShiftRepository shiftRepo;
    private final PersonRepository personRepo;
    private final Logger LOGGER = LoggerFactory.getLogger(ConfigurePersonsInShiftUseCase.class);

    public ConfigurePersonsInShiftUseCase(ShiftRepository shiftRepo,
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

        shift.addPerson(person);

        return shiftRepo.save(shift);           // aktualisierte Schicht zurück
    }



    public Shift remove(long personId, long shiftId) {
        /* --- Get Data ---------------------------------------------- */
        Shift shift  = shiftRepo.findById(shiftId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Shift " + shiftId + " not found"));
        Person person = personRepo.findById(personId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Person " + personId + " not found"));

        /* --- Check domain logic  ------------------------------------- */
        if(shift.getPersons().isEmpty()) {
            LOGGER.error("Shift " + shiftId + " is empty. Nothing to remove");
            return shift;
        }

        if(!shift.getPersons().contains(person)) {
            LOGGER.error("Person {} is not in shift {}. Nothing to remove.", personId, shiftId);
            return shift;
        }

        /* --- everything allright ----------------------------------------- */
        shift.removePerson(person);
        return shiftRepo.save(shift);
    }
}