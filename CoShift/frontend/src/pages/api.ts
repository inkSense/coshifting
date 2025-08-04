import { useAuth } from '../features/auth/AuthProvider.tsx'
import { useCallback, useMemo } from 'react'

// Hook providing helper functions for HTTP calls with optional auth header
export function useApi() {
  const { authHeader } = useAuth()

  const request = useCallback(async <T>(url: string, init: RequestInit = {}): Promise<T> => {
    const headers: HeadersInit = {
      ...(init.headers || {}),
      ...(authHeader ? { Authorization: authHeader } : {}),
    }

    const res = await fetch(url, { ...init, headers })
    if (!res.ok) throw new Error('Network error')

    if (res.status === 204) {
      // no content
      return undefined as T
    }

    return res.json() as Promise<T>
  }, [authHeader])

  return useMemo(() => ({
    get: <T>(url: string) => request<T>(url),
    post: <T>(url: string, body: unknown) =>
      request<T>(url, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body),
      }),
    put: <T>(url: string, body: unknown) =>
      request<T>(url, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body),
      }),
    del: <T>(url: string) => request<T>(url, { method: 'DELETE' }),
  }), [request])
}
