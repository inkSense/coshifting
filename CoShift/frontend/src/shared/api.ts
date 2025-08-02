const BASE_URL = import.meta.env.VITE_API_URL ?? ''

export class ApiError extends Error {
  constructor(public status: number, message: string) {
    super(message)
  }
}

export const apiFetch = async <T>(url: string, init: RequestInit = {}): Promise<T> => {
  let res: Response
  try {
    res = await fetch(BASE_URL + url, init)
  } catch (err) {
    // Network/connection error – wrap into ApiError with status 0
    throw new ApiError(0, (err as Error).message)
  }
  if (!res.ok) {
    const errMsg = `API error ${res.status}`
    // 401: Token invalid – entferne lokale Auth-Daten und leite um
    if (res.status === 401) {
      sessionStorage.removeItem('authHeader')
      // Soft-Redirect, ohne Full-Reload
      if (window.location.pathname !== '/login') {
        window.location.replace('/login')
      }
    }
    throw new ApiError(res.status, errMsg)
  }
  // DELETE liefert oft keinen Body
  if (res.status === 204) return undefined as unknown as T
  return res.json() as Promise<T>
}

export const authFetch = async <T>(url: string, header: string, init: RequestInit = {}): Promise<T> => {
  const method = init.method?.toUpperCase() || 'GET'
  const headers: Record<string, string> = {
    ...(init.headers as Record<string, string> | undefined),
    Authorization: header,
  }
  if (method !== 'GET' && method !== 'HEAD') {
    headers['Content-Type'] = headers['Content-Type'] ?? 'application/json'
  }

  return apiFetch<T>(url, { ...init, headers })
} 