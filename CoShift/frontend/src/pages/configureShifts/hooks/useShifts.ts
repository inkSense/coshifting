import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { useApi } from '../../api.ts'
import type { ShiftDto, NewShiftDto } from '../types/shift.ts'

export function useShifts() {
  const api = useApi()
  const qc = useQueryClient()

  const shifts = useQuery({
    queryKey: ['shifts'],
    queryFn: (): Promise<ShiftDto[]> => api.get('/api/shifts'),
  })

  const addShift = useMutation({
    mutationFn: (body: NewShiftDto) => api.post<ShiftDto>('/api/shifts', body),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['shifts'] }),
  })

  const updateShift = useMutation({
    mutationFn: (s: ShiftDto) => api.put<ShiftDto>(`/api/shifts/${s.id}`, s),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['shifts'] }),
  })

  const deleteShift = useMutation({
    mutationFn: (id: number) => api.del(`/api/shifts/${id}`),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['shifts'] }),
  })

  return { shifts, addShift, updateShift, deleteShift }
}
