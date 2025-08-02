# CoShift Frontend

## Architekturüberblick (React)

```
AppProviders
 └─ RouterProvider (router.tsx)
     ├─ /login → <LoginForm/>
     └─ (RequireAuth)
        └─ <Layout/>
            ├─ Übersicht   → <WeekView/>
            └─ Admin       → <AdminPage/>
```

Layer:
1. shared/   → generische Utilities (`api.ts`, `NotificationProvider`)
2. feature/  → fachliche Features (auth, week, admin)
3. layout/   → AppBar & Drawer

## Login-Flow

- `AuthProvider` erzeugt aus Benutzername/Passwort einen Basic-Auth-Header und speichert ihn in `sessionStorage`.
- Ein Probeaufruf auf `/api/shifts` validiert die Credentials und lädt bei Erfolg das Zeitkonto.
- Netzfehler und `401` werden unterschieden: Bei `401` bleibt das Formular mit "Benutzername oder Passwort falsch", andere Fehler führen zu "Server nicht erreichbar".
- API-Aufrufe gehen entweder über den Dev-Proxy (`/api`) oder verwenden `VITE_API_URL` aus `.env` als Basis-URL.

## Dev-Skripte

```
# Projekt starten
npm run dev

# Unit-Tests (Vitest)
npm run test
```

## Test-Setup
- Vitest + React Testing Library (jsdom)
- Setup-Datei `src/test/setup.ts` registriert `jest-dom`
- Beispiel‐Test `AuthProvider.test.tsx` zeigt grundlegendes Rendern

## API-Konfiguration
- Dev-Proxy leitet `/api` an `http://localhost:8080` (siehe `vite.config.ts`).
- Alternativ setzt `.env` die Basis-URL via `VITE_API_URL`.
- Globale 401-Behandlung in `shared/api.ts` (Logout & Redirect)


