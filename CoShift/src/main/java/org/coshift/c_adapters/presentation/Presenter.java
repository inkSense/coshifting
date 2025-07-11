package org.coshift.c_adapters.presentation;

import org.coshift.c_adapters.dto.ShiftDto;
import org.coshift.c_adapters.mapper.ShiftMapper;
import org.springframework.stereotype.Component;
import org.coshift.b_application.ports.PresenterInputPort;
import java.util.List;
import java.util.ArrayList;
import org.coshift.a_domain.Shift;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

@Component
public class Presenter implements PresenterInputPort {
    private List<DayCellViewModel> currentWeek;
    private WeekView weekView; 
    // ToDo: WeekView ist komisch. 
    // Das Interface sollte besser ViewModel heißen. 
    // Die konkretet Implementierung sollte dann WeekViewModel heißen.
    // Presenter sollte nicht abhängig von der konkreten Implementierung sein.
    // Oder gibt es einen Presenter pro View? Scheint mir gerade überdimensioniert.

    public Presenter(WeekView weekView) {
        this.weekView = weekView;
    }

    @Override
    public void showShiftsInThisWeek(List<Shift> shifts) {
        List<ShiftDto> shiftDtos = mapShifts(shifts);
        currentWeek = createDayCells(shiftDtos);
        weekView.render(currentWeek);
    }

    @Override
    public void showShifts(LocalDate monday, int weeks, List<Shift> shifts) {
        int totalDays = weeks * 7;
        List<DayCellViewModel> cells = new ArrayList<>(totalDays);
        for (int i=0;i<totalDays;i++) cells.add(new DayCellViewModel(new ArrayList<>()));

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm");

        for (Shift s : shifts) {
            int idx = (int) ChronoUnit.DAYS.between(monday, s.getStartTime().toLocalDate());
            if (idx < 0 || idx >= totalDays) continue;

            boolean fullyStaffed = s.getPersons().size() >= s.getCapacity();
            cells.get(idx).shifts().add(new ShiftCellViewModel(fmt.format(s.getStartTime()), fullyStaffed));
        }

        currentWeek = cells;
        weekView.render(cells);
    }

    private List<ShiftDto> mapShifts(List<Shift> shifts) {
        return shifts.stream()
            .map(ShiftMapper::toDto)
            .collect(Collectors.toList());
    }


    private List<DayCellViewModel> createDayCells(List<ShiftDto> dtos) {
        // leere Woche vorbereiten
        List<DayCellViewModel> cells = new ArrayList<>(7);
        for (int i=0;i<7;i++) cells.add(new DayCellViewModel(new ArrayList<>()));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        //DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME; // ISO 8601 format

        for (ShiftDto dto : dtos) {
            LocalDateTime start = dto.startTime();
            int dayIdx = (start.getDayOfWeek().getValue() + 6) % 7;
            boolean fullyStaffed = dto.personIds().size() >= dto.capacity();
            cells.get(dayIdx).shifts().add(
                new ShiftCellViewModel(formatter.format(start), fullyStaffed));
        }
        return cells;
    }
}



