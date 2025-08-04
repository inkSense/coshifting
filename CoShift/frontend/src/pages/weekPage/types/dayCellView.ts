export interface ShiftCellVM {
  startTime: string
  fullyStaffed: boolean
}

export interface DayCellViewModel {
  shifts: ShiftCellVM[]
}
