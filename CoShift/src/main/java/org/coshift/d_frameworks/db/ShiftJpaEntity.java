package org.coshift.d_frameworks.db;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 1-zu-1-Abbild der DB-Tabelle „shift“.
 * JPA-Annotationen sind hier lokalisiert,
 * Domain-Code bleibt davon unberührt.
 */
@Entity
@Table(name = "shift")
public class ShiftJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "shift_date", nullable = false)
    private LocalDate date;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    /* -------- JPA braucht einen No-Args-Ctor -------- */
    protected ShiftJpaEntity() { }

    public ShiftJpaEntity(LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.date      = date;
        this.startTime = startTime;
        this.endTime   = endTime;
    }

    /* -------- Getter/Setter -------- */
    public Long getId()               { return id; }
    public LocalDate getDate()        { return date; }
    public LocalTime getStartTime()   { return startTime; }
    public LocalTime getEndTime()     { return endTime; }

    public void setDate(LocalDate date)         { this.date = date; }
    public void setStartTime(LocalTime start)   { this.startTime = start; }
    public void setEndTime(LocalTime end)       { this.endTime = end; }
}
