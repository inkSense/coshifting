import type { PropsWithChildren } from 'react'
import { ThemeProvider, CssBaseline } from '@mui/material'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { theme } from '../theme'
import { AuthProvider } from '../feature/auth/AuthProvider'
import ErrorBoundary from './ErrorBoundary'
import { NotificationProvider } from '../shared/NotificationProvider'

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      staleTime: 300_000, // 5 Minuten
      retry: 1,
    },
  },
})

export function AppProviders({ children }: PropsWithChildren) {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <QueryClientProvider client={queryClient}>
        <NotificationProvider>
          <AuthProvider>
            <ErrorBoundary>
              {children}
            </ErrorBoundary>
          </AuthProvider>
        </NotificationProvider>
      </QueryClientProvider>
    </ThemeProvider>
  )
} 