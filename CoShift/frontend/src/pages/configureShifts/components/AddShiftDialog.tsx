import { useState } from 'react'
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Button,
} from '@mui/material'
import {
  LocalizationProvider,
  DateTimePicker,
} from '@mui/x-date-pickers'
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs'
import { Dayjs } from 'dayjs'
import type { NewShiftDto } from '../types/shift.ts'

interface Props {
  open: boolean
  onClose: () => void
  onSave: (shift: NewShiftDto) => void
}

export default function AddShiftDialog({ open, onClose, onSave }: Props) {
  const [start, setStart] = useState<Dayjs | null>(null)
  const [duration, setDuration] = useState(60)
  const [capacity, setCapacity] = useState(1)

  const canSave = start !== null && duration > 0 && capacity > 0

  const handleSave = () => {
    if (!canSave || !start) return
    onSave({
      startTime: start.format('YYYY-MM-DDTHH:mm'),
      durationInMinutes: duration,
      capacity,
    })
    setStart(null)
    setDuration(60)
    setCapacity(1)
    onClose()
  }

  return (
    <LocalizationProvider dateAdapter={AdapterDayjs}>
      <Dialog open={open} onClose={onClose}>
        <DialogTitle>Neue Schicht anlegen</DialogTitle>
        <DialogContent sx={{ display: 'flex', flexDirection: 'column', gap: 2, mt: 1 }}>
          <DateTimePicker
            label="Start"
            value={start}
            onChange={value => setStart(value)}
            ampm={false}
            slotProps={{ textField: { InputLabelProps: { shrink: true } } }}
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
    </LocalizationProvider>
  )
}
