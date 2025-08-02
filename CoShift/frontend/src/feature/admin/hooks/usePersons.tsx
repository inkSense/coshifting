
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import type { PersonDto } from '../../../types/person'
import { useAuth } from '../../auth/AuthProvider'
import { authFetch } from '../../../shared/api'

export function usePersons() {
    const { header } = useAuth()
    const qc = useQueryClient()

    const persons = useQuery({
        queryKey: ['persons'],
        queryFn: async (): Promise<PersonDto[]> => {
            if (!header) throw new Error('Unauthenticated')
            return authFetch<PersonDto[]>('/api/persons', header)
        },
    })

    const addPerson = useMutation({
        mutationFn: async (body: Omit<PersonDto, 'id'>) => {
            if (!header) throw new Error('Unauthenticated')
            return authFetch<PersonDto>('/api/persons', header, { method: 'POST', body: JSON.stringify(body) })
        },
        onSuccess: () => qc.invalidateQueries({ queryKey: ['persons'] }),
    })

    const updatePerson = useMutation({
        mutationFn: async (p: PersonDto) => {
            if (!header) throw new Error('Unauthenticated')
            return authFetch<PersonDto>(`/api/persons/${p.id}`, header, { method: 'PUT', body: JSON.stringify(p) })
        },
        onSuccess: () => qc.invalidateQueries({ queryKey: ['persons'] }),
    })

    const deletePerson = useMutation({
        mutationFn: async (id: number) => {
            if (!header) throw new Error('Unauthenticated')
            await authFetch(`/api/persons/${id}`, header, { method: 'DELETE' })
            return id
        },
        onSuccess: () => qc.invalidateQueries({ queryKey: ['persons'] }),
    })

    return { persons, addPerson, updatePerson, deletePerson }
}
