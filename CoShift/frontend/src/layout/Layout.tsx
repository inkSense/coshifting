import { AppBar, Toolbar, Typography, Button } from '@mui/material'
import LogoutIcon    from '@mui/icons-material/Logout'
import { Link as RouterLink, useLocation } from 'react-router-dom'
import { useAuth }   from '../feature/auth/AuthContext'

function formatMinutes(total: number): string {
  const hours = Math.floor(total / 60)
  const minutes = total % 60
  return `${hours}h ${minutes}m`
}

export default function Layout(){
  const { logout, balance } = useAuth()
  const loc = useLocation()

  return (
    <>
      <AppBar position="fixed">
        <Toolbar>
          <Typography sx={{ flexGrow: 1 }} variant="h6">CoShift</Typography>
          <Button color="inherit" component={RouterLink} to="/" disabled={loc.pathname === '/'}>
            Übersicht
          </Button>
          <Button color="inherit" component={RouterLink} to="/admin" disabled={loc.pathname === '/admin'}>
            Admin
          </Button>
          {balance !== null && (
            <Typography sx={{ mx: 2 }}>{formatMinutes(balance)}</Typography>
          )}
          <Button color="inherit" startIcon={<LogoutIcon />} onClick={logout}>Logout</Button>
        </Toolbar>
      </AppBar>

      {/* Platz für Inhalt unter der AppBar */}
      <Toolbar/>  {/* push content under fixed AppBar */}
    </>
  )
}