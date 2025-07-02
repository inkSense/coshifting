package org.coshift.c_adapters.web;

import org.coshift.b_application.UseCaseInteractor;
import org.coshift.c_adapters.dto.ShiftDto;
import org.coshift.c_adapters.mapper.ShiftMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller exposing shift information.
 */
@RestController
@RequestMapping("/api/shifts")
public class ShiftController {

    private final UseCaseInteractor interactor;

    public ShiftController(UseCaseInteractor interactor) {
        this.interactor = interactor;
    }

    @GetMapping
    public List<ShiftDto> all() {
        return interactor.getAllShifts().stream()
                .map(ShiftMapper::toDto)
                .toList();
    }
}
