export interface ShiftCellVM {
  startTime: string
  fullyStaffed: boolean
}

export interface DayCellViewModel {
  date: string
  shifts: ShiftCellVM[]
}
