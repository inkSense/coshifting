export interface ShiftDto {
  id: number
  startTime: string
  durationInMinutes: number
  capacity: number
  personIds: number[]
}

export type NewShiftDto = Omit<ShiftDto, 'id' | 'personIds'>
