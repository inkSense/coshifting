import { useState, useCallback } from 'react'
import './App.css'
import Login     from './Login'
import WeekView  from './WeekView'

/**
 * Root-Komponente:
 * – Verwaltet den Auth-Header im State.
 * – Reicht eine „tryLogin“-Funktion an <Login/> durch, die
 *   einen Probe-Call auf ein geschütztes Backend-API macht.
 * – Zeigt nach erfolgreichem Login die <WeekView/>.
 */
export default function App() {
  const [authHeader, setAuthHeader] = useState<string | null>(null)

  /**
   * Versucht, die übergebenen Credentials gegen das Backend zu prüfen.
   * @return true → Login ok, false → 401 oder Netzwerkfehler
   */
  const tryLogin = useCallback(async (header: string): Promise<boolean> => {
    try {
      const res = await fetch('/api/shifts', {          // geschütztes Endpoint
        headers: { Authorization: header }
      })

      if (res.ok) {
        setAuthHeader(header)          // Erfolg → Header speichern
        return true
      }
    } catch {
      /* Network error -> fällt durch */
    }
    return false                         // 401 oder Fehler
  }, [])

  return authHeader
    ? <WeekView authHeader={authHeader} />   // eingeloggt
    : <Login onLogin={tryLogin} />           // Login-Formular
}