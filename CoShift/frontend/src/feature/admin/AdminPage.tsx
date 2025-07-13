import { useAuth } from '../auth/AuthContext'
export default function AdminPage() {
  const { header } = useAuth()
  return (
    <div style={{padding:'1rem'}}>
      <h2>Admin-Konfiguration</h2>
      <small>Token: {header}</small>
    </div>
  )
}