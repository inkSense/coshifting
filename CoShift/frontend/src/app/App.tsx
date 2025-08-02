import './App.css'
import Login from '../feature/auth/LoginForm'
import WeekView from '../feature/week/WeekView'
import Layout from '../layout/Layout'
import PrivateLayout from '../layout/PrivateLayout'
import { AuthContext } from '../feature/auth/AuthContext'
import { Routes, Route, Navigate } from 'react-router-dom'
import AdminPage from '../feature/admin/AdminPage'
import useAuth from '../feature/auth/useAuth'

/**
 * Root-Komponente:
 * – Verwaltet den Auth-Status über einen Hook.
 * – Stellt Routen und den AuthContext bereit.
 */
export default function App() {
  const auth = useAuth()

  return (
    <AuthContext.Provider value={auth}>
      {auth.header && <Layout />}

      {!auth.header ? (
        <Login onLogin={auth.login} />
      ) : (
        <Routes>
          <Route element={<PrivateLayout />}>
            <Route path="/" element={<WeekView />} />
            <Route path="/admin" element={<AdminPage />} />
          </Route>
          <Route path="*" element={<Navigate to="/" />} />
        </Routes>
      )}
    </AuthContext.Provider>
  )
}

