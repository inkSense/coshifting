import { useState, type FormEvent } from 'react'
import { TextField, Button, CircularProgress, Box } from '@mui/material'

interface Props {
  /* Liefert true bei erfolgreichem Login, sonst false */
  onLogin: (authHeader: string) => Promise<boolean>
}

export default function Login({ onLogin }: Props) {
  const [user, setUser] = useState('')
  const [pass, setPass] = useState('')
  const [error, setError] = useState<string | null>(null)
  const [loading, setLoading] = useState(false)

  async function submit(e: FormEvent) {
    e.preventDefault()
    setError(null) // alte Fehlermeldung zurücksetzen
    setLoading(true)
    const token = btoa(`${user}:${pass}`)
    const success = await onLogin(`Basic ${token}`)
    setLoading(false)

    if (!success) {
      setError('Benutzername oder Passwort falsch')
    }
  }

  return (
    <Box
      component="form"
      onSubmit={submit}
      className="login-form"
      sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}
    >
      <TextField
        value={user}
        onChange={e => setUser(e.target.value)}
        label="User"
        autoComplete="username"
      />
      <TextField
        type="password"
        value={pass}
        onChange={e => setPass(e.target.value)}
        label="Password"
        autoComplete="current-password"
      />
      <Button type="submit" variant="contained" disabled={loading}>
        {loading ? <CircularProgress size={24} /> : 'Login'}
      </Button>

      {error && (
        <Box className="error-msg" sx={{ color: 'error.main' }}>
          {error}
        </Box>
      )}
    </Box>
  )
}
