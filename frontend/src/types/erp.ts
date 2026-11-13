import type { Id } from '@/types/api'

export type ProductView = {
  id: Id
  sku: string
  mnemonicCode: string | null
  name: string
  brandName: string
  category: string | null
  spec: string | null
  unit: string
  auxiliaryUnit: string | null
  auxiliarySpec: string | null
  packagingUnit: string | null
  packagingSpec: string | null
  unitWeight: number | null
  minimumOrderQty: number | null
  minStock: number | null
  maxStock: number | null
  smallColorStock: boolean
  stockAgeThreshold: number | null
  obsoleteDays: number | null
  shelfLifeDays: number | null
  productAttribute: string
  virtualProduct: boolean
  installationRequired: boolean
  cloudStoreVisible: boolean
  productionProcess: string | null
  productionOrigin: string | null
  usageLocation: string | null
  vrUrl: string | null
  remark: string | null
  purchasePrice: number
  salePrice: number
  status: number
}

export type CreateProductRequest = {
  sku: string
  mnemonicCode?: string
  name: string
  brandName: string
  category?: string
  spec?: string
  unit: string
  auxiliaryUnit?: string
  auxiliarySpec?: string
  packagingUnit?: string
  packagingSpec?: string
  unitWeight?: number
  minimumOrderQty?: number
  minStock?: number
  maxStock?: number
  smallColorStock?: boolean
  stockAgeThreshold?: number
  obsoleteDays?: number
  shelfLifeDays?: number
  productAttribute: string
  virtualProduct?: boolean
  installationRequired?: boolean
  cloudStoreVisible?: boolean
  productionProcess?: string
  productionOrigin?: string
  usageLocation?: string
  vrUrl?: string
  remark?: string
  purchasePrice?: number
  salePrice?: number
}

export type ProductImportRow = {
  sku: string
  name: string
  brandName: string
  unit: string
  productAttribute: string
  mnemonicCode?: string
  category?: string
  spec?: string
  remark?: string
}

export type ProductImportResult = {
  successCount: number
  failureCount: number
  errors: string[]
}

export type StockView = {
  id: Id
  warehouseId: Id
  productId: Id
  colorCode: string
  batchNo: string
  grade: string
  binName: string
  quantity: number
  lockedQty: number
}

export type PurchaseItemView = {
  id: Id | null
  productId: Id
  colorCode: string
  batchNo: string
  grade: string
  binName: string
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

export type InboundPurchaseRequest = {
  items: Array<{ itemId: Id; colorCode?: string; batchNo?: string; grade?: string; binName?: string }>
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
  colorCode: string | null
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
  colorCode?: string
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

export type WarehouseView = { id: Id; code: string; name: string; address: string | null; status: number }
export type WarehouseRequest = { code: string; name: string; address?: string; status: number }
export type ColorView = { id: Id; code: string; name: string; shortName: string | null; brandName: string | null; seriesName: string | null; manufacturerColorCode: string | null; swatchImageUrl: string | null; status: number; remark: string | null }
export type ColorRequest = { code: string; name: string; shortName?: string; brandName?: string; seriesName?: string; manufacturerColorCode?: string; swatchImageUrl?: string; status?: number; remark?: string }
export type ProductColorMode = { productId: Id; colorManagementMode: 'NONE' | 'DYNAMIC'; batchManagementEnabled: boolean; gradeManagementEnabled: boolean }
