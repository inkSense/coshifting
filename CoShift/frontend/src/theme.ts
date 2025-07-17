import { createTheme } from '@mui/material'

export const theme = createTheme({
  palette: {
    mode: 'light',
    primary: { main: '#646cff' },
    success: { main: '#339f4c' },          // für volle Schicht
    background: { default: '#ffffff' },
    text: { primary: '#213547' },
  },
  typography: {
    fontFamily: 'system-ui, Avenir, Helvetica, Arial, sans-serif',
    fontWeightRegular: 400,
    h1: { fontSize: '3.2rem', lineHeight: 1.1 },
  },
  shape: { borderRadius: 8 },
  components: {
    MuiCssBaseline: {
      styleOverrides: {
        '*, *::before, *::after': { boxSizing: 'border-box' },
        html:   { height: '100%' },
        body:   { height: '100%', margin: 0 },
        '#root':{ height: '100%' },

        a: {
          fontWeight: 500,
          color: '#646cff',
          textDecoration: 'none',
          '&:hover': { color: '#747bff' },
        },
        button: {
          borderRadius: 8,
          border: '1px solid transparent',
          padding: '0.6em 1.2em',
          fontSize: '1em',
          fontWeight: 500,
          fontFamily: 'inherit',
          transition: 'border-color 0.25s',
          '&:hover': { borderColor: '#646cff' },
          '&:focus-visible': { outline: '4px auto -webkit-focus-ring-color' },
        },
      },
    },
  },
})