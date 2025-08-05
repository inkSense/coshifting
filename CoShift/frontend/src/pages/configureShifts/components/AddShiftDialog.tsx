import { useState } from 'react'
import { Dialog, DialogTitle, DialogContent, DialogActions, TextField, Button } from '@mui/material'
import type { NewShiftDto } from '../types/shift.ts'

interface Props {
  open: boolean
  onClose: () => void
  onSave: (shift: NewShiftDto) => void
}

export default function AddShiftDialog({ open, onClose, onSave }: Props) {
  const [start, setStart] = useState('')
  const [duration, setDuration] = useState(60)
  const [capacity, setCapacity] = useState(1)

  const canSave = start.trim() !== '' && duration > 0 && capacity > 0

  const handleSave = () => {
    if (!canSave) return
    onSave({ startTime: start, durationInMinutes: duration, capacity })
    setStart('')
    setDuration(60)
    setCapacity(1)
    onClose()
  }

  return (
    <Dialog open={open} onClose={onClose}>
      <DialogTitle>Neue Schicht anlegen</DialogTitle>
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
