export interface AuthCtx {
  header: string | null
  balance: number | null
  logout: () => void
}
