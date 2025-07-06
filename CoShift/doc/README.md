# CoShift – Shift Management for a Cooperative Supermarket

CoShift is an open-source project.
Its goal is to help a **co-operative supermarket with ~500 members** to plan
and book the mandatory **work shifts** every member performs each
month.  Reduced staffing costs translate directly into lower product prices.

The long-term vision is:

* Web application (✔ in progress)  
* Optional mobile app (planned)  


---

## Current Walking Skeleton

The minimal slice already works end-to-end:

1. A member logs in with HTTP Basic Auth.  
2. After successful authentication the web client shows  
   * the current week (7 days)  
   * shifts per day  
   * a sign if the shift is fully staffed.  
3. All data is stored in **JSON files** – project can evolve quickly to a relational database.

---

## Tech Stack

Backend  
• Java 17 • Spring Boot 3.5 • Spring Security • Maven • JUnit 5 • Gson/Jackson

Frontend  
• React 19 • TypeScript 5 • Vite • ESLint

Tooling  
• Git • Docker (planned)

---

## Architecture at a Glance

CoShift follows **Clean / Hexagonal Architecture** with four source packages:

```text
org.coshift
├─ domain // business objects (Shift, Person, …)
├─ application // use-cases
├─ adapters // web, persistence, mapping, …
└─ frameworks // Spring wiring, Gson file access
```
Key points  
* Domain and application core are **framework-agnostic**.  
* Controllers convert HTTP requests to use-case calls, DTOs map domain
  objects to JSON payloads.

---

## Project Structure
```text
CoShift/
├── src/
│ ├── main/java/
│ │ └── org/coshift/… # (see above)
│ ├── main/resources/
│ │ └── application.yml # Spring Boot config
│ └── test/java/ 
└── frontend/ # React client (Vite)
```

---

## API Overview (so far)

| Method | Path        | Description                              | Auth |
|--------|-------------|------------------------------------------|------|
| GET    | `/api/shifts` | Returns all shifts as JSON           | ✔ |
| GET    | `/api/week`  | Returns a `DayCellViewModel` for UI  | ✔ |


---

## Build & Run 

Prerequisites  
* Java 17+  
* Maven 3.9+  
* Node 20+ (for the React client)

1. **Start the backend**

   ```bash
   cd CoShift
   mvn spring-boot:run          # http://localhost:8080
   ```

2. **Start the frontend (in a second terminal)**

   ```bash
   cd CoShift/frontend
   npm install
   npm run dev                  # http://localhost:5173
   ```

3. **Login**

   For a quick demo add a member directly in `persons.json` or via a unit
   test that calls `UseCaseInteractor.addPerson(nickname, password)`.

---

## Tests



CoShift is developed **test-first**.  Every new bug requires a failing
regression test before it is fixed.

---

## Data Persistence

JSON first – Domain objects are mapped to lightweight DTOs and written to the
  `data/` directory via `Gson`.  


---

## Roadmap

| Milestone | Details |
|-----------|---------|
| v0.1      | Walking skeleton (login + week view)  ✔ |
| v0.2      | Book / cancel a shift |
| v0.3      | Member time account (balance, transactions) |
| v0.4      | Replace BasicAuth with token (JWT / Session) |
| v0.5      | Mobile-first UI, PWA packaging |
| v1.0      | Switch persistence to relational DB (PostgreSQL) |

---

## Contributing

This project is a learning playground.  
Feedback, pull requests and architecture discussions are welcome!

* Fork → Create feature branch → Write failing test → Code → PR  
* Use conventional commits; every change must be covered by tests.  

---

## License

CoShift is distributed under the Apache License 2.0.