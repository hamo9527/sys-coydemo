/** 与后端 ApiResponse / PageResult 对齐 */
export type ApiResult = 'SUCCESS' | 'ERROR'

export type ApiResponse<T> = {
  result: ApiResult
  message: string
  data: T
}

export type PageResult<T> = {
  records: T[]
  total: number
  page: number
  size: number
}

export type PageQuery = {
  page?: number
  size?: number
  keyword?: string
}

/** 雪花 ID：后端 Long 以字符串下发，避免 JS 精度丢失 */
export type Id = string
