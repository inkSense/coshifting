/* eslint react-refresh/only-export-components: "off" */
import { createContext, useCallback, useEffect, useState, type ReactNode } from 'react'
import type { AuthCtx } from './types'

export const AuthContext = createContext<AuthCtx | null>(null)

export function AuthProvider({ children }: { children: ReactNode }) {
  const [header, setHeader]   = useState<string | null>(null)
  const [balance, setBalance] = useState<number | null>(null)

  const tryLogin = useCallback(async (authHeader: string): Promise<boolean> => {
    try {
      const res = await fetch('/api/shifts', {
        headers: { Authorization: authHeader }
      })

      if (res.ok) {
        setHeader(authHeader)
        sessionStorage.setItem('authHeader', authHeader)

        try {
          const balRes = await fetch('/api/persons/me/timeaccount', {
            headers: { Authorization: authHeader }
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
    return false
  }, [])

  function logout() {
    setHeader(null)
    setBalance(null)
    sessionStorage.removeItem('authHeader')
  }

  useEffect(() => {
    const stored = sessionStorage.getItem('authHeader')
    if (stored) {
      tryLogin(stored).catch(() => {
        sessionStorage.removeItem('authHeader')
      })
    }
  }, [tryLogin])

  return (
    <AuthContext.Provider value={{ header, balance, tryLogin, logout }}>
      {children}
    </AuthContext.Provider>
  )
}
