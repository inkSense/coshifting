
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import type { PersonDto } from '../../../types/person'
import { useApiClient } from '../../../api/client'

export function usePersons() {
    const { get, post, put, del } = useApiClient()
    const qc = useQueryClient()

    const persons = useQuery({
        queryKey: ['persons'],
        queryFn: async (): Promise<PersonDto[]> => get<PersonDto[]>('/api/persons'),
    })

    const addPerson = useMutation({
        mutationFn: async (body: Omit<PersonDto, 'id'>) => post<PersonDto>('/api/persons', body),
        onSuccess: () => qc.invalidateQueries({ queryKey: ['persons'] }),
    })

    const updatePerson = useMutation({
        mutationFn: async (p: PersonDto) => put<PersonDto>(`/api/persons/${p.id}`, p),
        onSuccess: () => qc.invalidateQueries({ queryKey: ['persons'] }),
    })

    const deletePerson = useMutation({
        mutationFn: async (id: number) => {
            await del(`/api/persons/${id}`)
            return id
        },
        onSuccess: () => qc.invalidateQueries({ queryKey: ['persons'] }),
    })

    return { persons, addPerson, updatePerson, deletePerson }
}
