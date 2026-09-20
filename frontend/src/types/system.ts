import type { Id } from '@/types/api'

export type UserView = {
  id: Id
  username: string
  realName: string | null
  mobile: string | null
  orgId: Id | null
  status: number
}

export type CreateUserRequest = {
  username: string
  password: string
  realName?: string
  mobile?: string
  orgId?: number | string
}

export type LoginRequest = {
  username: string
  password: string
}

export type LoginView = {
  token: string
  user: UserView
}
