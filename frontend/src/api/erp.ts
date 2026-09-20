import http from '@/utils/http'
import type { PageResult } from '@/types/api'
import type {
  CreateOutboundRequest,
  CreateProductRequest,
  CreatePurchaseRequest,
  OutboundView,
  ProductView,
  PurchaseView,
  StockView,
} from '@/types/erp'

export function listProducts(params: { keyword?: string; page?: number; size?: number }) {
  return http.get<PageResult<ProductView>>('/erp/products', { params })
}

export function getProduct(id: string | number) {
  return http.get<ProductView>(`/erp/products/${id}`)
}

export function createProduct(data: CreateProductRequest) {
  return http.post<ProductView>('/erp/products', data)
}

export function listStocks(params: {
  warehouseId?: string | number
  productId?: string | number
  page?: number
  size?: number
}) {
  return http.get<PageResult<StockView>>('/erp/stocks', { params })
}

export function listPurchases(params: { page?: number; size?: number }) {
  return http.get<PageResult<PurchaseView>>('/erp/purchases', { params })
}

export function getPurchase(id: string | number) {
  return http.get<PurchaseView>(`/erp/purchases/${id}`)
}

export function createPurchase(data: CreatePurchaseRequest) {
  return http.post<PurchaseView>('/erp/purchases', data)
}

export function inboundPurchase(id: string | number) {
  return http.post<PurchaseView>(`/erp/purchases/${id}/inbounds`)
}

export function listOutbounds(params: { page?: number; size?: number }) {
  return http.get<PageResult<OutboundView>>('/erp/outbounds', { params })
}

export function getOutbound(id: string | number) {
  return http.get<OutboundView>(`/erp/outbounds/${id}`)
}

export function createOutbound(data: CreateOutboundRequest) {
  return http.post<OutboundView>('/erp/outbounds', data)
}

export function confirmOutbound(id: string | number) {
  return http.post<OutboundView>(`/erp/outbounds/${id}/confirmations`)
}
