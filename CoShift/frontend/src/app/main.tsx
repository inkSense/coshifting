import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import { BrowserRouter } from 'react-router-dom'
import { CssBaseline, ThemeProvider, createTheme } from '@mui/material'
import App from './App.tsx'

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <ThemeProvider theme={createTheme()}>
      <CssBaseline/>
      <BrowserRouter>
        <App/>
      </BrowserRouter>
    </ThemeProvider>
  </StrictMode>,
)
