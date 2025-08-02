import { TextField, FormControl, InputLabel, Select, MenuItem } from '@mui/material'

interface Props {
  nick: string
  setNick: (v: string) => void
  pass: string
  setPass: (v: string) => void
  role: 'USER' | 'ADMIN'
  setRole: (r: 'USER' | 'ADMIN') => void
  passwordLabel?: string
}

export default function PersonFields({ nick, setNick, pass, setPass, role, setRole, passwordLabel = 'Passwort' }: Props) {
  return (
    <>
      <TextField
        label="Nickname"
        value={nick}
        onChange={e => setNick(e.target.value)}
        autoFocus
      />
      <TextField
        label={passwordLabel}
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
    </>
  )
} 