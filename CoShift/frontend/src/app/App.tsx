import { useState, useCallback, useEffect } from 'react'
import './App.css'
import Login       from '../feature/auth/LoginForm'
import WeekView    from '../feature/week/WeekView'
import Layout      from '../layout/Layout'
import PrivateLayout from '../layout/PrivateLayout'
import { AuthContext } from '../feature/auth/AuthContext'
import { Routes, Route, Navigate } from 'react-router-dom'
import AdminPage   from '../feature/admin/AdminPage'   // gleich anlegen, siehe unten
import { client } from '../api/client'

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
      await client.get('/api/shifts', { headers: { Authorization: header } })

      setAuthHeader(header)
      sessionStorage.setItem('authHeader', header)

      // Saldo laden
      try {
        const dto = await client.get<{ balanceInMinutes: number }>(
          '/api/persons/me/timeaccount',
          { headers: { Authorization: header } }
        )
        setBalance(dto.balanceInMinutes)
      } catch (e) {
        console.error('Unable to fetch balance', e)
      }

      return true
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
    <AuthContext.Provider value={{ header: authHeader, balance, logout }}>
      {authHeader && <Layout />}          {/* <- MUI-AppBar inkl. Burger */}

      {!authHeader ? (
        <Login onLogin={tryLogin} />
      ) : (
        <Routes>
          <Route element={<PrivateLayout />}>
            <Route path="/"      element={<WeekView />} />
            <Route path="/admin" element={<AdminPage />} />
          </Route>
          <Route path="*" element={<Navigate to="/" />} />
        </Routes>
      )}
    </AuthContext.Provider>
  )
}