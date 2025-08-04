export interface AuthCtx {
  authHeader: string | null
  role:       'ADMIN' | 'USER' | null
  balance:    number | null
  personId:   number | null
  isAuthenticated: boolean
  isAdmin:    boolean
  tryLogin: (authHeader: string) => Promise<boolean>
  logout:   () => void
}