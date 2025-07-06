import '../WeekView.css'
import type { DayCellViewModel } from '../WeekView'

export default function DayCell({ cell }: { cell: DayCellViewModel }) {
  const cellClass =
    'week-cell' +
    (cell.hasShift
      ? (cell.fullyStaffed ? ' full' : ' not-full')
      : '')

  return (
    <div className={cellClass}>
      {cell.hasShift && (
        <div className="shift-card">
          <span>{cell.startTime}</span>
        </div>
      )}
    </div>
  )
}