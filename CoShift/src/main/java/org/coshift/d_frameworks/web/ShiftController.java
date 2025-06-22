package org.coshift.d_frameworks.web;

import org.coshift.b_application.UseCaseInteractor;
import org.coshift.c_adapters.ShiftDto;
import org.coshift.c_adapters.ShiftMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller exposing shift information.
 */
@RestController
@RequestMapping("/api/shifts")
class ShiftController {

    private final UseCaseInteractor interactor;

    ShiftController(UseCaseInteractor interactor) {
        this.interactor = interactor;
    }

    @GetMapping
    public List<ShiftDto> all() {
        return interactor.getAllShifts().stream()
                .map(ShiftMapper::toDto)
                .toList();
    }
}
