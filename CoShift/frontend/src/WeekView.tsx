import { useEffect, useState } from 'react'
import DayCell from './components/DayCell'
import './WeekView.css'          

// Typvertrag „Dieses JSON-Objekt muss die Felder hasShift, startTime, fullyStaffed enthalten“.
export interface DayCellViewModel {
  hasShift: boolean
  startTime: string
  fullyStaffed: boolean
}

interface Props {
  authHeader?: string
}

export default function WeekView({ authHeader }: Props) {
  const weeksToShow = 3;
  const EXPECTED = weeksToShow * 7;

  const [cells, setCells] = useState<DayCellViewModel[]>([])

  useEffect(() => {
    fetch(`/api/week?count=${weeksToShow}`, {
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
  // test
  const empty = { hasShift: false, startTime: '', fullyStaffed: false }
  const display = cells.length === EXPECTED
    ? cells
    : Array.from({ length: EXPECTED }, () => empty)

  return (
    <div className="week-grid">
      {days.map(day => (
        <div key={day} className="day-header">{day}</div>
      ))}

      {display.map((cell, idx) => (
        <DayCell key={idx} cell={cell} />
      ))}
    </div>
  )
}