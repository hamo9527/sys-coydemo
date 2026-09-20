import http from '@/utils/http'
import type {
  CreateReceivableRequest,
  ReceivableView,
  ReceivePaymentRequest,
} from '@/types/finance'

export function getReceivable(id: string | number) {
  return http.get<ReceivableView>(`/finance/receivables/${id}`)
}

export function createReceivable(data: CreateReceivableRequest) {
  return http.post<ReceivableView>('/finance/receivables', data)
}

export function receivePayment(id: string | number, data: ReceivePaymentRequest) {
  return http.post<ReceivableView>(`/finance/receivables/${id}/payments`, data)
}
