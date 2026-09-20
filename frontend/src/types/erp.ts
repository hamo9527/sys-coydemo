import type { Id } from '@/types/api'

export type ProductView = {
  id: Id
  sku: string
  name: string
  unit: string | null
  purchasePrice: number
  salePrice: number
  status: number
}

export type CreateProductRequest = {
  sku: string
  name: string
  category?: string
  unit?: string
  spec?: string
  purchasePrice: number
  salePrice: number
}

export type StockView = {
  id: Id
  warehouseId: Id
  productId: Id
  quantity: number
  lockedQty: number
}

export type PurchaseItemView = {
  id: Id | null
  productId: Id
  qty: number
  price: number
  amount: number
}

export type PurchaseView = {
  id: Id
  orderNo: string
  supplierId: Id
  warehouseId: Id
  status: string
  totalAmount: number
  remark: string | null
  items: PurchaseItemView[]
}

export type CreatePurchaseItemRequest = {
  productId: number | string
  qty: number
  price: number
}

export type CreatePurchaseRequest = {
  orderNo: string
  supplierId: number | string
  warehouseId: number | string
  remark?: string
  items: CreatePurchaseItemRequest[]
}

export type OutboundItemView = {
  id: Id | null
  lineNo: number
  position: string | null
  productId: Id | null
  sku: string
  productName: string | null
  brand: string | null
  spec: string | null
  grade: string | null
  batchNo: string | null
  setsQty: number
  boxes: number
  pieces: number
  qty: number
  unit: string | null
  stdPrice: number
  discount: number
  price: number
  amount: number
  warehouseId: Id | null
  warehouseCode: string | null
  warehouseName: string | null
  binName: string | null
  feeType: string | null
  occupy: boolean
  countPerf: boolean
}

export type OutboundView = {
  id: Id
  billNo: string
  customNo: string | null
  billDate: string
  outboundType: string
  orderNo: string | null
  orderCustomNo: string | null
  orderDate: string | null
  orderType: string | null
  customerCode: string
  customerName: string
  contact: string | null
  mobile: string | null
  community: string | null
  address: string
  channel: string | null
  customerLevel: string | null
  salesDept: string
  salesperson: string | null
  guide: string | null
  designer: string | null
  designMethod: string | null
  shipFrom: string | null
  deliveryMethod: string | null
  packageProduct: string | null
  handler: string | null
  innerRemark: string | null
  outerRemark: string | null
  status: string
  totalAmount: number
  totalQty: number
  totalBoxes: number
  totalSqm: number
  totalWeight: number
  items: OutboundItemView[]
}

export type CreateOutboundItemRequest = {
  position?: string
  productId?: number | string
  sku: string
  productName?: string
  brand?: string
  spec?: string
  grade?: string
  batchNo?: string
  setsQty?: number
  boxes?: number
  pieces?: number
  qty: number
  unit?: string
  stdPrice?: number
  discount?: number
  price: number
  warehouseId?: number | string
  warehouseCode?: string
  warehouseName?: string
  binName?: string
  feeType?: string
  occupy?: boolean
  countPerf?: boolean
}

export type CreateOutboundRequest = {
  customNo?: string
  billDate: string
  outboundType: string
  orderNo?: string
  orderCustomNo?: string
  orderDate?: string
  orderType?: string
  customerCode: string
  customerName: string
  contact?: string
  mobile?: string
  community?: string
  address: string
  channel?: string
  customerLevel?: string
  salesDept: string
  salesperson?: string
  guide?: string
  designer?: string
  designMethod?: string
  shipFrom?: string
  deliveryMethod?: string
  packageProduct?: string
  handler?: string
  innerRemark?: string
  outerRemark?: string
  totalSqm?: number
  totalWeight?: number
  items: CreateOutboundItemRequest[]
}
