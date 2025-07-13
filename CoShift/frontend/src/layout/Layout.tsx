import { useState } from 'react'
import { AppBar, Toolbar, IconButton, Typography, Drawer, List, ListItemButton, ListItemIcon, ListItemText, Box, Button } from '@mui/material'
import MenuIcon      from '@mui/icons-material/Menu'
import HomeIcon      from '@mui/icons-material/Home'
import AdminPanelIcon from '@mui/icons-material/AdminPanelSettings'
import LogoutIcon    from '@mui/icons-material/Logout'
import { Link as RouterLink, useLocation } from 'react-router-dom'
import { useAuth }   from '../feature/auth/AuthContext'

export default function Layout(){
  const [open,setOpen]=useState(false)
  const { logout, balance } = useAuth()
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
          {balance!==null && <Typography sx={{mr:2}}>{balance} min</Typography>}
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
            <ListItemButton component={RouterLink} to="/admin" selected={loc.pathname==='/admin'}>
              <ListItemIcon><AdminPanelIcon/></ListItemIcon>
              <ListItemText primary="Admin"/>
            </ListItemButton>
          </List>
        </Box>
      </Drawer>

      {/* Platz für Inhalt unter der AppBar */}
      <Toolbar/>  {/* push content under fixed AppBar */}
    </>
  )
} 