import { Drawer, List, ListItemButton, ListItemIcon, ListItemText, Box } from '@mui/material'
import HomeIcon      from '@mui/icons-material/Home'
import AdminPanelIcon from '@mui/icons-material/AdminPanelSettings'
import { Link as RouterLink, useLocation } from 'react-router-dom'

type NavDrawerProps = {
  open: boolean
  onClose: () => void
}

export default function NavDrawer({ open, onClose }: NavDrawerProps){
  const loc = useLocation()
  const handleClose = () => onClose()

  return (
    <Drawer open={open} onClose={handleClose}>
      <Box sx={{width:240}} role="presentation" onClick={handleClose}>
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
  )
}
