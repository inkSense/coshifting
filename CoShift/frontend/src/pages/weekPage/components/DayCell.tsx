import { Box } from '@mui/material'
import { useNavigate } from 'react-router-dom'
import type { DayCellViewModel } from '../types/dayCellView.ts'

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
        <Box
          key={i}
          sx={{
            bgcolor: s.fullyStaffed ? 'success.main' : 'grey.600',
            color: s.fullyStaffed ? 'common.white' : 'text.primary',
            fontWeight: s.fullyStaffed ? 600 : 'normal',
            borderRadius: 1,
            m: 0.5,
            p: 0.5,
            fontSize: '0.85rem',
          }}
        >
          {s.startTime}
        </Box>
      ))}
    </Box>
  )
}