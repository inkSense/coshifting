import { useAuth } from '../feature/auth/AuthContext'

export default function useApiClient() {
  const { header } = useAuth()

  async function get<T>(url: string): Promise<T> {
    const res = await fetch(url, {
      headers: header ? { Authorization: header } : undefined,
    })
    if (!res.ok) {
      throw new Error('Network response was not ok')
    }
    return res.json() as Promise<T>
  }

  return { get }
}
