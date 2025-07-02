package org.coshift.c_adapters.presentation;

public record DayCellViewModel (
    boolean hasShift,
    String startTime,
    boolean fullyStaffed
) {}
