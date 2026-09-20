import http from '@/utils/http'
import type { CreateUserRequest, LoginRequest, LoginView, UserView } from '@/types/system'

export function login(data: LoginRequest) {
  return http.post<LoginView>('/system/auth/login', data)
}

export function getUser(id: string | number) {
  return http.get<UserView>(`/system/users/${id}`)
}

export function createUser(data: CreateUserRequest) {
  return http.post<UserView>('/system/users', data)
}
