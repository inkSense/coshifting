import './Header.css'

interface Props {
  onLogout: () => void
  balance?: number
}

export default function Header({ onLogout, balance }: Props) {
  return (
    <header className="app-header">
      <h1>CoShift</h1>
      {balance !== undefined && (
        <span className="balance">{balance} min</span>
      )}

      {/* Button ganz rechts */}
      <button onClick={onLogout}>Logout</button>
    </header>
  )
}