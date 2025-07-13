import { useEffect, useState } from 'react'
import DayCell from './DayCell.tsx'
import './WeekView.css'          
import { useAuth } from '../auth/AuthContext'

export interface ShiftCellVM {
  startTime: string
  fullyStaffed: boolean
}

export interface DayCellViewModel {
  shifts: ShiftCellVM[]
}

export default function WeekView() {
  const { header: authHeader } = useAuth()
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
  const empty: DayCellViewModel = { shifts: [] }
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