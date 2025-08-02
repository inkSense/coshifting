import { createContext, useContext, useEffect, useState, useCallback } from 'react'
import type { PropsWithChildren } from 'react'
import { authFetch } from '../../shared/api'

interface AuthState {
  header: string | null
  balance: number | null
  isAuthenticated: boolean
  loading: boolean
  error: string | null
  login: (user: string, pass: string) => Promise<boolean>
  logout: () => void
}

const AuthContext = createContext<AuthState | undefined>(undefined)

export const useAuth = () => {
  const ctx = useContext(AuthContext)
  if (!ctx) throw new Error('useAuth must be used within <AuthProvider>')
  return ctx
}

export function AuthProvider({ children }: PropsWithChildren) {
  const [header, setHeader] = useState<string | null>(null)
  const [balance, setBalance] = useState<number | null>(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  /* --- Hilfsfunktionen -------------------------------------------------- */
  const fetchBalance = useCallback(async (hdr: string) => {
    try {
      const { balanceInMinutes } = await authFetch<{ balanceInMinutes: number }>('/api/persons/me/timeaccount', hdr)
      setBalance(balanceInMinutes)
    } catch {
      // ignore
    }
  }, [])

  const checkToken = useCallback(
    async (hdr: string) => {
      setLoading(true)
      setError(null)
      const ok = await authFetch('/api/shifts', hdr).then(() => true).catch(() => false)

      if (ok) {
        setHeader(hdr)
        sessionStorage.setItem('authHeader', hdr)
        await fetchBalance(hdr)
      } else {
        logout()
      }
      setLoading(false)
      return ok
    },
    [fetchBalance],
  )

  const login = useCallback(
    async (user: string, pass: string) => {
      const hdr = `Basic ${btoa(`${user}:${pass}`)}`
      return checkToken(hdr)
    },
    [checkToken],
  )

  const logout = () => {
    setHeader(null)
    setBalance(null)
    sessionStorage.removeItem('authHeader')
  }

  /* --- Bootstrap bei App-Start ----------------------------------------- */
  useEffect(() => {
    const stored = sessionStorage.getItem('authHeader')
    if (stored) {
      checkToken(stored)
    }
  }, [checkToken])

  /* --- Context-Objekt --------------------------------------------------- */
  const value: AuthState = {
    header,
    balance,
    isAuthenticated: !!header,
    loading,
    error,
    login,
    logout,
  }

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}