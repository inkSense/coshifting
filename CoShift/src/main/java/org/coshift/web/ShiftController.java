package org.coshift.web;

import org.coshift.domain.Shift;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/shifts")
class ShiftController {

    @GetMapping
    public List<Shift> all() {
        // Walking-Skeleton: genau **eine** Demo-Schicht zurückgeben
        return List.of(
                new Shift(null,
                        LocalDate.now(),
                        LocalTime.of(18, 0),
                        LocalTime.of(21, 0))
        );
    }
}
