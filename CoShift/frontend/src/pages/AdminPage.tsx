import { useState } from 'react'
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
    IconButton,
    Tooltip,
} from '@mui/material'
import EditIcon from '@mui/icons-material/Edit'
import DeleteIcon from '@mui/icons-material/Delete'
import AddIcon from '@mui/icons-material/Add'
import { IconButton as MuiIconButton } from '@mui/material'

import AddPersonDialog from '../features/admin/components/AddPersonDialog'
import EditPersonDialog from '../features/admin/components/EditPersonDialog'
import { usePersons } from '../features/admin/hooks/usePersons'
import type { PersonDto } from '../types/person'

export default function AdminPage() {
    const {
        persons,          // useQuery-Result
        addPerson,        // useMutation-Resulten
        updatePerson,
        deletePerson,
    } = usePersons()

    const [addOpen,  setAddOpen]  = useState(false)
    const [edit,     setEdit]     = useState<PersonDto | null>(null)

    const isLoading = persons.isLoading
    const rows      = persons.data ?? []

    return (
        <Container sx={{ py: 3 }}>
            <Typography variant="h5" gutterBottom>Personenverwaltung</Typography>

            {/* Add Person Button */}
            <Tooltip title="Neue Person hinzufügen">
                <IconButton color="primary" onClick={() => setAddOpen(true)}>
                    <AddIcon />
                </IconButton>
            </Tooltip>

            {/* Tabelle */}
            {isLoading ? (
                <CircularProgress />
            ) : (
                <TableContainer component={Paper} sx={{ maxHeight: 500 }}>
                    <Table stickyHeader size="small" aria-label="persons table">
                        <TableHead>
                            <TableRow>
                                <TableCell>ID</TableCell>
                                <TableCell>Nickname</TableCell>
                                <TableCell>Rolle</TableCell>
                                <TableCell /> {/* Aktionen */}
                            </TableRow>
                        </TableHead>

                        <TableBody>
                            {rows.map(p => (
                                <TableRow key={p.id} hover>
                                    <TableCell>{p.id}</TableCell>
                                    <TableCell>{p.nickname}</TableCell>
                                    <TableCell>{p.role}</TableCell>

                                    <TableCell padding="checkbox">
                                        <IconButton size="small" onClick={() => setEdit(p)}>
                                            <EditIcon fontSize="small" />
                                        </IconButton>

                                        <MuiIconButton
                                            size="small"
                                            color="error"
                                            onClick={() => {
                                                if (confirm(`Person ${p.nickname} wirklich löschen?`)) {
                                                    deletePerson.mutate(p.id)
                                                }
                                            }}
                                        >
                                            <DeleteIcon fontSize="small" />
                                        </MuiIconButton>
                                    </TableCell>
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>
            )}

            {/* Dialoge */}
            <AddPersonDialog
                open={addOpen}
                onClose={() => setAddOpen(false)}
                onCreated={dto => addPerson.mutate(dto)}
            />

            <EditPersonDialog
                person={edit}
                onClose={() => setEdit(null)}
                onUpdated={dto => updatePerson.mutate(dto)}
            />
        </Container>
    )
}
