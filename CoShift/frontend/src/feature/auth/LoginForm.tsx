import { useState, type FormEvent } from 'react'
import { Box, TextField, Button, Alert } from '@mui/material'

interface Props {
  /* Liefert true bei erfolgreichem Login, sonst false */
  onLogin: (authHeader: string) => Promise<boolean>
}

export default function Login({ onLogin }: Props) {
  const [user,  setUser]  = useState('')
  const [pass,  setPass]  = useState('')
  const [error, setError] = useState<string | null>(null)

  async function submit(e: FormEvent) {
    e.preventDefault()
    setError(null)                               // alte Fehlermeldung zurücksetzen
    const token   = btoa(`${user}:${pass}`)
    const success = await onLogin(`Basic ${token}`)

    if (!success) {
      setError('Benutzername oder Passwort falsch')
    }
  }

  return (
    <Box
      component="form"
      onSubmit={submit}
      sx={{
        display: 'flex',
        flexDirection: 'column',
        gap: 2,
        width: 300,
        mx: 'auto',
        mt: 8,
      }}
    >
      <TextField
        label="User"
        value={user}
        onChange={e => setUser(e.target.value)}
        autoComplete="username"
        fullWidth
      />
      <TextField
        label="Password"
        type="password"
        value={pass}
        onChange={e => setPass(e.target.value)}
        autoComplete="current-password"
        fullWidth
      />
      {error && <Alert severity="error">{error}</Alert>}
      <Button type="submit" variant="contained">Login</Button>
    </Box>
  )
}