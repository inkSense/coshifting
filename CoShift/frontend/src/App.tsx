import { useState, useCallback, useEffect } from 'react'
import './App.css'
import Header     from './components/Header'
import Login      from './components/LoginForm'
import WeekView   from './WeekView'

/**
 * Root-Komponente:
 * – Verwaltet den Auth-Header im State.
 * – Reicht eine „tryLogin“-Funktion an <Login/> durch, die
 *   einen Probe-Call auf ein geschütztes Backend-API macht.
 * – Zeigt nach erfolgreichem Login die <WeekView/>.
 */
export default function App() {
  const [authHeader, setAuthHeader] = useState<string | null>(null)
  const [balance,    setBalance]    = useState<number | null>(null)

  const tryLogin = useCallback(async (header: string): Promise<boolean> => {
    try {
      const res = await fetch('/api/shifts', {
        headers: { Authorization: header }
      })

      if (res.ok) {
        setAuthHeader(header)
        sessionStorage.setItem('authHeader', header)

        // Saldo laden
        try {
          const balRes = await fetch('/api/persons/me/timeaccount', {
            headers: { Authorization: header }
          })
          if (balRes.ok) {
            const dto = await balRes.json() as { balanceInMinutes: number }
            setBalance(dto.balanceInMinutes)
          }
        } catch (e) {
          console.error('Unable to fetch balance', e)
        }

        return true
      }
    } catch {
      console.error('Api unavailable')
    }
    sessionStorage.removeItem('authHeader')
    return false                         // 401 
  }, [])

  function logout() {
    setAuthHeader(null)
    setBalance(null)
    sessionStorage.removeItem('authHeader')        
  }

  useEffect(() => {
    const stored = sessionStorage.getItem('authHeader')
    if (stored) {
      // Schnelltest: ist das Token noch gültig?
      tryLogin(stored).catch(() => {
        sessionStorage.removeItem('authHeader')
      })
    }
  }, [tryLogin])
  



  return (
    <>
      {authHeader && <Header onLogout={logout} balance={balance ?? undefined} />}   

      {authHeader
        ? <WeekView authHeader={authHeader} />       
        : <Login onLogin={tryLogin} />}
    </>
  )
}