import { useQuery } from '@tanstack/react-query'
import { useAuth } from '../../auth/AuthContext'

export interface ShiftCellVM {
  startTime: string
  fullyStaffed: boolean
}

export interface DayCellViewModel {
  shifts: ShiftCellVM[]
}

async function fetchWeek(weeksToShow: number, header?: string): Promise<DayCellViewModel[]> {
  const res = await fetch(`/api/week?count=${weeksToShow}`, {
    headers: header ? { Authorization: header } : {},
  })
  if (!res.ok) throw new Error('Network response was not ok')
  return res.json()
}

export function useWeek(weeksToShow: number) {
  const { header } = useAuth()
  return useQuery({
    queryKey: ['week', weeksToShow],
    queryFn: () => fetchWeek(weeksToShow, header),
  })
}
