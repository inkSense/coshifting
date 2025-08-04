import { Box } from '@mui/material'
import { useNavigate } from 'react-router-dom'
import type { DayCellViewModel } from '../types/dayCellView.ts'
import ShiftBlock from '../../ShiftBlock.tsx'

export default function DayCell({ cell }: { cell: DayCellViewModel }) {
  const navigate = useNavigate()

  return (
    <Box
      sx={{ minHeight: '4rem', border: 1, borderColor: 'divider', cursor: 'pointer' }}
      onClick={() => cell.date && navigate(`/day/${cell.date}`)}
    >
      {cell.shifts.length === 0 && (
        <Box sx={{ opacity: 0.3 }}>&nbsp;</Box>
      )}

      {cell.shifts.map((s, i) => (
        <ShiftBlock key={i} filled={s.fullyStaffed} text={s.startTime} />
      ))}
    </Box>
  )
}