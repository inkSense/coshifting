package org.coshift.c_adapters.web;

import org.coshift.b_application.UseCaseInteractor;
import org.coshift.c_adapters.dto.ShiftPublicDetailDto;
import org.coshift.c_adapters.dto.ShiftSummeryDto;
import org.coshift.c_adapters.mapper.ShiftMapper;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * REST controller exposing shift information.
 * grundsätzlich: übersetzt HTTP-Aufrufe in Use-Case-Aufrufe
 * 
 */
@RestController
@RequestMapping("/api/shifts")
public class ShiftController {

    private final UseCaseInteractor interactor;

    public ShiftController(UseCaseInteractor interactor) {
        this.interactor = interactor;
    }

    @GetMapping
    public List<ShiftSummeryDto> all() {
        return interactor.getAllShifts().stream()
                .map(ShiftMapper::toSummeryDto)
                .toList();
    }

    public record ShiftDto(LocalDateTime startTime, long durationInMinutes, int capacity) {}

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ShiftSummeryDto create(@RequestBody ShiftDto dto) {
        var shift = interactor.addShift(dto.startTime(), dto.durationInMinutes(), dto.capacity());
        return ShiftMapper.toSummeryDto(shift);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ShiftSummeryDto update(@PathVariable long id, @RequestBody ShiftDto dto) {
        var shift = interactor.updateShift(id, dto.startTime(), dto.durationInMinutes(), dto.capacity());
        return ShiftMapper.toSummeryDto(shift);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable long id) {
        interactor.deleteShift(id);
    }

    @GetMapping("/day")
    public List<ShiftPublicDetailDto> getShiftsForDay(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return interactor.getShiftsBetween(date, date).stream()
                .map(ShiftMapper::toPublicDetailDto)
                .toList();
    }


    @PutMapping("/{id}/participation")
    public ShiftPublicDetailDto addUserToShift(@PathVariable long id, @RequestBody long personId){
        var shift = interactor.addPersonToShift(personId, id);
        return ShiftMapper.toPublicDetailDto(shift);
    }

    @DeleteMapping("/{id}/participation")
    public ShiftPublicDetailDto deleteUserFromShift(@PathVariable long id, @RequestBody long personId){
        var shift = interactor.removePersonFromShift(personId, id);
        return ShiftMapper.toPublicDetailDto(shift);
    }



}
