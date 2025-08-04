import { Box, Button } from '@mui/material'
import { useParams } from 'react-router-dom'
import { useApi } from '../api.ts'
import { useQuery, useQueryClient } from '@tanstack/react-query'
import ShiftBlock from '../ShiftBlock.tsx'
import type { ShiftDetail } from './shift.ts'
import { useAuth } from '../../features/auth/AuthProvider.tsx'

export default function DayPage() {
    const { date = '' } = useParams<{ date: string }>()
    const api = useApi()
    const { personId } = useAuth()
    const queryClient = useQueryClient()

    const { data: shifts = [] } = useQuery({
        queryKey: ['day', date],
        queryFn: () => api.get<ShiftDetail[]>(`/api/shifts/day?date=${date}`),
        enabled: !!date,
    })

    const toggleParticipation = async (shiftId: number, isParticipant: boolean) => {
        if (personId == null) return
        const path = `/api/shifts/${shiftId}/participation`
        const updated = isParticipant
            ? await api.del<ShiftDetail>(path, personId)
            : await api.put<ShiftDetail>(path, personId)

        queryClient.setQueryData<ShiftDetail[]>(['day', date], old =>
            (old ?? []).map(s => (s.id === shiftId ? updated : s))
        )
    }

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
                    const isParticipant = personId != null && shift.persons.some(p => p.id === personId)
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
                            }}
                        >
                            <Button
                                variant="contained"
                                size="small"
                                onClick={() => toggleParticipation(shift.id, isParticipant)}
                            >
                                {isParticipant ? 'austragen' : 'eintragen'}
                            </Button>
                        </ShiftBlock>
                    )
                })}
            </Box>
        </Box>
    )
}