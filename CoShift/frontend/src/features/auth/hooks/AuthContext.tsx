import { useContext } from 'react'
import { AuthContext } from '../AuthProvider'
import type { AuthCtx } from '../types'

export const useAuth = () => {
  const ctx = useContext<AuthCtx | null>(AuthContext)
  if (!ctx) throw new Error('useAuth must be inside <AuthProvider>')
  return ctx
}
