import { Box } from '@mui/material'
import { useParams } from 'react-router-dom'
import { useApi } from '../api.ts'
import { useQuery } from '@tanstack/react-query'
import ShiftBlock from '../ShiftBlock.tsx'
import type { ShiftDetail } from './shift.ts'

export default function DayPage() {
  const { date = '' } = useParams<{ date: string }>()
  const api = useApi()

  const { data: shifts = [] } = useQuery({
    queryKey: ['day', date],
    queryFn: () => api.get<ShiftDetail[]>(`/api/shifts/day?date=${date}`),
    enabled: !!date,
  })

  const startHour = 6
  const endHour = 22
  const hourHeight = 50
  const pxPerMinute = hourHeight / 60
  const totalHours = endHour - startHour
  const containerHeight = totalHours * hourHeight

  return (
    <Box sx={{ display: 'grid', gridTemplateColumns: '4rem 1fr' }}>
      <Box sx={{ position: 'relative', height: containerHeight }}>
        {Array.from({ length: totalHours + 1 }).map((_, i) => (
          <Box
            key={i}
            sx={{
              position: 'absolute',
              top: i * hourHeight,
              height: hourHeight,
              borderTop: 1,
              borderColor: 'divider',
              fontSize: '0.75rem',
            }}
          >
            {(startHour + i).toString().padStart(2, '0')}:00
          </Box>
        ))}
      </Box>
      <Box sx={{ position: 'relative', height: containerHeight, borderLeft: 1, borderColor: 'divider' }}>
        {Array.from({ length: totalHours + 1 }).map((_, i) => (
          <Box
            key={i}
            sx={{
              position: 'absolute',
              top: i * hourHeight,
              left: 0,
              right: 0,
              borderTop: 1,
              borderColor: 'divider',
            }}
          />
        ))}
        {shifts.map(shift => {
          const start = new Date(shift.startTime)
          const startMinutes = start.getHours() * 60 + start.getMinutes()
          const top = (startMinutes - startHour * 60) * pxPerMinute
          const height = shift.durationInMinutes * pxPerMinute
          const names = shift.persons.map(p => p.nickname).join(', ')
          return (
            <ShiftBlock
              key={shift.id}
              filled={shift.persons.length >= shift.capacity}
              text={names || 'frei'}
              sx={{
                position: 'absolute',
                top,
                height,
                left: 0,
                right: 0,
                overflow: 'hidden',
              }}
            />
          )
        })}
      </Box>
    </Box>
  )
}
