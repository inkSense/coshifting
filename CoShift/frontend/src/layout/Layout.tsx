import { useState } from 'react'
import { Toolbar } from '@mui/material'
import Header    from './Header'
import NavDrawer from './NavDrawer'

export default function Layout(){
  const [open,setOpen]=useState(false)

  return (
    <>
      <Header onMenuClick={()=>setOpen(true)}/>
      <NavDrawer open={open} onClose={()=>setOpen(false)}/>
      {/* Platz für Inhalt unter der AppBar */}
      <Toolbar/>
    </>
  )
}
