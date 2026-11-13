<template>
  <div class="page-card">
    <div class="page-header">
      <h2>采购订单</h2>
      <el-button type="primary" @click="router.push('/erp/purchase/create')">新建采购单</el-button>
    </div>

    <el-table :data="rows" stripe v-loading="loading" empty-text="暂无采购单">
      <el-table-column prop="id" label="ID" width="160" />
      <el-table-column prop="orderNo" label="单号" min-width="140" />
      <el-table-column prop="supplierId" label="供应商 ID" width="120" />
      <el-table-column prop="warehouseId" label="仓库 ID" width="120" />
      <el-table-column prop="totalAmount" label="金额" width="120" />
      <el-table-column prop="status" label="状态" width="100" />
      <el-table-column label="操作" width="120" fixed="right">
        <template #default="{ row }">
          <el-button
            link
            type="primary"
            :disabled="row.status !== 'draft'"
            @click="handleInbound(row.id)"
          >
            入库
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pager">
      <el-pagination
        v-model:current-page="page"
        v-model:page-size="size"
        layout="total, prev, pager, next"
        :total="total"
        @current-change="loadList"
        @size-change="handleSizeChange"
      />
    </div>

    <el-dialog v-model="inboundVisible" title="采购入库 - 确认库存维度" width="min(1100px, 94vw)" :close-on-click-modal="false">
      <el-alert
        type="info"
        :closable="false"
        title="请按实际到货情况填写色号、批次、等级和货位。色号支持自由输入，也可选择该商品历史使用过的色号。"
        class="mb"
      />
      <el-table :data="inboundLines" border size="small">
        <el-table-column prop="productId" label="商品 ID" width="130" />
        <el-table-column prop="qty" label="入库数量" width="110" />
        <el-table-column v-if="hasColor" label="色号" min-width="170">
          <template #default="{ row }">
            <el-autocomplete
              v-if="row.colorManagementMode === 'DYNAMIC'"
              v-model="row.colorCode"
              clearable
              size="small"
              placeholder="自由输入或选历史色号"
              :fetch-suggestions="(queryString, callback) => fetchColorSuggestions(queryString, callback, row)"
            />
          </template>
        </el-table-column>
        <el-table-column v-if="hasBatch" label="批次" min-width="130">
          <template #default="{ row }">
            <el-input v-if="row.batchManagementEnabled" v-model="row.batchNo" size="small" clearable />
          </template>
        </el-table-column>
        <el-table-column v-if="hasGrade" label="等级" min-width="120">
          <template #default="{ row }">
            <el-input v-if="row.gradeManagementEnabled" v-model="row.grade" size="small" clearable />
          </template>
        </el-table-column>
        <el-table-column label="货位" min-width="130">
          <template #default="{ row }">
            <el-input v-model="row.binName" size="small" clearable />
          </template>
        </el-table-column>
      </el-table>
      <template #footer>
        <el-button @click="inboundVisible = false">取消</el-button>
        <el-button type="primary" :loading="inboundSaving" @click="confirmInbound">确认入库</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  getProductColorMode,
  getPurchase,
  inboundPurchase,
  listColorCodeSuggestions,
  listPurchases,
} from '@/api/erp'
import type { ProductColorMode, PurchaseView } from '@/types/erp'

type InboundLine = {
  itemId: string | number
  productId: string | number
  qty: number
  colorCode: string
  batchNo: string
  grade: string
  binName: string
  colorManagementMode: ProductColorMode['colorManagementMode']
  batchManagementEnabled: boolean
  gradeManagementEnabled: boolean
}

const router = useRouter()
const loading = ref(false)
const inboundSaving = ref(false)
const inboundVisible = ref(false)
const activePurchaseId = ref<string | number>('')
const inboundLines = ref<InboundLine[]>([])
const page = ref(1)
const size = ref(10)
const total = ref(0)
const rows = ref<PurchaseView[]>([])

const hasColor = computed(() => inboundLines.value.some((item) => item.colorManagementMode === 'DYNAMIC'))
const hasBatch = computed(() => inboundLines.value.some((item) => item.batchManagementEnabled))
const hasGrade = computed(() => inboundLines.value.some((item) => item.gradeManagementEnabled))

async function loadList() {
  loading.value = true
  try {
    const result = await listPurchases({ page: page.value, size: size.value })
    rows.value = result.records
    total.value = result.total
  } finally {
    loading.value = false
  }
}

function handleSizeChange() {
  page.value = 1
  void loadList()
}

async function handleInbound(id: string | number) {
  const purchase = await getPurchase(id)
  const lines = await Promise.all(purchase.items.map(async (item) => {
    const config = await getProductColorMode(item.productId)
    return {
      itemId: item.id ?? '',
      productId: item.productId,
      qty: item.qty,
      colorCode: item.colorCode || '',
      batchNo: item.batchNo || '',
      grade: item.grade || '',
      binName: item.binName || '',
      colorManagementMode: config.colorManagementMode,
      batchManagementEnabled: config.batchManagementEnabled,
      gradeManagementEnabled: config.gradeManagementEnabled,
    }
  }))
  activePurchaseId.value = id
  inboundLines.value = lines
  inboundVisible.value = true
}

async function fetchColorSuggestions(
  queryString: string,
  callback: (suggestions: Array<{ value: string }>) => void,
  row: InboundLine,
) {
  try {
    const suggestions = await listColorCodeSuggestions(row.productId, queryString)
    callback(suggestions.map((value) => ({ value })))
  } catch {
    callback([])
  }
}

async function confirmInbound() {
  inboundSaving.value = true
  try {
    await inboundPurchase(activePurchaseId.value, {
      items: inboundLines.value.map((item) => ({
        itemId: item.itemId,
        colorCode: item.colorCode.trim() || undefined,
        batchNo: item.batchManagementEnabled ? item.batchNo.trim() || undefined : undefined,
        grade: item.gradeManagementEnabled ? item.grade.trim() || undefined : undefined,
        binName: item.binName.trim() || undefined,
      })),
    })
    ElMessage.success('入库成功')
    inboundVisible.value = false
    await loadList()
  } finally {
    inboundSaving.value = false
  }
}

onMounted(() => {
  void loadList()
})
</script>

<style scoped>
.mb {
  margin-bottom: 12px;
}

.pager {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}
</style>
