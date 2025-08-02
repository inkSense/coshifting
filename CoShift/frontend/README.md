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
- Basis‐URL via  `.env`  ⇒  `VITE_API_URL`
- Globale 401-Behandlung in `shared/api.ts` (Logout & Redirect)


