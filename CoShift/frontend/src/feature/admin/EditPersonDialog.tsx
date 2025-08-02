import { useEffect, useState } from 'react'
import {
    Dialog, DialogTitle, DialogContent, DialogActions,
    TextField, FormControl, InputLabel, Select, MenuItem, Button
} from '@mui/material'
import { useApiClient } from '../../api/client'
import type { PersonDto } from '../../types/person'



export default function EditPersonDialog({
                                             person,
                                             onClose,
                                             onUpdated,
                                         }: {
    person: PersonDto | null
    onClose: () => void
    onUpdated: (p: PersonDto) => void
}) {
    const { put } = useApiClient()

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
        try {
            const updated = await put<PersonDto>(`/api/persons/${person.id}`, {
                nickname: nick.trim(),
                password: pass.trim() === '' ? null : pass,
                role,
            })
            onUpdated(updated)
            onClose()
        } catch (e) {
            console.error(e)
        }
    }

    return (
        <Dialog open={!!person} onClose={onClose}>
            <DialogTitle>Person bearbeiten</DialogTitle>

            <DialogContent sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
                <TextField
                    label="Nickname"
                    value={nick}
                    onChange={e => setNick(e.target.value)}
                />
                <TextField
                    label="Neues Passwort"
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
                <Button onClick={onClose}>Abbrechen</Button>
                <Button variant="contained" onClick={handleSave}>
                    Speichern
                </Button>
            </DialogActions>
        </Dialog>
    )
}
