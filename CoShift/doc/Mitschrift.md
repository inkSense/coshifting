# 20. Juni

## Was müsste sich ändern?

Nach der Modell­änderung betrifft die Umstellung im Wesentlichen alle Schichten, in denen

a) das alte `date`-Feld oder  
b) die getrennten Typen `LocalDate` / `LocalTime`  
verwendet wurden.

Im Projekt sind das nur wenige, klar abgegrenzte Stellen.  
Hier eine Checkliste von oben (Domäne) nach unten (DB, Web, Tests):

1. Domänen­objekt  
   • `Shift.java` durch die neue Version ersetzen (kein `date`, neue Felder/Getter/Konstruktor).  
   → Alles Weitere baut darauf auf.

2. Persistence-Layer  
   2.1 JPA-Entity `ShiftJpaEntity`  
       • Felder: `LocalDateTime startTime`, `LocalDateTime endTime`, `int durationInMinutes`.  
       • `date` entfernen, Getter/Setter anpassen.  
       • Tabelle anpassen: neue Spalten `start_time TIMESTAMP`, `end_time TIMESTAMP`, `duration_minutes INT`.  
   2.2 Mapper `ShiftMapper`  
       • `toEntity()` und `toDomain()` auf neues Modell umstellen  
         (endTime berechnen oder einfach mappen, `durationInMinutes` mitgeben).  
   2.3 Spring-Data-Repository  
       • Signatur ändern:  
         ```java
         interface SpringDataShiftJpaRepository
                 extends JpaRepository<ShiftJpaEntity, Long> {
             // Beispiel-Query nach Tag:
             List<ShiftJpaEntity> findByStartTimeBetween(LocalDateTime from, LocalDateTime to);
         }
         ```  
       • Methode `findByDate()` entfernen oder ersatzweise obige Range-Variante anbieten.  
   2.4 Adapter-Implementierung `ShiftRepositoryJpaImpl`  
       • Gleiche Umstellung wie beim Spring-Data-Repo (Methoden streichen/anpassen).  
       • Mapper-Aufrufe bleiben, aber nutzen jetzt die neue API.

3. Application-Layer  
   • `ShiftRepository`-Interface:  
     – Methode `findByDate(LocalDate)` entfernen oder durch eine Zeit­raum-Suche ersetzen  
       (`findStartingBetween(LocalDateTime from, LocalDateTime to)`).  
   • `ShiftService` selbst benötigt keine Änderung, solange nur `findAll()`, `save()`, `deleteById()` benutzt werden.

4. Web-Schicht  
   • `ShiftController`  
     – Demo-Schicht jetzt mit `LocalDateTime`:  
       ```java
       new ShiftJpaEntity(
            LocalDateTime.now().withHour(18).withMinute(0),
            LocalDateTime.now().withHour(21).withMinute(0),
            180)
       ```  
     – oder (sauberer) erst eine Domain-`Shift` erstellen und über Mapper in die Entity wandeln.  
   • Falls das Frontend das JSON direkt nutzt, dort gleichfalls Felder von `date`/`startTime`→`startTime` (Datum + Uhrzeit) umstellen.

5. Tests  
   • Alle Konstruktor-Aufrufe anpassen  
     – `new Shift(null, startDateTime, 180)` statt bisheriger vier Parameter.  
   • Assertions auf `getDate()` entfernen und ggf. auf `getStartTime().toLocalDate()` ausweichen.  
   • Tests, die `findByDate()` benutzen, auf den neuen Repository-Call umstellen.

6. Datenbank / Migration  
   • DDL-Änderung via Flyway/Liquibase oder JPA-`ddl-auto=update`:  
     – Spalte `shift_date` löschen.  
     – Neue Spalten `start_time TIMESTAMP`, `end_time TIMESTAMP`, `duration_minutes INT` hinzufügen.  
   • Eventuell bestehende Daten per Skript migrieren (`start_time = shift_date + start_time`, etc.).

7. Sonstiges  
   • Suche nach `.getDate()` und `findByDate(` im Code -> entfernen/ersetzen.  
   • IDE- oder Build-Fehler nach dem ersten Kompilieren kurz durchgehen – sie zeigen genau an, wo noch alte Felder referenziert werden.

Mit diesen Änderungen bleibt der Build grün und die Anwendung verhält sich wie zuvor, nur auf Basis der neuen, flexibleren Zeitrepräsentation.


### Wie würde ShiftJpaEntity also aussehen?

## So könnte die überarbeitete JPA-Entity aussehen:

