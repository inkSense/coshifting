package org.coshift.c_adapters;

import org.coshift.b_application.UseCaseInteractor;
import org.coshift.c_adapters.json.PersonJsonRepository;
import org.coshift.c_adapters.json.ShiftJsonRepository;
import org.coshift.d_frameworks.json.PersonGsonFileAccessor;
import org.coshift.d_frameworks.json.ShiftGsonFileAccessor;
import java.time.LocalDateTime;

public class ControlerFake {

    private final UseCaseInteractor interactor = new UseCaseInteractor(
            new ShiftJsonRepository(new ShiftGsonFileAccessor()),
            new PersonJsonRepository(new PersonGsonFileAccessor())
        );


    public static void main(String[] args) {
        var controler = new ControlerFake();
        //controler.addThreeUsers();
        //controler.addOneShift();
        controler.addPersonToShift(); 
    }

    public void addThreeUsers() {
        interactor.addPerson("Alice", "secret");
        interactor.addPerson("Bob", "pwd");
        interactor.addPerson("Charlie", "pwd");
    }

    public void addOneShift() {
        /* Beispiel: Schicht morgen, 18 Uhr, 120 min, Kapazität 10 */
        interactor.addShift(
                LocalDateTime.now().plusDays(1).withHour(18).withMinute(0).withSecond(0).withNano(0),
                120,
                10);
    }

    public void addPersonToShift() {
        // Nur ausführen, wenn die Person noch nicht drin ist:
        interactor.addPersonToShift(1, 1);
    }
}
