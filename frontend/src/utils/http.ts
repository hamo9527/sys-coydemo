import axios, { type AxiosError, type AxiosRequestConfig } from 'axios'
import { ElMessage } from 'element-plus'
import type { ApiResponse } from '@/types/api'
import { useUserStore } from '@/stores/user'

const instance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  timeout: 30000,
})

instance.interceptors.request.use((config) => {
  const userStore = useUserStore()
  if (userStore.token) {
    config.headers.Authorization = `Bearer ${userStore.token}`
  }
  return config
})

instance.interceptors.response.use(
  (response) => {
    const payload = response.data as ApiResponse<unknown>
    if (payload && typeof payload === 'object' && 'result' in payload) {
      if (payload.result !== 'SUCCESS') {
        ElMessage.error(payload.message || '请求失败')
        return Promise.reject(new Error(payload.message || '请求失败'))
      }
      return payload.data
    }
    return response.data
  },
  (error: AxiosError<ApiResponse<unknown>>) => {
    if (error.response?.status === 401) {
      const userStore = useUserStore()
      userStore.logout()
      if (!window.location.pathname.startsWith('/login')) {
        const redirect = encodeURIComponent(window.location.pathname + window.location.search)
        window.location.assign(`/login?redirect=${redirect}`)
      }
    }
    const message = error.response?.data?.message || error.message || '网络异常'
    ElMessage.error(message)
    return Promise.reject(error)
  },
)

function get<T>(url: string, config?: AxiosRequestConfig) {
  return instance.get(url, config) as Promise<T>
}

function post<T>(url: string, data?: unknown, config?: AxiosRequestConfig) {
  return instance.post(url, data, config) as Promise<T>
}

function put<T>(url: string, data?: unknown, config?: AxiosRequestConfig) {
  return instance.put(url, data, config) as Promise<T>
}

function del<T>(url: string, config?: AxiosRequestConfig) {
  return instance.delete(url, config) as Promise<T>
}

const http = { get, post, put, delete: del }

export default http
