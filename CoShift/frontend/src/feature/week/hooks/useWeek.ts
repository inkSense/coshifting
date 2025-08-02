import { useQuery } from '@tanstack/react-query'
import useApiClient from '../../../api/useApiClient'

export interface ShiftCellVM {
  startTime: string
  fullyStaffed: boolean
}

export interface DayCellViewModel {
  shifts: ShiftCellVM[]
}

export function useWeek(weeksToShow: number) {
  const { get } = useApiClient()
  return useQuery({
    queryKey: ['week', weeksToShow],
    queryFn: () => get<DayCellViewModel[]>(`/api/week?count=${weeksToShow}`),
  })
}
