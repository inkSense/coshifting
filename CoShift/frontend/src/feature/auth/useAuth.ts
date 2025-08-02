import { useState, useCallback, useEffect } from 'react'

interface TimeAccountDto {
  balanceInMinutes: number
}

export default function useAuth() {
  const [header, setHeader] = useState<string | null>(null)
  const [balance, setBalance] = useState<number | null>(null)

  const loadBalance = useCallback(
    async (overrideHeader?: string): Promise<void> => {
      const auth = overrideHeader ?? header
      if (!auth) return
      try {
        const res = await fetch('/api/persons/me/timeaccount', {
          headers: { Authorization: auth }
        })
        if (res.ok) {
          const dto = (await res.json()) as TimeAccountDto
          setBalance(dto.balanceInMinutes)
        }
      } catch (e) {
        console.error('Unable to fetch balance', e)
      }
    },
    [header]
  )

  const login = useCallback(
    async (auth: string): Promise<boolean> => {
      try {
        const res = await fetch('/api/shifts', {
          headers: { Authorization: auth }
        })
        if (res.ok) {
          setHeader(auth)
          sessionStorage.setItem('authHeader', auth)
          await loadBalance(auth)
          return true
        }
      } catch {
        console.error('Api unavailable')
      }
      sessionStorage.removeItem('authHeader')
      return false
    },
    [loadBalance]
  )

  const logout = useCallback(() => {
    setHeader(null)
    setBalance(null)
    sessionStorage.removeItem('authHeader')
  }, [])

  useEffect(() => {
    const stored = sessionStorage.getItem('authHeader')
    if (stored) {
      login(stored).catch(() => {
        sessionStorage.removeItem('authHeader')
      })
    }
  }, [login])

  return { header, balance, login, logout, loadBalance }
}

