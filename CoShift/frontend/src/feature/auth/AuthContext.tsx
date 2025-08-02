import { createContext, useContext } from 'react'

export interface AuthCtx {
  header:  string | null
  balance: number | null
  login:   (authHeader: string) => Promise<boolean>
  logout: () => void
}
export const AuthContext = createContext<AuthCtx | null>(null)
export const useAuth = () => {
  const ctx = useContext(AuthContext)
  if (!ctx) throw new Error('useAuth must be inside <AuthContext.Provider>')
  return ctx
}