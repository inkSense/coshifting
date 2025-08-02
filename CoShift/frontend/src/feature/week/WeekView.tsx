import DayCell from './DayCell.tsx'
import { Box } from '@mui/material'
import { useWeek, type DayCellViewModel } from './hooks/useWeek'

export default function WeekView() {
  const weeksToShow = 3
  const EXPECTED = weeksToShow * 7

  const { data: cells = [], isLoading, isError } = useWeek(weeksToShow)

  if (isLoading) {
    return <Box>Loading...</Box>
  }

  if (isError) {
    return <Box>Error loading week data</Box>
  }

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