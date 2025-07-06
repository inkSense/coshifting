import './Header.css'

interface Props {
  onLogout: () => void
}

export default function Header({ onLogout }: Props) {
  return (
    <header className="app-header">
      <h1>CoShift</h1>

      {/* Button ganz rechts */}
      <button onClick={onLogout}>Logout</button>
    </header>
  )
}