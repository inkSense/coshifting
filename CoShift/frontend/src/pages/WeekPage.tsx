import DayCell from '../features/week/components/DayCell'
import { Box } from '@mui/material'
import { useApi } from '../api'
import { useQuery } from '@tanstack/react-query'
import type { DayCellViewModel } from '../features/week/types'

export default function WeekPage() {
  const api = useApi()
  const weeksToShow = 3
  const EXPECTED = weeksToShow * 7

  const { data: cells = [] } = useQuery({
    queryKey: ['week', weeksToShow],
    queryFn: () => api.get<DayCellViewModel[]>(`/api/week?count=${weeksToShow}`),
  })

  // Kopfzeile
  const days = ['Mo', 'Di', 'Mi', 'Do', 'Fr', 'Sa', 'So']

  // Fallback: solange noch keine Daten da sind, leere Zellen anzeigen
  const empty: DayCellViewModel = { shifts: [] }
  const display = cells.length === EXPECTED
    ? cells
    : Array.from({ length: EXPECTED }, () => empty)

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
