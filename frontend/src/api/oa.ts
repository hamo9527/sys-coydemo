import http from '@/utils/http'
import type { ApprovalView, CreateApprovalRequest } from '@/types/oa'

export function getApproval(id: string | number) {
  return http.get<ApprovalView>(`/oa/approvals/${id}`)
}

export function createApproval(data: CreateApprovalRequest) {
  return http.post<ApprovalView>('/oa/approvals', data)
}

export function submitApproval(id: string | number) {
  return http.post<ApprovalView>(`/oa/approvals/${id}/submissions`)
}
