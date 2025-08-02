// src/components/AddPersonDialog.tsx
import { useState } from 'react'
import { Dialog, DialogTitle, DialogContent, DialogActions, Button } from '@mui/material'
import { useAuth } from '../auth/AuthProvider'
import { authFetch } from '../../shared/api'
import type { PersonDto } from '../../types/person'
import PersonFields from './PersonFields'


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
            if (!header) throw new Error('Unauthenticated')
            const created = await authFetch<PersonDto>('/api/persons', header, {
              method: 'POST',
              body: JSON.stringify({ nickname: nick.trim(), password: pass, role }),
            })
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
              <PersonFields
                nick={nick}
                setNick={setNick}
                pass={pass}
                setPass={setPass}
                role={role}
                setRole={setRole}
              />
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
