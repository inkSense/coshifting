import { useState, type FormEvent } from 'react'
import { useAuth } from './AuthProvider'
import { useNavigate } from 'react-router-dom'

export default function Login() {
  const { login } = useAuth()
  const navigate = useNavigate()
  const [user,  setUser]  = useState('')
  const [pass,  setPass]  = useState('')
  const [error, setError] = useState<string | null>(null)

  async function submit(e: FormEvent) {
    e.preventDefault()
    setError(null)                               // alte Fehlermeldung zurücksetzen
    const success = await login(user, pass)

    if (success) {
      navigate('/')
    } else {
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