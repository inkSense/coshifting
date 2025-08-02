import { render, screen, renderHook, act } from '@testing-library/react'
import { AuthProvider, useAuth } from './AuthProvider'
import type { ReactNode } from 'react'
import { describe, it, expect, vi, beforeEach } from 'vitest'
import { authFetch, ApiError } from '../../shared/api'

vi.mock('../../shared/api', async () => {
  const actual = await vi.importActual<typeof import('../../shared/api')>('../../shared/api')
  return { ...actual, authFetch: vi.fn() }
})

const authFetchMock = authFetch as unknown as vi.Mock

function Wrapper({ children }: { children: ReactNode }) {
  return <AuthProvider>{children}</AuthProvider>
}

describe('AuthProvider', () => {
  it('shows children when not authenticated', () => {
    render(<div data-testid="hello" />, { wrapper: Wrapper })
    expect(screen.getByTestId('hello')).toBeInTheDocument()
  })

  beforeEach(() => {
    authFetchMock.mockReset()
    sessionStorage.clear()
  })

  it('logs in successfully', async () => {
    authFetchMock.mockImplementation((url: string) => {
      if (url === '/api/shifts') return Promise.resolve({})
      if (url === '/api/persons/me/timeaccount') return Promise.resolve({ balanceInMinutes: 15 })
      return Promise.reject(new Error('unknown url'))
    })
    const { result } = renderHook(() => useAuth(), { wrapper: Wrapper })
    let ok = false
    await act(async () => {
      ok = await result.current.login('user', 'pass')
    })
    expect(ok).toBe(true)
    expect(result.current.isAuthenticated).toBe(true)
    expect(result.current.balance).toBe(15)
  })

  it('handles 401 error', async () => {
    authFetchMock.mockRejectedValue(new ApiError(401, 'unauthorized'))
    const { result } = renderHook(() => useAuth(), { wrapper: Wrapper })
    let ok = true
    await act(async () => {
      ok = await result.current.login('user', 'pass')
    })
    expect(ok).toBe(false)
    expect(result.current.isAuthenticated).toBe(false)
  })
})