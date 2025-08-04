import { Box, type BoxProps } from '@mui/material'

interface ShiftBlockProps extends BoxProps {
  filled: boolean
  text: string
}

export default function ShiftBlock({ filled, text, sx, ...rest }: ShiftBlockProps) {
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
        ...(sx ?? {}),
      }}
    >
      {text}
    </Box>
  )
}
