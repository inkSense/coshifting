import { useState, FormEvent } from 'react'

interface Props {
  onLogin: (authHeader: string) => void
}

export default function Login({ onLogin }: Props) {
  const [user, setUser] = useState('')
  const [pass, setPass] = useState('')

  function submit(e: FormEvent) {
    e.preventDefault()
    const token = btoa(`${user}:${pass}`)
    onLogin(`Basic ${token}`)
  }

  return (
    <form onSubmit={submit} className="login-form">
      <input value={user} onChange={e => setUser(e.target.value)} placeholder="User" />
      <input type="password" value={pass} onChange={e => setPass(e.target.value)} placeholder="Password" />
      <button type="submit">Login</button>
    </form>
  )
}

