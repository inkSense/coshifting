package org.coshift.c_adapters;

import org.coshift.c_adapters.presentation.DayCellViewModel;
import org.coshift.c_adapters.presentation.WeekViewModel;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class WeekViewModelTest {

    @Test
    void renderStoresCellsForLaterRetrieval() {
        WeekViewModel model = new WeekViewModel();
        List<DayCellViewModel> cells = List.of(new DayCellViewModel(LocalDate.now(), List.of()));

        model.render(cells);

        assertThat(model.getCurrentWeek()).isSameAs(cells);
    }
}
