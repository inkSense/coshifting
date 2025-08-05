import { Routes, Route } from 'react-router-dom'
import Login            from '../features/auth/components/LoginForm'
import PrivateLayout    from '../layout/PrivateLayout'
import RequireAdmin     from '../layout/RequireAdmin'
import WeekPage         from '../pages/weekPage/WeekPage.tsx'
import DayPage          from '../pages/dayPage/DayPage.tsx'
import AdminPage        from '../pages/adminPage/AdminPage.tsx'
import ConfigureShiftsPage from '../pages/configureShifts/ConfigureShiftsPage.tsx'
import { AuthProvider, useAuth } from '../features/auth/AuthProvider'

function Routing() {
  
  const { authHeader, tryLogin } = useAuth()

  return (
    <>
      {authHeader ? (
        <Routes>
          <Route element={<PrivateLayout />}>
            <Route path="/" element={<WeekPage />} />
            <Route path="day/:date" element={<DayPage />} />
            <Route element={<RequireAdmin />}>           {/* ⬅︎ Guard */}
              <Route path="admin" element={<AdminPage />} />
              <Route path="configure-shifts" element={<ConfigureShiftsPage />} />
            </Route>
          </Route>
        </Routes>
      ) : (
        <Login onLogin={tryLogin} />
      ) }
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
