package org.coshift.c_adapters.web;

import org.coshift.c_adapters.presentation.DayCellViewModel;
import org.coshift.c_adapters.presentation.WeekViewModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import org.coshift.b_application.UseCaseInteractor;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/week")
public class WeekViewController {
    private final UseCaseInteractor interactor;
    private final WeekViewModel weekViewModel;

    public WeekViewController(UseCaseInteractor interactor, WeekViewModel weekViewModel) {
        this.interactor = interactor;
        this.weekViewModel = weekViewModel;
    }

    @GetMapping
    public List<DayCellViewModel> currentWeek() {
        // Montag dieser Woche bestimmen
        LocalDate today  = LocalDate.now();
        LocalDate monday = today.minusDays((today.getDayOfWeek().getValue() + 6) % 7);
 
        interactor.showCurrentWeek(monday);   // ruft Presenter → render(...)
        return weekViewModel.getCurrentWeek();                   // enthält jetzt aktuelle Daten
    }
}
