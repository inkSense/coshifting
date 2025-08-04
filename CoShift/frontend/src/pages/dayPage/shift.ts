export interface ShiftPerson {
  id: number
  nickname: string
  role: string
}

export interface ShiftDetail {
  id: number
  startTime: string
  durationInMinutes: number
  capacity: number
  persons: ShiftPerson[]
}
