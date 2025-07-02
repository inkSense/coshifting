import './App.css'
import WeekView from './WeekView'
import Login from './Login'
import { useState } from 'react'

export default function App() {
  const [auth, setAuth] = useState<string | null>(null)

  return auth
    ? <WeekView authHeader={auth} />
    : <Login onLogin={setAuth} />
}