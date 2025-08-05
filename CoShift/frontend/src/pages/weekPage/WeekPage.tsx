import DayCell from './components/DayCell.tsx'
import { Box } from '@mui/material'
import { useApi } from '../api.ts'
import { useQuery } from '@tanstack/react-query'
import type { DayCellViewModel } from './types/dayCellView.ts'
import dayjs from 'dayjs'
import isoWeek from 'dayjs/plugin/isoWeek'
import { Fragment } from 'react'

dayjs.extend(isoWeek)

export default function WeekPage() {
  const api = useApi()
  const weeksToShow = 12
  const EXPECTED = weeksToShow * 7

  const { data: cells = [] } = useQuery({
    queryKey: ['week', weeksToShow],
    queryFn: () => api.get<DayCellViewModel[]>(`/api/week?count=${weeksToShow}`),
  })

  // Kopfzeile
  const days = ['Mo', 'Di', 'Mi', 'Do', 'Fr', 'Sa', 'So']
  const headerSx = {
    fontWeight: 'bold',
    p: 0.5,
    bgcolor: 'background.paper',
    border: 1,
    borderColor: 'divider',
    textAlign: 'center',
  } as const

  // Fallback: solange noch keine Daten da sind, leere Zellen anzeigen
  const empty: DayCellViewModel = { date: '', shifts: [] }
  const display = cells.length === EXPECTED
    ? cells
    : Array.from({ length: EXPECTED }, () => empty)

  const weeks = Array.from({ length: weeksToShow }, (_, i) =>
    display.slice(i * 7, (i + 1) * 7),
  )

  return (
    <Box
      sx={{
        display: 'grid',
        gridTemplateColumns: '4rem repeat(7, 1fr)',
        gap: 2,                       // theme.spacing(2) → 2*4px
      }}
    >
      <Box sx={headerSx}>KW</Box>
      {days.map(day => (
        <Box key={day} sx={headerSx}>
          {day}
        </Box>
      ))}
      {weeks.map((week, wIdx) => (
        <Fragment key={wIdx}>
          <Box sx={headerSx}>
            {week[0].date ? `KW ${dayjs(week[0].date).isoWeek()}` : ''}
          </Box>
          {week.map((cell, idx) => (
            <DayCell key={wIdx * 7 + idx} cell={cell} />
          ))}
        </Fragment>
      ))}
    </Box>
  )
}
