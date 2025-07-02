package org.coshift.b_application;
import org.coshift.a_domain.Shift;
import java.util.List;  

public interface UseCasesOutputPort {
    void showShiftsInThisWeek(List<Shift> shifts);
}
