import './App.css'
import Login       from '../features/auth/components/LoginForm'
import WeekPage    from '../pages/WeekPage'
import Layout      from '../layout/Layout'
import PrivateLayout from '../layout/PrivateLayout'
import { Routes, Route, Navigate } from 'react-router-dom'
import AdminPage   from '../pages/AdminPage'
import { AuthProvider } from '../features/auth/AuthProvider'
import { useAuth } from '../features/auth/hooks/AuthContext'

function Routing() {
  const { header, tryLogin } = useAuth()

  return (
    <>
      {header && <Layout />}

      {!header ? (
        <Login onLogin={tryLogin} />
      ) : (
        <Routes>
          <Route element={<PrivateLayout />}>
            <Route path="/"      element={<WeekPage />} />
            <Route path="/admin" element={<AdminPage />} />
          </Route>
          <Route path="*" element={<Navigate to="/" />} />
        </Routes>
      )}
    </>
  )
}

export default function App() {
  return (
    <AuthProvider>
      <Routing />
    </AuthProvider>
  )
}
