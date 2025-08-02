import { useAuth } from '../auth/AuthProvider'
import DayCell from './DayCell.tsx'
import { useQuery } from '@tanstack/react-query'
import { authFetch } from '../../shared/api'
import { Box, CircularProgress, Typography } from '@mui/material'

export interface ShiftCellVM {
  startTime: string
  fullyStaffed: boolean
}

export interface DayCellViewModel {
  shifts: ShiftCellVM[]
}

export default function WeekView() {
  const { header } = useAuth()
  const weeksToShow = 3

  const { data, isLoading, error } = useQuery({
    queryKey: ['week', weeksToShow],
    enabled: !!header,
    queryFn: () => authFetch<DayCellViewModel[]>(`/api/week?count=${weeksToShow}`, header!),
  })

  const days = dayLabels

  if (isLoading) return <CircularProgress />
  if (error) return <Typography color="error">Fehler beim Laden</Typography>

  return (
    <Box sx={{ display: 'grid', gridTemplateColumns: 'repeat(7, 1fr)', gap: 2 }}>
      {days.map(day => (
        <Box key={day} sx={{ fontWeight: 'bold', p: 0.5, bgcolor: 'background.paper', border: 1, borderColor: 'divider', textAlign: 'center' }}>{day}</Box>
      ))}

      {data?.map((cell, idx) => (
        <DayCell key={idx} cell={cell} />
      ))}
    </Box>
  )
}

const dayLabels = ['Mo', 'Di', 'Mi', 'Do', 'Fr', 'Sa', 'So']