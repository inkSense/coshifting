import { createBrowserRouter, Navigate } from 'react-router-dom'
import LoginForm from '../feature/auth/LoginForm'
import Layout from '../layout/Layout'
import WeekView from '../feature/week/WeekView'
import AdminPage from '../feature/admin/AdminPage'
import { useAuth } from '../feature/auth/AuthProvider'
import type { ReactElement } from 'react'

function RequireAuth({ children }: { children: ReactElement }) {
  const { isAuthenticated } = useAuth()
  if (!isAuthenticated) return <Navigate to="/login" replace />
  return children
}

export const router = createBrowserRouter([
  { path: '/login', element: <LoginForm /> },
  {
    element: (
      <RequireAuth>
        <Layout />
      </RequireAuth>
    ),
    children: [
      { index: true, element: <WeekView /> },
      { path: 'admin', element: <AdminPage /> },
      { path: '*', element: <Navigate to="/" replace /> },
    ],
  },
]) 