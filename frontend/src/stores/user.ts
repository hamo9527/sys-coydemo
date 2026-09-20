import { computed, ref } from 'vue'
import { defineStore } from 'pinia'
import { login as loginApi } from '@/api/system'
import type { UserView } from '@/types/system'

const TOKEN_KEY = 'cailu_token'
const USER_KEY = 'cailu_user'

function isUserView(value: unknown): value is UserView {
  if (!value || typeof value !== 'object') {
    return false
  }
  const record = value as Record<string, unknown>
  const idOk = typeof record.id === 'string' || typeof record.id === 'number'
  return idOk && typeof record.username === 'string'
}

function normalizeUser(user: UserView): UserView {
  return {
    ...user,
    id: String(user.id),
    orgId: user.orgId == null ? null : String(user.orgId),
  }
}

function readStoredUser(): UserView | null {
  const raw = localStorage.getItem(USER_KEY)
  if (!raw) {
    return null
  }
  try {
    const parsed: unknown = JSON.parse(raw)
    return isUserView(parsed) ? normalizeUser(parsed) : null
  } catch {
    return null
  }
}

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem(TOKEN_KEY) || '')
  const profile = ref<UserView | null>(readStoredUser())

  const isLoggedIn = computed(() => Boolean(token.value))
  const displayName = computed(
    () => profile.value?.realName || profile.value?.username || '访客',
  )

  function setSession(nextToken: string, user: UserView) {
    const normalized = normalizeUser(user)
    token.value = nextToken
    profile.value = normalized
    localStorage.setItem(TOKEN_KEY, nextToken)
    localStorage.setItem(USER_KEY, JSON.stringify(normalized))
  }

  function logout() {
    token.value = ''
    profile.value = null
    localStorage.removeItem(TOKEN_KEY)
    localStorage.removeItem(USER_KEY)
  }

  async function login(username: string, password: string) {
    const result = await loginApi({ username, password })
    setSession(result.token, result.user)
    return result.user
  }

  return {
    token,
    profile,
    isLoggedIn,
    displayName,
    setSession,
    logout,
    login,
  }
})
