import { useState, type FormEvent } from 'react'

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
    <form onSubmit={submit} className="login-form">
      <input
        value={user}
        onChange={e => setUser(e.target.value)}
        placeholder="User"
        autoComplete="username"
      />
      <input
        type="password"
        value={pass}
        onChange={e => setPass(e.target.value)}
        placeholder="Password"
        autoComplete="current-password"
      />
      <button type="submit">Login</button>

      {error && <div className="error-msg">{error}</div>}
    </form>
  )
}