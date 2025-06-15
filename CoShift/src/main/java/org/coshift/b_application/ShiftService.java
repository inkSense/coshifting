package org.coshift.b_application;

import org.coshift.a_domain.Shift;

import java.util.List;

/**
 * Use-Case-Klasse „Schichten anzeigen / verwalten“.
 * Enthält KEINE Spring-Annotationen – damit bleibt der Application-Ring
 * unabhängig von Frameworks.  Die Bean-Erzeugung erfolgt später außen.
 */
public class ShiftService {

    private final ShiftRepository repository;

    public ShiftService(ShiftRepository repository) {
        this.repository = repository;
    }

    /**
     * Liefert alle künftig bekannten Schichten.
     * (Derzeit keinerlei Filter – dein Test prüft nur die Delegation.)
     */
    public List<Shift> upcomingShifts() {
        return repository.findAll();
    }

    /* Raum für weitere Use-Cases – z. B.  */

    public Shift save(Shift shift) {
        return repository.save(shift);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
