import { Box, type BoxProps } from '@mui/material'
import { type ReactNode } from 'react'

interface ShiftBlockProps extends BoxProps {
  filled: boolean
  text: string
  children?: ReactNode
}

export default function ShiftBlock({ filled, text, children, sx, ...rest }: ShiftBlockProps) {
  return (
    <Box
      {...rest}
      sx={{
        bgcolor: filled ? 'success.main' : 'grey.600',
        color: filled ? 'common.white' : 'text.primary',
        fontWeight: filled ? 600 : 'normal',
        borderRadius: 1,
        m: 0.5,
        p: 0.5,
        fontSize: '0.85rem',
        display: 'flex',
        flexDirection: 'column',
        ...(sx ?? {}),
      }}
    >
        <Box sx={{ flexGrow: 1 }}>{text}</Box>
        {children}
    </Box>
  )
}
