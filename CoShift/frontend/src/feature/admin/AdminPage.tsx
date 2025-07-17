import { useEffect, useState } from 'react'
import { useAuth } from '../auth/AuthContext'
import EditIcon from '@mui/icons-material/Edit'
import DeleteIcon from '@mui/icons-material/Delete'
import { IconButton as MuiIconButton } from '@mui/material'
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
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  TextField,
  Select,
  MenuItem,
  InputLabel,
  FormControl,
  DialogActions,
  IconButton,
  Tooltip,
} from '@mui/material'
import AddIcon from '@mui/icons-material/Add'

interface PersonDto {
  id: number
  nickname: string
  role: string
}

export default function AdminPage() {
  const { header } = useAuth()
  const [persons, setPersons] = useState<PersonDto[]>([])
  const [loading, setLoading] = useState(false)
  const [open, setOpen] = useState(false)
  const [nick, setNick] = useState('')
  const [pass, setPass] = useState('')
  const [role, setRole] = useState<'USER' | 'ADMIN'>('USER')
  const [saving, setSaving] = useState(false)
  const canSave = nick.trim().length > 0 && pass.trim().length > 0
  const [edit, setEdit] = useState<PersonDto|null>(null)
  const [eNick, setENick] = useState('')
  const [ePass, setEPass] = useState('')
  const [eRole, setERole] = useState<'USER'|'ADMIN'>('USER')

  useEffect(() => {
    setLoading(true)
    fetch('/api/persons', {
      headers: header ? { Authorization: header } : {}
    })
      .then(res => {
        if (!res.ok) throw new Error('Network error')
        return res.json()
      })
      .then((data: PersonDto[]) => setPersons(data))
      .catch(err => console.error('Failed to load persons', err))
      .finally(() => setLoading(false))
  }, [header])

  return (
    <Container sx={{ py: 3 }}>
      <Typography variant="h5" gutterBottom>
        Personenverwaltung
      </Typography>

      {/* Add Person Button */}
      <Tooltip title="Neue Person hinzufügen">
        <IconButton color="primary" onClick={() => setOpen(true)}>
          <AddIcon />
        </IconButton>
      </Tooltip>

      {loading ? (
        <CircularProgress />
      ) : (
        <TableContainer component={Paper} sx={{ maxHeight: 500 }}>
          <Table stickyHeader size="small" aria-label="persons table">
            <TableHead>
              <TableRow>
                <TableCell>ID</TableCell>
                <TableCell>Nickname</TableCell>
                <TableCell>Rolle</TableCell>
                <TableCell/> {/* Aktionen */}
              </TableRow>
            </TableHead>
            <TableBody>
              {persons.map(p => (
                <TableRow key={p.id} hover>
                  <TableCell>{p.id}</TableCell>
                  <TableCell>{p.nickname}</TableCell>
                  <TableCell>{p.role}</TableCell>
                  <TableCell padding="checkbox">
                    <IconButton size="small"
                      onClick={()=>{
                        setEdit(p)
                        setENick(p.nickname)
                        setERole(p.role as 'USER'|'ADMIN')
                      }}>
                      <EditIcon fontSize="small"/>
                    </IconButton>
                    <MuiIconButton
                      size="small"
                      color="error"
                      onClick={async () => {
                        if (!confirm(`Person ${p.nickname} wirklich löschen?`)) return
                        await fetch(`/api/persons/${p.id}`, {
                          method: 'DELETE',
                          headers: header ? { Authorization: header } : {}
                        })
                        setPersons(prev => prev.filter(x => x.id !== p.id))
                      }}
                    >
                      <DeleteIcon fontSize="small" />
                    </MuiIconButton>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      )}

      {/* Dialog */}
      <Dialog open={open} onClose={() => setOpen(false)}>
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

          {/* Rollen-Auswahl */}
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
          <Button onClick={() => setOpen(false)} disabled={saving}>Abbrechen</Button>
          <Button
            variant="contained"
            onClick={async () => {
              if (!canSave) return
              setSaving(true)
              try {
                const res = await fetch('/api/persons', {
                  method: 'POST',
                  headers: {
                    'Content-Type': 'application/json',
                    ...(header ? { Authorization: header } : {}),
                  },
                  body: JSON.stringify({
                    nickname: nick.trim(),
                    password: pass,
                    role
                  })
                })
                if (!res.ok) throw new Error('Create failed')
                const created: PersonDto = await res.json()
                setPersons(prev => [...prev, created])
                setOpen(false)
                setNick('')
                setPass('')
                setRole('USER')
              } catch (e) {
                console.error(e)
              } finally {
                setSaving(false)
              }
            }}
            disabled={!canSave || saving}
          >
            Speichern
          </Button>
        </DialogActions>
      </Dialog>

      {/* Edit Dialog */}
      <Dialog open={!!edit} onClose={()=>setEdit(null)}>
        <DialogTitle>Person bearbeiten</DialogTitle>
        <DialogContent sx={{display:'flex', flexDirection:'column', gap:2}}>
          <TextField label="Nickname" value={eNick}
                     onChange={e=>setENick(e.target.value)}/>
          <TextField label="Neues Passwort"
                     type="password"
                     value={ePass}
                     onChange={e=>setEPass(e.target.value)}/>
          <FormControl size="small">
            <InputLabel>Rolle</InputLabel>
            <Select value={eRole}
                    label="Rolle"
                    onChange={e=>setERole(e.target.value as any)}>
              <MenuItem value="USER">USER</MenuItem>
              <MenuItem value="ADMIN">ADMIN</MenuItem>
            </Select>
          </FormControl>
        </DialogContent>
        <DialogActions>
          <Button onClick={()=>setEdit(null)}>Abbrechen</Button>
          <Button variant="contained" onClick={async ()=>{
            if(!edit) return
            const res = await fetch(`/api/persons/${edit.id}`,{
              method:'PUT',
              headers:{
                'Content-Type':'application/json',
                ...(header ? {Authorization:header}: {})
              },
              body: JSON.stringify({
                nickname: eNick.trim(),
                password: ePass.trim()===''? null : ePass,
                role: eRole
              })
            })
            if(res.ok){
              const updated:PersonDto = await res.json()
              setPersons(prev=>prev.map(p=>p.id===updated.id? updated : p))
              setEdit(null)
            } else console.error('update failed')
          }}>Speichern</Button>
        </DialogActions>
      </Dialog>
    </Container>
  )
}