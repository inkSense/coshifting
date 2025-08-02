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
  const { header, login, balance, logout } = useAuth()

  return (
    <AuthContext.Provider value={{ header, balance, logout }}>
      {header && <Layout />}

      {!header ? (
        <Login onLogin={login} />
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

