import { useEffect, useState } from 'react'
import './WeekView.css'          // Styles wiederverwenden

// View-Model, das der Backend-Endpoint /api/week liefert
export interface DayCellViewModel {
  hasShift: boolean
  startTime: string
  fullyStaffed: boolean
}

interface Props {
  authHeader?: string
}

export default function WeekView({ authHeader }: Props) {
  const [cells, setCells] = useState<DayCellViewModel[]>([])

  useEffect(() => {
    fetch('/api/week', {
      headers: authHeader ? { Authorization: authHeader } : {}
    })
      .then(res => {
        if (!res.ok) throw new Error('Network response was not ok')
        return res.json()
      })
      .then((data: DayCellViewModel[]) => setCells(data))
      .catch(err => console.error('Failed to load week data', err))
  }, [authHeader])

  // Kopfzeile
  const days = ['Mo', 'Di', 'Mi', 'Do', 'Fr', 'Sa', 'So']

  // Fallback: solange noch keine Daten da sind, leere Zellen anzeigen
  const display = cells.length === 7
    ? cells
    : Array.from({ length: 7 }, () => ({ hasShift: false, startTime: '', fullyStaffed: false }))

  return (
    <div className="week-grid">
      {days.map(d => (
        <div key={d} className="day-header">{d}</div>
      ))}

      {display.map((c, idx) => (
        <div key={idx} className="week-cell">
          {c.hasShift && (
            <div className="shift-card">
              <span>{c.startTime}</span>
              {c.fullyStaffed && <span> ✔</span>}
            </div>
          )}
        </div>
      ))}
    </div>
  )
}