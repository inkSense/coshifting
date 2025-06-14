package org.coshift.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "shift")
public class Shift {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "shift_date")
    private LocalDate date;          // oder: private LocalDate shiftDate;

    @Column(name = "start_time")
    private LocalTime start;         // oder: private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime end;

    public Shift() {
    }

    public Shift(Long id, LocalDate now, LocalTime of, LocalTime of1) {
        this.id = id;
        this.date = now;
        this.start = of;
        this.end = of1;
    }


}
