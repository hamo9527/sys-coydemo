<template>
  <div class="page-card">
    <div class="page-header">
      <h2>盘点单</h2>
      <el-button type="primary" @click="createVisible = true">新建盘点单</el-button>
    </div>

    <el-table :data="rows" stripe v-loading="loading" empty-text="暂无盘点单">
      <el-table-column prop="billNo" label="系统单号" min-width="170" />
      <el-table-column prop="billDate" label="开单日期" width="130" />
      <el-table-column label="仓库" min-width="150">
        <template #default="{ row }">{{ warehouseName(row.warehouseId) }}</template>
      </el-table-column>
      <el-table-column label="盘点商品行数" width="120">
        <template #default="{ row }">{{ row.items.length }}</template>
      </el-table-column>
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 'confirmed' ? 'success' : 'warning'">
            {{ row.status === 'confirmed' ? '已确认' : '草稿' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="100" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="openDetail(row.id)">查看/编辑</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pager">
      <el-pagination v-model:current-page="page" v-model:page-size="size"
        layout="total, prev, pager, next" :total="total"
        @current-change="loadList" @size-change="handleSizeChange" />
    </div>

    <el-dialog v-model="createVisible" title="新建盘点单" width="480px" :close-on-click-modal="false">
      <el-form label-width="90px">
        <el-form-item label="开单日期" required>
          <el-date-picker v-model="createForm.billDate" type="date" value-format="YYYY-MM-DD"
            :disabled-date="disableFutureDate" />
        </el-form-item>
        <el-form-item label="盘点仓库" required>
          <el-select v-model="createForm.warehouseId" placeholder="请选择仓库" class="full">
            <el-option v-for="warehouse in warehouses" :key="warehouse.id"
              :label="`${warehouse.code} - ${warehouse.name}`" :value="warehouse.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注"><el-input v-model="createForm.remark" type="textarea" :rows="2" /></el-form-item>
      </el-form>
      <el-alert type="info" :closable="false" title="先保存空白盘点单，再逐行选择商品并录入实盘数量和成本单价。" />
      <template #footer>
        <el-button @click="createVisible = false">取消</el-button>
        <el-button type="primary" :loading="creating" @click="handleCreate">保存并进入盘点单</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="detailVisible"
      :title="activeCount ? `盘点单 ${activeCount.billNo}` : '盘点单明细'"
      width="min(1500px, 98vw)" :close-on-click-modal="false">
      <template v-if="activeCount">
        <div class="summary">
          <span>开单日期：{{ activeCount.billDate }}</span>
          <span>仓库：{{ warehouseName(activeCount.warehouseId) }}</span>
          <span>状态：{{ activeCount.status === 'confirmed' ? '已确认' : '草稿' }}</span>
          <span>明细：{{ activeCount.items.length }} 行</span>
        </div>
        <el-alert v-if="activeCount.status === 'draft'" type="info" :closable="false"
          title="账面数量由商品维度与开单日期的库存流水自动匹配。保存明细不会调整库存，确认盘点才正式过账。" class="mb" />
        <el-table :data="activeCount.items" border size="small" max-height="520">
          <el-table-column label="商品编码/名称" min-width="240">
            <template #default="{ row }">
              <el-select v-if="activeCount.status === 'draft'" v-model="row.productId"
                filterable remote clearable reserve-keyword placeholder="搜索商品编码或名称"
                :remote-method="loadProducts" :loading="productsLoading" class="full">
                <el-option v-for="product in productOptions" :key="product.id" :value="product.id"
                  :label="`${product.sku} - ${product.name}`" />
              </el-select>
              <span v-else>{{ productLabel(row.productId) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="色号" min-width="120">
            <template #default="{ row }">
              <el-input v-if="activeCount.status === 'draft'" v-model="row.colorCode" />
              <span v-else>{{ row.colorCode || '-' }}</span>
            </template>
          </el-table-column>
          <el-table-column label="批次" min-width="110">
            <template #default="{ row }">
              <el-input v-if="activeCount.status === 'draft'" v-model="row.batchNo" />
              <span v-else>{{ row.batchNo || '-' }}</span>
            </template>
          </el-table-column>
          <el-table-column label="等级" min-width="100">
            <template #default="{ row }">
              <el-input v-if="activeCount.status === 'draft'" v-model="row.grade" />
              <span v-else>{{ row.grade || '-' }}</span>
            </template>
          </el-table-column>
          <el-table-column label="货位" min-width="100">
            <template #default="{ row }">
              <el-input v-if="activeCount.status === 'draft'" v-model="row.binName" />
              <span v-else>{{ row.binName || '-' }}</span>
            </template>
          </el-table-column>
          <el-table-column label="账面数量" width="120">
            <template #default="{ row }">{{ Number(row.bookQty || 0).toFixed(4) }}</template>
          </el-table-column>
          <el-table-column label="实盘数量" width="150">
            <template #default="{ row }">
              <el-input-number v-if="activeCount.status === 'draft'" v-model="row.actualQty"
                :min="0" :precision="4" :step="1" controls-position="right" size="small" />
              <span v-else>{{ row.actualQty }}</span>
            </template>
          </el-table-column>
          <el-table-column label="成本单价" width="140">
            <template #default="{ row }">
              <el-input-number v-if="activeCount.status === 'draft'" v-model="row.costPrice"
                :min="0" :precision="4" :step="0.1" controls-position="right" size="small" />
              <span v-else>{{ Number(row.costPrice || 0).toFixed(4) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="盈亏数量" width="120">
            <template #default="{ row }">
              <span :class="difference(row) > 0 ? 'gain' : difference(row) < 0 ? 'loss' : ''">
                {{ row.actualQty === null ? '待盘点' : difference(row).toFixed(4) }}
              </span>
            </template>
          </el-table-column>
          <el-table-column label="盈亏金额" width="130">
            <template #default="{ row }">
              {{ row.actualQty === null ? '-' : (difference(row) * Number(row.costPrice || 0)).toFixed(2) }}
            </template>
          </el-table-column>
          <el-table-column v-if="activeCount.status === 'draft'" label="操作" width="80" fixed="right">
            <template #default="{ $index }">
              <el-button link type="danger" @click="removeItem($index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div v-if="activeCount.status === 'draft'" class="toolbar">
          <el-button @click="addItem">新增明细行</el-button>
          <span class="hint">同一商品、色号、批次、等级、货位不可重复</span>
        </div>
        <el-empty v-if="activeCount.items.length === 0" description="当前为空白盘点单，请新增明细或后续导入账面库存" />
      </template>
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
        <template v-if="activeCount?.status === 'draft'">
          <el-button :loading="saving" @click="saveItems">保存明细</el-button>
          <el-button type="primary" :loading="confirming"
            :disabled="activeCount.items.length === 0" @click="handleConfirm">确认盘点并调整库存</el-button>
        </template>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  confirmStockCount, createStockCount, getStockCount, listProducts,
  listStockCounts, listWarehouses, updateStockCount,
} from '@/api/erp'
import type { ProductView, StockCountView, WarehouseView } from '@/types/erp'

const loading = ref(false)
const creating = ref(false)
const saving = ref(false)
const confirming = ref(false)
const productsLoading = ref(false)
const createVisible = ref(false)
const detailVisible = ref(false)
const rows = ref<StockCountView[]>([])
const warehouses = ref<WarehouseView[]>([])
const productOptions = ref<ProductView[]>([])
const activeCount = ref<StockCountView | null>(null)
const page = ref(1)
const size = ref(10)
const total = ref(0)

function today() {
  const date = new Date()
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
}

function disableFutureDate(date: Date) {
  const todayDate = new Date()
  todayDate.setHours(0, 0, 0, 0)
  return date.getTime() > todayDate.getTime()
}

const createForm = reactive({ warehouseId: '' as string | number, billDate: today(), remark: '' })

function warehouseName(id: string | number) {
  return warehouses.value.find((item) => String(item.id) === String(id))?.name || String(id)
}
function productLabel(id: string | number) {
  const product = productOptions.value.find((item) => String(item.id) === String(id))
  return product ? `${product.sku} - ${product.name}` : String(id)
}
function difference(row: StockCountView['items'][number]) {
  return (row.actualQty ?? row.bookQty) - row.bookQty
}
async function loadProducts(keyword = '') {
  productsLoading.value = true
  try {
    const result = await listProducts({ keyword, page: 1, size: 50 })
    productOptions.value = result.records
  } finally {
    productsLoading.value = false
  }
}
async function loadList() {
  loading.value = true
  try {
    const result = await listStockCounts({ page: page.value, size: size.value })
    rows.value = result.records
    total.value = result.total
  } finally {
    loading.value = false
  }
}
async function loadWarehouses() { warehouses.value = await listWarehouses({ status: 1 }) }
function handleSizeChange() { page.value = 1; void loadList() }

async function handleCreate() {
  if (!createForm.warehouseId) { ElMessage.warning('请选择盘点仓库'); return }
  creating.value = true
  try {
    const count = await createStockCount({
      warehouseId: createForm.warehouseId, billDate: createForm.billDate,
      remark: createForm.remark.trim() || undefined,
    })
    createVisible.value = false
    createForm.remark = ''
    await loadList()
    await openDetail(count.id)
    ElMessage.success('空白盘点单已保存')
  } finally { creating.value = false }
}

async function openDetail(id: string | number) {
  activeCount.value = await getStockCount(id)
  detailVisible.value = true
  if (activeCount.value.status === 'draft') await loadProducts()
}
function addItem() {
  if (!activeCount.value) return
  activeCount.value.items.push({
    id: 0 as StockCountView['items'][number]['id'],
    productId: '' as StockCountView['items'][number]['productId'],
    colorCode: '', batchNo: '', grade: '', binName: '',
    costPrice: 0, bookQty: 0, actualQty: null, differenceQty: null, differenceAmount: null,
  })
}
function removeItem(index: number) { activeCount.value?.items.splice(index, 1) }

async function saveItems() {
  if (!activeCount.value) return
  const items = activeCount.value.items
  if (items.some((item) => item.productId === '' || item.productId === null || item.actualQty === null || item.actualQty === undefined)) {
    ElMessage.warning('请为每一行选择商品并填写实盘数量')
    return
  }
  saving.value = true
  try {
    activeCount.value = await updateStockCount(activeCount.value.id, {
      items: items.map((item) => ({
        productId: item.productId, colorCode: item.colorCode, batchNo: item.batchNo,
        grade: item.grade, binName: item.binName, costPrice: Number(item.costPrice || 0),
        actualQty: item.actualQty,
      })),
    })
    await loadList()
    ElMessage.success('盘点明细已保存，库存尚未调整')
  } finally { saving.value = false }
}

async function handleConfirm() {
  if (!activeCount.value) return
  if (activeCount.value.items.length === 0) { ElMessage.warning('没有盘点明细，无法确认'); return }
  if (activeCount.value.items.some((item) => item.actualQty === null || item.actualQty === undefined)) {
    ElMessage.warning('请填写全部商品的实盘数量'); return
  }
  try {
    await ElMessageBox.confirm(
      '确认后系统将按实盘数量与账面数量的差额调整库存，并生成盘点盈亏流水。此操作不可撤销。',
      '确认盘点', { type: 'warning', confirmButtonText: '确认调整', cancelButtonText: '再检查一下' },
    )
  } catch { return }
  confirming.value = true
  try {
    await saveItems()
    activeCount.value = await confirmStockCount(activeCount.value!.id)
    await loadList()
    ElMessage.success('盘点确认完成，库存已调整')
  } finally { confirming.value = false }
}

onMounted(async () => { await Promise.all([loadWarehouses(), loadList(), loadProducts()]) })
</script>

<style scoped>
.pager { display: flex; justify-content: flex-end; margin-top: 16px; }
.full { width: 100%; }
.summary { display: flex; flex-wrap: wrap; gap: 24px; margin-bottom: 12px; color: var(--el-text-color-regular); }
.mb { margin-bottom: 12px; }
.toolbar { display: flex; align-items: center; gap: 12px; margin-top: 12px; }
.hint { color: var(--el-text-color-secondary); font-size: 12px; }
.gain { color: var(--el-color-success); }
.loss { color: var(--el-color-danger); }
</style>
