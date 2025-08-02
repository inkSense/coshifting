export interface AuthCtx {
  header: string | null
  balance: number | null
  tryLogin: (authHeader: string) => Promise<boolean>
  logout: () => void
}
