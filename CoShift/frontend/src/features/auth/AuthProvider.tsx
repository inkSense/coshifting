/* eslint react-refresh/only-export-components: "off" */
import { createContext, useCallback, useContext, useEffect, useState, type ReactNode } from 'react'
import type { AuthCtx } from './types'

export const AuthContext = createContext<AuthCtx | null>(null)

export function AuthProvider({ children }: { children: ReactNode }) {
  const [header, setHeader]   = useState<string | null>(null)
  const [balance, setBalance] = useState<number | null>(null)
  const [role,       setRole]       = useState<'ADMIN' | 'USER' | null>(null)

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

        try {
          const me = await fetch('/api/persons/me', { headers:{ Authorization: authHeader }})
          if (me.ok) {
            const dto = await me.json() as { role: 'ADMIN' | 'USER' }
            setRole(dto.role)
          }
        } catch (e) {
          console.error('Unable to fetch role', e)
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
    <AuthContext.Provider value={{ 
      authHeader: header, 
      balance, 
      role, 
      isAuthenticated: header!==null, 
      isAdmin: role==='ADMIN', 
      tryLogin, 
      logout 
      }}>
      {children}
    </AuthContext.Provider>
  )
}

export const useAuth = () => {
  const ctx = useContext<AuthCtx | null>(AuthContext)
  if (!ctx) throw new Error('useAuth must be inside <AuthProvider>')
  return ctx
}

