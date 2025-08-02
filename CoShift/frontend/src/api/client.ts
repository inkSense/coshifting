import { useAuth } from '../feature/auth/AuthContext'

// Generic request helper with central error handling
async function request<T>(
  method: string,
  url: string,
  {
    body,
    headers: customHeaders,
    ...options
  }: RequestInit & { body?: unknown } = {}
): Promise<T> {
  const headers: HeadersInit = {
    ...(body ? { 'Content-Type': 'application/json' } : {}),
    ...(customHeaders || {}),
  }

  try {
    const res = await fetch(url, {
      ...options,
      method,
      headers,
      body: body ? JSON.stringify(body) : undefined,
    })

    if (!res.ok) {
      const msg = `Request failed with status ${res.status}`
      console.error(msg)
      if (typeof window !== 'undefined') window.alert(msg)
      throw new Error(msg)
    }

    if (res.status === 204) return null as T
    return res.json() as Promise<T>
  } catch (err) {
    console.error('Network error', err)
    if (typeof window !== 'undefined') window.alert('Network error')
    throw err
  }
}

// Base client without automatic auth header
export const client = {
  get: <T>(url: string, options?: RequestInit) => request<T>('GET', url, options),
  post: <T>(url: string, body?: unknown, options?: RequestInit) =>
    request<T>('POST', url, { ...options, body }),
  put: <T>(url: string, body?: unknown, options?: RequestInit) =>
    request<T>('PUT', url, { ...options, body }),
  del: <T>(url: string, options?: RequestInit) => request<T>('DELETE', url, options),
}

// Hook that injects Authorization header from useAuth
export function useApiClient() {
  const { header } = useAuth()

  const withAuth = (options?: RequestInit & { body?: unknown }) => ({
    ...options,
    headers: {
      ...(header ? { Authorization: header } : {}),
      ...(options?.headers || {}),
    },
  })

  return {
    get: <T>(url: string, options?: RequestInit) =>
      client.get<T>(url, withAuth(options)),
    post: <T>(url: string, body?: unknown, options?: RequestInit) =>
      client.post<T>(url, body, withAuth(options)),
    put: <T>(url: string, body?: unknown, options?: RequestInit) =>
      client.put<T>(url, body, withAuth(options)),
    del: <T>(url: string, options?: RequestInit) =>
      client.del<T>(url, withAuth(options)),
  }
}
