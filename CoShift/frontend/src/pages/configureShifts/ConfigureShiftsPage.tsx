import { useState } from 'react'
import {
  Container,
  Paper,
  Typography,
  TableContainer,
  Table,
  TableHead,
  TableRow,
  TableCell,
  TableBody,
  CircularProgress,
  IconButton,
  Tooltip,
} from '@mui/material'
import AddIcon from '@mui/icons-material/Add'
import EditIcon from '@mui/icons-material/Edit'
import DeleteIcon from '@mui/icons-material/Delete'

import { useShifts } from './hooks/useShifts.ts'
import type { ShiftDto } from './types/shift.ts'
import AddShiftDialog from './components/AddShiftDialog.tsx'
import EditShiftDialog from './components/EditShiftDialog.tsx'

export default function ConfigureShiftsPage() {
  const { shifts, addShift, updateShift, deleteShift } = useShifts()
  const [addOpen, setAddOpen] = useState(false)
  const [edit, setEdit] = useState<ShiftDto | null>(null)

  const rows = shifts.data ?? []
  const isLoading = shifts.isLoading

  return (
    <Container sx={{ py: 3 }}>
      <Typography variant="h5" gutterBottom>Schichten verwalten</Typography>
      <Tooltip title="Neue Schicht hinzufügen">
        <IconButton color="primary" onClick={() => setAddOpen(true)}>
          <AddIcon />
        </IconButton>
      </Tooltip>

      {isLoading ? (
        <CircularProgress />
      ) : (
        <TableContainer component={Paper} sx={{ maxHeight: 500 }}>
          <Table stickyHeader size="small" aria-label="shift table">
            <TableHead>
              <TableRow>
                <TableCell>ID</TableCell>
                <TableCell>Start</TableCell>
                <TableCell>Dauer</TableCell>
                <TableCell>Kapazität</TableCell>
                <TableCell />
              </TableRow>
            </TableHead>
            <TableBody>
              {rows.map(s => (
                <TableRow key={s.id} hover>
                  <TableCell>{s.id}</TableCell>
                  <TableCell>{new Date(s.startTime).toLocaleString()}</TableCell>
                  <TableCell>{s.durationInMinutes}</TableCell>
                  <TableCell>{s.capacity}</TableCell>
                  <TableCell padding="checkbox">
                    <IconButton size="small" onClick={() => setEdit(s)}>
                      <EditIcon fontSize="small" />
                    </IconButton>
                    <IconButton
                      size="small"
                      color="error"
                      onClick={() => {
                        if (confirm(`Schicht ${s.id} wirklich löschen?`)) {
                          deleteShift.mutate(s.id)
                        }
                      }}
                    >
                      <DeleteIcon fontSize="small" />
                    </IconButton>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      )}

      <AddShiftDialog
        open={addOpen}
        onClose={() => setAddOpen(false)}
        onSave={dto => addShift.mutate(dto)}
      />
      <EditShiftDialog
        shift={edit}
        onClose={() => setEdit(null)}
        onSave={s => updateShift.mutate(s)}
      />
    </Container>
  )
}
