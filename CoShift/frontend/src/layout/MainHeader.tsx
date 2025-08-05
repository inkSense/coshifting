import { useState } from 'react'
import { AppBar, Toolbar, IconButton, Typography, Drawer, List, ListItemButton, ListItemIcon, ListItemText, Box, Button } from '@mui/material'
import MenuIcon      from '@mui/icons-material/Menu'
import HomeIcon      from '@mui/icons-material/Home'
import AdminPanelIcon from '@mui/icons-material/AdminPanelSettings'
import SettingsIcon from '@mui/icons-material/Settings'
import LogoutIcon    from '@mui/icons-material/Logout'
import { Link as RouterLink, useLocation } from 'react-router-dom'
import { useAuth }   from '../features/auth/AuthProvider'

function formatMinutes(total: number): string {
  const hours = Math.floor(total / 60)
  const minutes = total % 60
  return `${hours}h ${minutes}m`
}

export default function MainHeader(){
  const [open,setOpen]=useState(false)
  const { logout, balance, isAdmin } = useAuth()
  const loc = useLocation()

  return (
    <>
      {/* Header */}
      <AppBar position="fixed">
        <Toolbar>
          <IconButton color="inherit" edge="start" onClick={()=>setOpen(true)}>
            <MenuIcon/>
          </IconButton>
          <Typography sx={{flexGrow:1}} variant="h6">CoShift</Typography>
          {balance!==null && (
            <Typography sx={{ mr: 2 }}>{formatMinutes(balance)}</Typography>
          )}
          <Button color="inherit" startIcon={<LogoutIcon/>} onClick={logout}>Logout</Button>
        </Toolbar>
      </AppBar>

      {/* Drawer */}
      <Drawer open={open} onClose={()=>setOpen(false)}>
        <Box sx={{width:240}} role="presentation" onClick={()=>setOpen(false)}>
          <List>
            <ListItemButton component={RouterLink} to="/" selected={loc.pathname==='/'}>
              <ListItemIcon><HomeIcon/></ListItemIcon>
              <ListItemText primary="Übersicht"/>
            </ListItemButton>
            {isAdmin && (
              <>
                <ListItemButton component={RouterLink} to="/admin" selected={loc.pathname==='/admin'}>
                  <ListItemIcon><AdminPanelIcon/></ListItemIcon>
                  <ListItemText primary="Admin"/>
                </ListItemButton>
                <ListItemButton component={RouterLink} to="/configure-shifts" selected={loc.pathname==='/configure-shifts'}>
                  <ListItemIcon><SettingsIcon/></ListItemIcon>
                  <ListItemText primary="Configure Shifts"/>
                </ListItemButton>
              </>
            )}
          </List>
        </Box>
      </Drawer>

      {/* Platz für Inhalt unter der AppBar */}
      <Toolbar/>  {/* push content under fixed AppBar */}
    </>
  )
} 