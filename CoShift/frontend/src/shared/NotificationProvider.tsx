import { createContext, useContext, useState } from 'react'
import { Snackbar, Alert } from '@mui/material'
import type { PropsWithChildren } from 'react'

interface NotifyCtx { notify: (msg: string, severity?: 'success' | 'error' | 'info' | 'warning') => void }
const Ctx = createContext<NotifyCtx | undefined>(undefined)
export const useNotify = () => {
  const ctx = useContext(Ctx)
  if (!ctx) throw new Error('useNotify must be inside NotificationProvider')
  return ctx.notify
}

export function NotificationProvider({ children }: PropsWithChildren) {
  const [open, setOpen] = useState(false)
  const [msg, setMsg] = useState('')
  const [sev, setSev] = useState<'success' | 'error' | 'info' | 'warning'>('info')

  const notify = (m: string, s: typeof sev = 'info') => {
    setMsg(m)
    setSev(s)
    setOpen(true)
  }

  return (
    <Ctx.Provider value={{ notify }}>
      {children}
      <Snackbar open={open} autoHideDuration={3000} onClose={() => setOpen(false)}>
        <Alert severity={sev} sx={{ width: '100%' }} onClose={() => setOpen(false)}>
          {msg}
        </Alert>
      </Snackbar>
    </Ctx.Provider>
  )
} 