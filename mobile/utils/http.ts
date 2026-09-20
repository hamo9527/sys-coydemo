export function createRequest() {
  const baseURL = '/api'

  function request<T>(options: UniApp.RequestOptions): Promise<T> {
    return new Promise((resolve, reject) => {
      uni.request({
        ...options,
        url: `${baseURL}${options.url}`,
        success: (res) => {
          const payload = res.data as { result?: string; message?: string; data?: T }
          if (payload?.result === 'SUCCESS') {
            resolve(payload.data as T)
            return
          }
          reject(new Error(payload?.message || '请求失败'))
        },
        fail: (err) => reject(err),
      })
    })
  }

  return {
    get: <T>(url: string) => request<T>({ url, method: 'GET' }),
    post: <T>(url: string, data?: unknown) => request<T>({ url, method: 'POST', data }),
  }
}

export const http = createRequest()
