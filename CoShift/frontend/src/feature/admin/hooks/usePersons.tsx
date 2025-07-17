
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import type { PersonDto } from '../../../types/person'
import { useAuth } from '../../auth/AuthContext'

export function usePersons() {
    const { header } = useAuth()
    const qc = useQueryClient()

    const persons = useQuery({
        queryKey: ['persons'],
        queryFn: async (): Promise<PersonDto[]> => {
            const res = await fetch('/api/persons', {
                headers: header ? { Authorization: header } : {},
            })
            if (!res.ok) throw new Error('Network error')
            return res.json()
        },
    })

    const addPerson = useMutation({
        mutationFn: async (body: Omit<PersonDto, 'id'>) => {
            const res = await fetch('/api/persons', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    ...(header ? { Authorization: header } : {}),
                },
                body: JSON.stringify(body),
            })
            return res.json()
        },
        onSuccess: () => qc.invalidateQueries({ queryKey: ['persons'] }),
    })

    const updatePerson = useMutation({
        mutationFn: async (p: PersonDto) => {
            const res = await fetch(`/api/persons/${p.id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    ...(header ? { Authorization: header } : {}),
                },
                body: JSON.stringify(p),
            })
            return res.json()
        },
        onSuccess: () => qc.invalidateQueries({ queryKey: ['persons'] }),
    })

    const deletePerson = useMutation({
        mutationFn: async (id: number) => {
            await fetch(`/api/persons/${id}`, {
                method: 'DELETE',
                headers: header ? { Authorization: header } : {},
            })
            return id
        },
        onSuccess: () => qc.invalidateQueries({ queryKey: ['persons'] }),
    })

    return { persons, addPerson, updatePerson, deletePerson }
}
