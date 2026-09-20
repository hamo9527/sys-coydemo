import type { Id } from '@/types/api'

export type ReceivableView = {
  id: Id
  billNo: string
  customerId: Id
  sourceOrderId: Id | null
  amount: number
  receivedAmount: number
  dueDate: string | null
  status: string
}

export type CreateReceivableRequest = {
  billNo: string
  customerId: number | string
  sourceOrderId?: number | string
  amount: number
  dueDate?: string
}

export type ReceivePaymentRequest = {
  amount: number
}
