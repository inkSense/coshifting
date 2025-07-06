package org.coshift.b_application.ports;
import org.coshift.a_domain.Shift;
import java.util.List;  

public interface PresenterInputPort {
    void showShiftsInThisWeek(List<Shift> shifts);
}
