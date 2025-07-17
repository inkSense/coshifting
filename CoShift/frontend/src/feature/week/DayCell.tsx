import './WeekView.css'
import type { DayCellViewModel } from './WeekView'

export default function DayCell({ cell }: { cell: DayCellViewModel }) {

  return (
    <div className='week-cell'>
      {cell.shifts.length === 0 && <span className="empty">&nbsp;</span>}

      {cell.shifts.map((s, i) => (
        <div key={i} className={s.fullyStaffed ? 'shift-card full' : 'shift-card'}>
          <span>{s.startTime}</span>
        </div>
      ))}
    </div>
  )
}