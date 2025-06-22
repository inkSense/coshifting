package org.coshift.a_domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class Shift {

    private final Long            id;
    private final LocalDateTime   startTime;
    private final LocalDateTime   endTime;
    private final long             durationInMinutes;
    private List<Person>          persons;
    private final int             capacity;

    public Shift(Long id, LocalDateTime startTime, int durationInMinutes, int capacity) {
        this.id                = id;
        this.startTime         = startTime;
        this.durationInMinutes = durationInMinutes;
        this.endTime           = startTime.plusMinutes(durationInMinutes);
        this.persons           = new ArrayList<>();
        this.capacity          = capacity;
    }

    public Shift(Long id, LocalDateTime startTime, int durationInMinutes, int capacity, List<Person> persons) {
        this.id                = id;
        this.startTime         = startTime;
        this.durationInMinutes = durationInMinutes;
        this.endTime           = startTime.plusMinutes(durationInMinutes);
        this.persons           = persons;
        this.capacity          = capacity;
    }

    public Long            getId()               { return id; }
    public LocalDateTime   getStartTime()        { return startTime; }
    public LocalDateTime   getEndTime()          { return endTime; }
    public long            getDurationInMinutes(){ return durationInMinutes; }
    public List<Person>    getPersons()          { return persons; }
    public int             getCapacity()       { return capacity; }

    public void addPerson(Person person) {
        persons.add(person);
    }

    public void removePerson(Person person) {
        persons.remove(person);
    }

    public boolean isFull() {
        return persons.size() >= capacity;
    }

    /* -------- Geschäftslogik -------- */
    public boolean overlaps(Shift other) {
        return !(this.endTime.isBefore(other.startTime) ||
                 this.startTime.isAfter(other.endTime));
    }
}