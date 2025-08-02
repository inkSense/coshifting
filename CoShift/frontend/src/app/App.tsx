import './App.css'
import Login       from '../feature/auth/LoginForm'
import WeekView    from '../feature/week/WeekView'
import Layout      from '../layout/Layout'
import PrivateLayout from '../layout/PrivateLayout'
import { AuthContext } from '../feature/auth/AuthContext'
import { Routes, Route, Navigate } from 'react-router-dom'
import AdminPage   from '../feature/admin/AdminPage'
import { useAuthProvider } from '../feature/auth/useAuthProvider'

export default function App() {
  const auth = useAuthProvider()

  return (
    <AuthContext.Provider value={auth}>
      {auth.header && <Layout />}

      {!auth.header ? (
        <Login />
      ) : (
        <Routes>
          <Route element={<PrivateLayout />}>
            <Route path="/"      element={<WeekView />} />
            <Route path="/admin" element={<AdminPage />} />
          </Route>
          <Route path="*" element={<Navigate to="/" />} />
        </Routes>
      )}
    </AuthContext.Provider>
  )
}
