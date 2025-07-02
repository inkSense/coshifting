package org.coshift.c_adapters;

import org.springframework.stereotype.Component;
import org.coshift.b_application.UseCasesOutputPort;
import java.util.List;
import java.util.ArrayList;
import org.coshift.a_domain.Shift;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.context.annotation.Lazy;

@Component
public class Presenter implements UseCasesOutputPort {
    private List<DayCellViewModel> currentWeek;
    private WeekView weekView;

    public Presenter(@Lazy WeekView weekView) {
        this.weekView = weekView;
    }

    @Override
    public void showShiftsInThisWeek(List<Shift> shifts) {
        List<ShiftDto> shiftDtos = mapShifts(shifts);
        currentWeek = createDayCells(shiftDtos);
        weekView.render(currentWeek);
    }

    private List<ShiftDto> mapShifts(List<Shift> shifts) {
        return shifts.stream()
            .map(ShiftMapper::toDto)
            .collect(Collectors.toList());
    }


    private List<DayCellViewModel> createDayCells(List<ShiftDto> dtos) {
        // leere Woche vorbereiten
        List<DayCellViewModel> cells = new ArrayList<>(7);
        for (int i = 0; i < 7; i++) {
            cells.add(new DayCellViewModel(false, "", false));
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        //DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME; // ISO 8601 format

        for (ShiftDto dto : dtos) {
            LocalDateTime start = dto.startTime();
            boolean hasShift = dto.persons().size() > 0;
            int dayIdx = (start.getDayOfWeek().getValue() + 6) % 7; // 0=Mo … 6=So
            boolean fullyStaffed = dto.persons().size() >= dto.capacity();
            cells.set(dayIdx,
                      new DayCellViewModel(true,
                                           formatter.format(start),
                                           fullyStaffed));
        }
        return cells;
    }
}
