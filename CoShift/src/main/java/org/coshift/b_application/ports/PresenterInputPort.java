package org.coshift.b_application.ports;
import org.coshift.a_domain.Shift;
import java.time.LocalDate;
import java.util.List;  

public interface PresenterInputPort {
    void showShiftsInThisWeek(List<Shift> shifts);
    void showShifts(LocalDate monday, int weeks, List<Shift> shifts);
}
