import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { BrowserRouter } from 'react-router-dom'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { CssBaseline, ThemeProvider } from '@mui/material'
import { theme } from '../theme'
import App from './App.tsx'


const queryClient = new QueryClient()
const container = document.getElementById('root');

createRoot(container!).render(
  <StrictMode>
    <ThemeProvider theme={theme}>
      <CssBaseline/>
        <BrowserRouter>
          <QueryClientProvider client={queryClient}>
              <App />
          </QueryClientProvider>
        </BrowserRouter>
    </ThemeProvider>
  </StrictMode>,
)
