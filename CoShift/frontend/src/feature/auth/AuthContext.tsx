import { createContext, useContext } from 'react'
import type useAuth from './useAuth'

export type AuthCtx = ReturnType<typeof useAuth>
export const AuthContext = createContext<AuthCtx | null>(null)
export const useAuth = () => {
  const ctx = useContext(AuthContext)
  if (!ctx) throw new Error('useAuth must be inside <AuthContext.Provider>')
  return ctx
}

