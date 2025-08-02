import { Component } from 'react'
import type { ReactNode } from 'react'
import { Box, Button, Typography } from '@mui/material'

interface State { hasError: boolean }

export default class ErrorBoundary extends Component<{ children: ReactNode }, State> {
  state: State = { hasError: false }

  static getDerivedStateFromError() {
    return { hasError: true }
  }

  componentDidCatch(error: Error) {
    console.error('Uncaught error:', error)
  }

  render() {
    if (this.state.hasError) {
      return (
        <Box sx={{ p: 4 }}>
          <Typography variant="h5" gutterBottom>Etwas ist schief gegangen.</Typography>
          <Button variant="contained" onClick={() => window.location.reload()}>Seite neu laden</Button>
        </Box>
      )
    }
    return this.props.children
  }
} 