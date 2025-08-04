import { Navigate, Outlet } from 'react-router-dom'
import { useAuth } from '../features/auth/AuthProvider'

export default function RequireAdmin() {
  const { isAdmin } = useAuth()         // kommt aus dem Auth-Context
  return isAdmin ? <Outlet />           // alles ok → untergeordnete Route rendern
                 : <Navigate to="/" />  // sonst zurück zur Übersicht
}