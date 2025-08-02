import DayCell from './DayCell.tsx'
import { Box, CircularProgress } from '@mui/material'
import { useApi } from '../../api'
import { useQuery } from '@tanstack/react-query'

export interface ShiftCellVM {
  startTime: string
  fullyStaffed: boolean
}

export interface DayCellViewModel {
  shifts: ShiftCellVM[]
}

export default function WeekView() {
  const api = useApi()

  const { data: cells, isLoading } = useQuery({
    queryKey: ['week'],
    queryFn: () => api.get<DayCellViewModel[]>(`/api/week?count=1`),
  })

  // Kopfzeile
  const days = ['Mo', 'Di', 'Mi', 'Do', 'Fr', 'Sa', 'So']

  if (isLoading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}>
        <CircularProgress />
      </Box>
    )
  }

  const display = (cells ?? Array.from({ length: 7 }, () => ({ shifts: [] })) ) as DayCellViewModel[]

  return (
    <Box
      sx={{
        display: 'grid',
        gridTemplateColumns: 'repeat(7, 1fr)',
        gap: 2,                       // theme.spacing(2) → 2*4px
      }}
    >
      {days.map(day => (
        <Box
          key={day}
          sx={{
            fontWeight: 'bold',
            p: 0.5,
            bgcolor: 'background.paper',
            border: 1,
            borderColor: 'divider',
            textAlign: 'center',
          }}
        >
          {day}
        </Box>
      ))}
      {display.map((cell, idx) => (
        <DayCell key={idx} cell={cell} />
      ))}
    </Box>
  )
}
