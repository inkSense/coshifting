import { useState, useEffect } from 'react'
import { Dialog, DialogTitle, DialogContent, DialogActions, TextField, Button } from '@mui/material'
import type { ShiftDto } from '../types/shift.ts'

interface Props {
  shift: ShiftDto | null
  onClose: () => void
  onSave: (shift: ShiftDto) => void
}

export default function EditShiftDialog({ shift, onClose, onSave }: Props) {
  const [start, setStart] = useState('')
  const [duration, setDuration] = useState(60)
  const [capacity, setCapacity] = useState(1)

  useEffect(() => {
    if (shift) {
      setStart(shift.startTime)
      setDuration(shift.durationInMinutes)
      setCapacity(shift.capacity)
    }
  }, [shift])

  const canSave = start.trim() !== '' && duration > 0 && capacity > 0 && shift !== null

  const handleSave = () => {
    if (!shift || !canSave) return
    onSave({ ...shift, startTime: start, durationInMinutes: duration, capacity })
    onClose()
  }

  return (
    <Dialog open={!!shift} onClose={onClose}>
      <DialogTitle>Schicht bearbeiten</DialogTitle>
      <DialogContent sx={{ display: 'flex', flexDirection: 'column', gap: 2, mt: 1 }}>
        <TextField
          label="Start"
          type="datetime-local"
          value={start}
          onChange={e => setStart(e.target.value)}
          InputLabelProps={{ shrink: true }}
        />
        <TextField
          label="Dauer (Minuten)"
          type="number"
          value={duration}
          onChange={e => setDuration(Number(e.target.value))}
        />
        <TextField
          label="Kapazität"
          type="number"
          value={capacity}
          onChange={e => setCapacity(Number(e.target.value))}
        />
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose}>Abbrechen</Button>
        <Button variant="contained" onClick={handleSave} disabled={!canSave}>
          Speichern
        </Button>
      </DialogActions>
    </Dialog>
  )
}
