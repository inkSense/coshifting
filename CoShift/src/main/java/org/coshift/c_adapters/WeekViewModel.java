package org.coshift.c_adapters;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class WeekViewModel implements WeekView {
    private final AtomicReference<List<DayCellViewModel>> cache = new AtomicReference<>(List.of());

    @Override
    public void render(List<DayCellViewModel> cells) {
        cache.set(cells);
    }

    public List<DayCellViewModel> getCurrentWeek() {
        return cache.get();
    }
}
