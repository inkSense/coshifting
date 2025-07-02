package org.coshift.c_adapters;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.coshift.b_application.UseCaseInteractor;
import java.time.LocalDate;
import org.springframework.context.annotation.Lazy;

@RestController
@RequestMapping("/api/week")
public class WeekViewController implements WeekView {
    private final UseCaseInteractor interactor;

    private final AtomicReference<List<DayCellViewModel>> cache =
            new AtomicReference<>(List.of());

    public WeekViewController(@Lazy UseCaseInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void render(List<DayCellViewModel> cells) {
        cache.set(cells);            // Presenter liefert hier die Daten ab
    }

    @GetMapping
    public List<DayCellViewModel> currentWeek() {
        // Montag dieser Woche bestimmen
        LocalDate today  = LocalDate.now();
        LocalDate monday = today.minusDays((today.getDayOfWeek().getValue() + 6) % 7);
 
        interactor.showCurrentWeek(monday);   // ruft Presenter → render(...)
        return cache.get();                   // enthält jetzt aktuelle Daten
    }
}
