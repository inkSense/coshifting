import { useEffect, useState } from 'react'
import { useAuth } from '../auth/AuthContext'
import {
  Container,
  Paper,
  Typography,
  TableContainer,
  Table,
  TableHead,
  TableRow,
  TableCell,
  TableBody,
  CircularProgress,
} from '@mui/material'

interface PersonDto {
  id: number
  nickname: string
  role: string
}

export default function AdminPage() {
  const { header } = useAuth()
  const [persons, setPersons] = useState<PersonDto[]>([])
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    setLoading(true)
    fetch('/api/persons', {
      headers: header ? { Authorization: header } : {}
    })
      .then(res => {
        if (!res.ok) throw new Error('Network error')
        return res.json()
      })
      .then((data: PersonDto[]) => setPersons(data))
      .catch(err => console.error('Failed to load persons', err))
      .finally(() => setLoading(false))
  }, [header])

  return (
    <Container sx={{ py: 3 }}>
      <Typography variant="h5" gutterBottom>
        Personenverwaltung
      </Typography>

      {loading ? (
        <CircularProgress />
      ) : (
        <TableContainer component={Paper} sx={{ maxHeight: 500 }}>
          <Table stickyHeader size="small" aria-label="persons table">
            <TableHead>
              <TableRow>
                <TableCell>ID</TableCell>
                <TableCell>Nickname</TableCell>
                <TableCell>Rolle</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {persons.map(p => (
                <TableRow key={p.id} hover>
                  <TableCell>{p.id}</TableCell>
                  <TableCell>{p.nickname}</TableCell>
                  <TableCell>{p.role}</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      )}
    </Container>
  )
}