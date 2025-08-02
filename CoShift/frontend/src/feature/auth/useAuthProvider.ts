import { useCallback, useEffect, useState } from 'react'

interface BalanceDto {
  balanceInMinutes: number
}

export function useAuthProvider() {
  const [header, setHeader]   = useState<string | null>(null)
  const [balance, setBalance] = useState<number | null>(null)

  const login = useCallback(async (authHeader: string): Promise<boolean> => {
    try {
      const res = await fetch('/api/shifts', {
        headers: { Authorization: authHeader }
      })
      if (!res.ok) throw new Error('unauthorized')

      setHeader(authHeader)
      sessionStorage.setItem('authHeader', authHeader)

      try {
        const balRes = await fetch('/api/persons/me/timeaccount', {
          headers: { Authorization: authHeader }
        })
        if (balRes.ok) {
          const dto = await balRes.json() as BalanceDto
          setBalance(dto.balanceInMinutes)
        }
      } catch (e) {
        console.error('Unable to fetch balance', e)
      }
      return true
    } catch {
      sessionStorage.removeItem('authHeader')
      return false
    }
  }, [])

  const logout = useCallback(() => {
    setHeader(null)
    setBalance(null)
    sessionStorage.removeItem('authHeader')
  }, [])

  useEffect(() => {
    const stored = sessionStorage.getItem('authHeader')
    if (stored) {
      login(stored).catch(() => sessionStorage.removeItem('authHeader'))
    }
  }, [login])

  return { header, balance, login, logout }
}
