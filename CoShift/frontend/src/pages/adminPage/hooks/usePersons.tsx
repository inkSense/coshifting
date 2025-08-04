
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import type { PersonDto } from '../types/person.ts'
import { useApi } from '../../api.ts'

export function usePersons() {
    const api = useApi()
    const qc = useQueryClient()

    const persons = useQuery({
        queryKey: ['persons'],
        queryFn: (): Promise<PersonDto[]> => api.get('/api/persons'),
    })

    const addPerson = useMutation({
        mutationFn: (body: Omit<PersonDto, 'id'>) => api.post<PersonDto>('/api/persons', body),
        onSuccess: () => qc.invalidateQueries({ queryKey: ['persons'] }),
    })

    const updatePerson = useMutation({
        mutationFn: (p: PersonDto) => api.put<PersonDto>(`/api/persons/${p.id}`, p),
        onSuccess: () => qc.invalidateQueries({ queryKey: ['persons'] }),
    })

    const deletePerson = useMutation({
        mutationFn: (id: number) => api.del(`/api/persons/${id}`),
        onSuccess: () => qc.invalidateQueries({ queryKey: ['persons'] }),
    })

    return { persons, addPerson, updatePerson, deletePerson }
}
