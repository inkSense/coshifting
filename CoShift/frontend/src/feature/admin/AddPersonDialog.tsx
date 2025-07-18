// src/components/AddPersonDialog.tsx
import { useState } from 'react'
import {
    Dialog, DialogTitle, DialogContent, DialogActions,
    TextField, FormControl, InputLabel, Select, MenuItem,
    Button
} from '@mui/material'
import { useAuth } from '../auth/AuthContext'
import type { PersonDto } from '../../types/person'



interface AddPersonDialogProps {
    open: boolean
    onClose: () => void
    onCreated: (person: PersonDto) => void   // liefert neu angelegte Person zurück
}

export default function AddPersonDialog({ open, onClose, onCreated }: AddPersonDialogProps) {
    const { header } = useAuth()

    // lokaler Formular-State
    const [nick, setNick] = useState('')
    const [pass, setPass] = useState('')
    const [role, setRole] = useState<'USER' | 'ADMIN'>('USER')
    const [saving, setSaving] = useState(false)

    const canSave = nick.trim() !== '' && pass.trim() !== ''

    const handleSave = async () => {
        if (!canSave) return
        setSaving(true)
        try {
            const res = await fetch('/api/persons', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    ...(header ? { Authorization: header } : {}),
                },
                body: JSON.stringify({ nickname: nick.trim(), password: pass, role }),
            })
            if (!res.ok) throw new Error('Create failed')
            const created: PersonDto = await res.json()
            onCreated(created)          // 👉 Parent informieren
            onClose()
            // Reset für nächstes Öffnen
            setNick('')
            setPass('')
            setRole('USER')
        } catch (e) {
            console.error(e)
        } finally {
            setSaving(false)
        }
    }

    return (
        <Dialog open={open} onClose={onClose}>
            <DialogTitle>Neue Person anlegen</DialogTitle>

            <DialogContent sx={{ display: 'flex', flexDirection: 'column', gap: 2, mt: 1 }}>
                <TextField
                    label="Nickname"
                    value={nick}
                    onChange={e => setNick(e.target.value)}
                    autoFocus
                />
                <TextField
                    label="Passwort"
                    type="password"
                    value={pass}
                    onChange={e => setPass(e.target.value)}
                />
                <FormControl size="small">
                    <InputLabel id="role-label">Rolle</InputLabel>
                    <Select
                        labelId="role-label"
                        value={role}
                        label="Rolle"
                        onChange={e => setRole(e.target.value as 'USER' | 'ADMIN')}
                    >
                        <MenuItem value="USER">USER</MenuItem>
                        <MenuItem value="ADMIN">ADMIN</MenuItem>
                    </Select>
                </FormControl>
            </DialogContent>

            <DialogActions>
                <Button onClick={onClose} disabled={saving}>Abbrechen</Button>
                <Button
                    variant="contained"
                    onClick={handleSave}
                    disabled={!canSave || saving}
                >
                    Speichern
                </Button>
            </DialogActions>
        </Dialog>
    )
}
