// package org.coshift.d_frameworks.db;

// import jakarta.persistence.*;
// import java.time.LocalDateTime;

// /**
//  * 1-zu-1-Abbild der DB-Tabelle „shift“.
//  * JPA-Annotationen sind hier lokalisiert,
//  * Domain-Code bleibt davon unberührt.
//  */
// @Entity
// @Table(name = "shift")
// public class ShiftJpaEntity {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     @Column(name = "start_time", nullable = false)
//     private LocalDateTime startTime;

//     @Column(name = "end_time", nullable = false)
//     private LocalDateTime endTime;

//     @Column(name = "duration_minutes", nullable = false)
//     private int durationInMinutes;

//     /* -------- JPA braucht einen No-Args-Ctor -------- */
//     protected ShiftJpaEntity() { }

//     public ShiftJpaEntity(LocalDateTime startTime,
//                           LocalDateTime endTime,
//                           int durationInMinutes) {
//         this.startTime         = startTime;
//         this.endTime           = endTime;
//         this.durationInMinutes = durationInMinutes;
//     }

//     /* -------- Getter/Setter -------- */
//     public Long          getId()               { return id; }
//     public LocalDateTime getStartTime()        { return startTime; }
//     public LocalDateTime getEndTime()          { return endTime; }
//     public int           getDurationInMinutes(){ return durationInMinutes; }

//     public void setStartTime(LocalDateTime start)   { this.startTime = start; }
//     public void setEndTime(LocalDateTime end)       { this.endTime   = end; }
//     public void setDurationInMinutes(int minutes)   { this.durationInMinutes = minutes; }
// }