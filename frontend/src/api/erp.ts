import http from '@/utils/http'
import type { PageResult } from '@/types/api'
import type {
  CreateOutboundRequest,
  CreateProductRequest,
  CreatePurchaseRequest,
  OutboundView,
  ProductView,
  ProductImportRow,
  ProductImportResult,
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

export function updateProduct(id: string | number, data: CreateProductRequest) {
  return http.put<ProductView>(`/erp/products/${id}`, data)
}

export function deleteProduct(id: string | number) {
  return http.delete<void>(`/erp/products/${id}`)
}

export function importProducts(rows: ProductImportRow[]) {
  return http.post<ProductImportResult>('/erp/products/imports', rows)
}

export function downloadProductExcelTemplate() {
  return http.get<Blob>('/erp/products/imports/excel/template', { responseType: 'blob' })
}

export function previewProductsExcel(file: File) {
  const data = new FormData()
  data.append('file', file)
  return http.post<ProductImportRow[]>('/erp/products/imports/excel/preview', data)
}

export function listColorCodeSuggestions(productId: string | number, keyword = '') {
  return http.get<string[]>('/erp/stocks/color-codes/suggestions', { params: { productId, keyword } })
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

import type { WarehouseView, WarehouseRequest, ColorView, ColorRequest, ProductColorMode } from '@/types/erp'
export function listWarehouses(params: { keyword?: string; status?: number }) { return http.get<WarehouseView[]>('/erp/warehouses', { params }) }
export function createWarehouse(data: WarehouseRequest) { return http.post<WarehouseView>('/erp/warehouses', data) }
export function updateWarehouse(id: string | number, data: WarehouseRequest) { return http.put<WarehouseView>(`/erp/warehouses/${id}`, data) }
export function deleteWarehouse(id: string | number) { return http.delete<void>(`/erp/warehouses/${id}`) }
export function listColors(keyword?: string) { return http.get<ColorView[]>('/erp/colors', { params: { keyword } }) }
export function createColor(data: ColorRequest) { return http.post<ColorView>('/erp/colors', data) }
export function updateProductColorMode(id: string | number, data: Omit<ProductColorMode, 'productId'>) { return http.put<ProductColorMode>(`/erp/products/${id}/color-management`, data) }
export function getProductColorMode(id: string | number) { return http.get<ProductColorMode>(`/erp/products/${id}/color-management`) }
export function listProductColors(id: string | number) { return http.get<ColorView[]>(`/erp/products/${id}/colors`) }
export function bindProductColor(id: string | number, colorId: string | number) { return http.post<void>(`/erp/products/${id}/colors/${colorId}`) }
export function unbindProductColor(id: string | number, colorId: string | number) { return http.delete<void>(`/erp/products/${id}/colors/${colorId}`) }

export function updateColor(id: string | number, data: ColorRequest) { return http.put<ColorView>(`/erp/colors/${id}`, data) }
export function deleteColor(id: string | number) { return http.delete<void>(`/erp/colors/${id}`) }
