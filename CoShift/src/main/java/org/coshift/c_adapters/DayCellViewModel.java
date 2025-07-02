package org.coshift.c_adapters;

public record DayCellViewModel (
    boolean hasShift,
    String startTime,
    boolean fullyStaffed
) {}
