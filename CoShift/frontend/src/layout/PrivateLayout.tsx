import { Navigate, Outlet } from 'react-router-dom'
import { useAuth } from '../feature/auth/AuthContext'
export default function PrivateLayout(){ return useAuth().header ? <Outlet/> : <Navigate to="/" replace/> }