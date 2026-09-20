import type { Id } from '@/types/api'

export type ApprovalView = {
  id: Id
  title: string
  bizType: string | null
  bizId: Id | null
  applicantId: Id
  status: string
  remark: string | null
}

export type CreateApprovalRequest = {
  title: string
  bizType?: string
  bizId?: number | string
  applicantId: number | string
  remark?: string
}
