import { useEffect, useState } from 'react'
import { Dialog, DialogTitle, DialogContent, DialogActions, Button } from '@mui/material'
import { useAuth } from '../auth/AuthProvider'
import { authFetch } from '../../shared/api'
import type { PersonDto } from '../../types/person'
import PersonFields from './PersonFields'


export default function EditPersonDialog({
                                             person,
                                             onClose,
                                             onUpdated,
                                         }: {
    person: PersonDto | null
    onClose: () => void
    onUpdated: (p: PersonDto) => void
}) {
    const { header } = useAuth()

    // lokaler Formular-State
    const [nick, setNick] = useState('')
    const [pass, setPass] = useState('')
    const [role, setRole] = useState<'USER' | 'ADMIN'>('USER')

    // wenn ein neuer Datensatz zum Bearbeiten reinkommt, Felder füllen
    useEffect(() => {
        if (person) {
            setNick(person.nickname)
            setRole(person.role)
            setPass('')
        }
    }, [person])

    const handleSave = async () => {
        if (!person) return
        if (!header) return
        try {
            const updated = await authFetch<PersonDto>(`/api/persons/${person.id}`, header, {
                method: 'PUT',
                body: JSON.stringify({
                    nickname: nick.trim(),
                    password: pass.trim() === '' ? null : pass,
                    role,
                }),
            })
            onUpdated(updated)
            onClose()
        } catch (e) {
            console.error('update failed', e)
        }
    }

    return (
        <Dialog open={!!person} onClose={onClose}>
            <DialogTitle>Person bearbeiten</DialogTitle>

            <DialogContent sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
                <PersonFields
                  nick={nick}
                  setNick={setNick}
                  pass={pass}
                  setPass={setPass}
                  role={role}
                  setRole={setRole}
                  passwordLabel="Neues Passwort"
                />
            </DialogContent>

            <DialogActions>
                <Button onClick={onClose}>Abbrechen</Button>
                <Button variant="contained" onClick={handleSave}>
                    Speichern
                </Button>
            </DialogActions>
        </Dialog>
    )
}