```java:CoShift/src/main/java/org/coshift/d_frameworks/db/ShiftJpaEntity.java
// ... existing code ...
import java.time.LocalDateTime;
// ... existing code ...

/**
 * 1-zu-1-Abbild der DB-Tabelle „shift“ – jetzt mit Start/End als LocalDateTime
 * und expliziter Dauer (Minuten).
 */
// ... existing code ...
public class ShiftJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "duration_minutes", nullable = false)
    private int durationInMinutes;

    /* -------- JPA braucht einen No-Args-Ctor -------- */
    protected ShiftJpaEntity() { }

    public ShiftJpaEntity(LocalDateTime startTime,
                          LocalDateTime endTime,
                          int durationInMinutes) {
        this.startTime         = startTime;
        this.endTime           = endTime;
        this.durationInMinutes = durationInMinutes;
    }

    /* -------- Getter/Setter -------- */
    public Long          getId()               { return id; }
    public LocalDateTime getStartTime()        { return startTime; }
    public LocalDateTime getEndTime()          { return endTime; }
    public int           getDurationInMinutes(){ return durationInMinutes; }

    public void setStartTime(LocalDateTime start)   { this.startTime = start; }
    public void setEndTime(LocalDateTime end)       { this.endTime   = end; }
    public void setDurationInMinutes(int minutes)   { this.durationInMinutes = minutes; }
}
// ... existing code ...
```

Wesentliche Änderungen:

1. `LocalDateTime startTime` und `endTime` ersetzen das alte `date`-/`LocalTime`-Paar.  
2. Neues Feld `durationInMinutes` speichert die berechnete Dauer explizit.  
3. Tabelle erhält entsprechend drei Spalten: `start_time`, `end_time`, `duration_minutes`.

### Wie würde also sollten SpringDataShiftJpaRepository und seine Implementierung aussehen?


## Hier die angepassten Klassen – jeweils nur die veränderten Ausschnitte.

1. Spring-Data-Repository  
```java:CoShift/src/main/java/org/coshift/d_frameworks/db/SpringDataShiftJpaRepository.java
// ... existing code ...
import java.time.LocalDateTime;
// ... existing code ...

public interface SpringDataShiftJpaRepository
        extends JpaRepository<ShiftJpaEntity, Long> {

    /**
     * Liefert alle Schichten, deren Startzeit in den angegebenen Bereich fällt.
     * (Beispiel – weitere Query-Methoden je nach Bedarf ergänzen.)
     */
    List<ShiftJpaEntity> findByStartTimeBetween(LocalDateTime from, LocalDateTime to);
}
// ... existing code ...
```

2. Adapter-Implementierung  
```java:CoShift/src/main/java/org/coshift/c_adapter/persistence/ShiftRepositoryJpaImpl.java
// ... existing imports ...
import java.time.LocalDateTime;
// ... existing code ...

@Repository
@Transactional
public class ShiftRepositoryJpaImpl implements ShiftRepository {

    private final SpringDataShiftJpaRepository jpa;

    public ShiftRepositoryJpaImpl(SpringDataShiftJpaRepository jpa) {
        this.jpa = jpa;
    }

    // ... save, findById, findAll, deleteById bleiben unverändert ...

    @Override
    public List<Shift> findStartingBetween(LocalDateTime from, LocalDateTime to) {
        return jpa.findByStartTimeBetween(from, to)
                  .stream()
                  .map(ShiftMapper::toDomain)
                  .toList();
    }
}
// ... existing code ...
```

Wichtig:

• Das Application-Interface `ShiftRepository` muss jetzt statt `findByDate` die neue Methode  
  `List<Shift> findStartingBetween(LocalDateTime from, LocalDateTime to);` anbieten.  
• Alle Aufrufe (Service-Klasse, Tests …) passen Sie entsprechend an.

# 21. Juni

Als nächstes müsste ich noch mal an c_adapters.ShiftJsonAdapter ran. Das ist vielleicht doch eher ein Mapper! Es sollten die Domänenobjekte oberhalb von hier nicht vorkommen. Im Moment scheint mir d_frameworks.json.ShiftJsonMapper falsch zu sein. 

# 22. Juni

Deleted all Files that need Spring-JPA. Changed the whole project towards JSON-based repository solutions.

Added Person. Added related UseCases. Added related DTO and Mapper in adapters-package. Added PersonJsonRepository in adapters-package. Added PersonGsonFileAccessor in frameworks-package.

Added Shift. Added related UseCases. Added related DTO and Mapper in adapters-package. Added ShiftJsonRepository in adapters-package. Added ShiftGsonFileAccessor in frameworks-package.

Added TimeBalance and TimeTransaction in domain-package. 

# 2. Juli

Analysiere das Projekt auf seine Testabdeckung. Identifiziere den wichtigsten fehlenden Testfall, der die Codequalität oder Zuverlässigkeit am meisten verbessern würde. Implementiere genau einen sinnvollen Unit-Test oder Integrationstest dafür. Gib ausschließlich den vollständigen Code des Tests zurück.