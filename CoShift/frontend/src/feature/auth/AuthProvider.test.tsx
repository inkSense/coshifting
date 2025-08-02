import { render, screen } from '@testing-library/react'
import { AuthProvider } from './AuthProvider'
import type { ReactNode } from 'react'
import { describe, it, expect } from 'vitest'

function Wrapper({ children }: { children: ReactNode }) {
  return <AuthProvider>{children}</AuthProvider>
}

describe('AuthProvider', () => {
  it('shows children when not authenticated', () => {
    render(<div data-testid="hello" />, { wrapper: Wrapper })
    expect(screen.getByTestId('hello')).toBeInTheDocument()
  })
}) 