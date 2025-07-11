package org.coshift.c_adapters.presentation;

import java.util.List;

/**
 * Container für alle Schichten eines Tages.
 */
public record DayCellViewModel(List<ShiftCellViewModel> shifts) {}

/**
 * Einzelne Schicht-Darstellung innerhalb eines Tages.
 */
record ShiftCellViewModel(String startTime, boolean fullyStaffed) {}
