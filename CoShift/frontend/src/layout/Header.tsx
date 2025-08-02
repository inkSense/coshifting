import { AppBar, Toolbar, IconButton, Typography, Button } from '@mui/material'
import MenuIcon      from '@mui/icons-material/Menu'
import LogoutIcon    from '@mui/icons-material/Logout'
import { useAuth }   from '../feature/auth/AuthContext'

type HeaderProps = {
  onMenuClick: () => void
}

export default function Header({ onMenuClick }: HeaderProps){
  const { logout, balance } = useAuth()

  return (
    <AppBar position="fixed">
      <Toolbar>
        <IconButton color="inherit" edge="start" onClick={onMenuClick}>
          <MenuIcon/>
        </IconButton>
        <Typography sx={{flexGrow:1}} variant="h6">CoShift</Typography>
        {balance!==null && <Typography sx={{mr:2}}>{balance} min</Typography>}
        <Button color="inherit" startIcon={<LogoutIcon/>} onClick={logout}>Logout</Button>
      </Toolbar>
    </AppBar>
  )
}
