import { useEffect, useState } from 'react'
import './App.css'

interface Shift {
  id: number
  startTime: string
}

export default function ShiftWeekView() {
  const [shifts, setShifts] = useState<Shift[]>([])

  useEffect(() => {
    fetch('/api/shifts')
      .then(res => res.json())
      .then(data => setShifts(data))
      .catch(err => console.error('Failed to fetch shifts', err))
  }, [])

  const days = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']

  const shiftsByDay = days.map((_, i) =>
    shifts.filter(s => {
      const date = new Date(s.startTime)
      const jsDay = date.getDay() // 0 Sun, 1 Mon, ...
      const dayIndex = (jsDay + 6) % 7 // convert so Monday=0
      return dayIndex === i
    })
  )

  return (
    <div className="week-grid">
      {days.map((day, idx) => (
        <div key={day} className="day-column">
          <h3>{day}</h3>
          {shiftsByDay[idx].map(shift => (
            <div key={shift.id} className="shift-card">
              <div>ID: {shift.id}</div>
              <div>{new Date(shift.startTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}</div>
            </div>
          ))}
        </div>
      ))}
    </div>
  )
}
