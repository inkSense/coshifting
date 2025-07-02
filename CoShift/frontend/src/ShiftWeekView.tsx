import { useEffect, useMemo, useState } from 'react'
import './ShiftWeekView.css'

export interface Shift {
  id: number
  startTime: string   // ISO-String
}

interface Props {
  cutoff?: Date                    // default = heute
}

export default function WeekView({ cutoff = today() }: Props) {
  const [shifts, setShifts] = useState<Shift[]>([])

  /* --- Daten holen ---------------------------------------------------- */
  useEffect(() => {
    fetch('/api/shifts')
      .then(r => r.json())
      .then(setShifts)
      .catch(console.error)
  }, [])

  /* --- Shifts gruppieren --------------------------------------------- */
  const { weeks, sortedKeys } = useMemo(() => {
    const grid = new Map<string, Map<number, Shift[]>>()     // Map<KW, Map<TagsIndex, Shift[]>>

    shifts
      .filter(s => new Date(s.startTime) >= cutoff)
      .forEach(s => {
        const d       = new Date(s.startTime)
        const weekKey = isoWeekKey(d)
        const dayIdx  = jsDayToMonday0(d.getDay())          // 0 = Mo

        if (!grid.has(weekKey)) grid.set(weekKey, new Map())
        const dayMap = grid.get(weekKey)!
        if (!dayMap.has(dayIdx)) dayMap.set(dayIdx, [])
        dayMap.get(dayIdx)!.push(s)
      })

    const keys = Array.from(grid.keys()).sort()             // chronologisch
    return { weeks: grid, sortedKeys: keys }
  }, [shifts, cutoff])

  /* --- Render --------------------------------------------------------- */
  const days = ['Mo', 'Di', 'Mi', 'Do', 'Fr', 'Sa', 'So']

  return (
    <div className="week-grid">
      {/* Kopfzeile */}
      {days.map(d => (
        <div key={d} className="day-header">{d}</div>
      ))}

      {/* eine Zeile pro KW */}
      {sortedKeys.map(kw => (
        days.map((_, dayIdx) => {
          const cellShifts = weeks.get(kw)?.get(dayIdx) ?? []
          return (
            <div key={kw + dayIdx} className="week-cell">
              {cellShifts.slice(0, 1).map(s =>
                <div key={s.id} className="shift-card">
                  {new Date(s.startTime).toLocaleTimeString([], {hour:'2-digit', minute:'2-digit'})}
                </div>
              )}
            </div>
          )
        })
      ))}
    </div>
  )
}

/* --- Hilfsfunktionen -------------------------------------------------- */
function today() {
  const now = new Date()
  return new Date(now.getFullYear(), now.getMonth(), now.getDate())
}

function jsDayToMonday0(jsDay: number) {
  return (jsDay + 6) % 7   // JS: 0=So; wir: 0=Mo
}

// ISO-Woche als "YYYY-W##"
function isoWeekKey(d: Date) {
  const tmp = new Date(d.getTime())
  tmp.setHours(0, 0, 0, 0)
  tmp.setDate(tmp.getDate() + 3 - jsDayToMonday0(tmp.getDay()))  // Do der KW
  const week1 = new Date(tmp.getFullYear(), 0, 4)
  const weekNo = 1 + Math.round(((tmp.getTime() - week1.getTime()) / 86400000 - 3 + jsDayToMonday0(week1.getDay())) / 7)
  return `${tmp.getFullYear()}-W${String(weekNo).padStart(2,'0')}`
}