import { Box } from '@mui/material'
import type { DayCellViewModel } from './hooks/useWeek'

export default function DayCell({ cell }: { cell: DayCellViewModel }) {

  return (
    <Box sx={{ minHeight: '4rem', border: 1, borderColor: 'divider' }}>
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