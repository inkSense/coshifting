import { Outlet } from 'react-router-dom'
import MainHeader from './MainHeader.tsx'

export default function PrivateLayout(){ 
     return (
       <>
         <MainHeader />     {/* AppBar + Drawer immer dabei */}
         <Outlet />     {/* geschützte Seite */}
       </>
     )
}