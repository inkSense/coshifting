import { useState, useEffect } from 'react'
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
import dayjs, { Dayjs } from 'dayjs'
import type { ShiftDto } from '../types/shift.ts'
import { defaultDuration, defaultCapacity } from './DefaultValues';

interface Props {
  shift: ShiftDto | null
  onClose: () => void
  onSave: (shift: ShiftDto) => void
}

export default function EditShiftDialog({ shift, onClose, onSave }: Props) {
  const [start, setStart] = useState<Dayjs | null>(null)
  const [duration, setDuration] = useState(defaultDuration)
  const [capacity, setCapacity] = useState(defaultCapacity)

  useEffect(() => {
    if (shift) {
      setStart(dayjs(shift.startTime))
      setDuration(shift.durationInMinutes)
      setCapacity(shift.capacity)
    }
  }, [shift])

  const canSave = start !== null && duration > 0 && capacity > 0 && shift !== null

  const handleSave = () => {
    if (!shift || !canSave || !start) return
    onSave({
      ...shift,
      startTime: start.format('YYYY-MM-DDTHH:mm'),
      durationInMinutes: duration,
      capacity,
    })
    onClose()
  }

  return (
    <LocalizationProvider dateAdapter={AdapterDayjs}>
      <Dialog open={!!shift} onClose={onClose}>
        <DialogTitle>Schicht bearbeiten</DialogTitle>
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
