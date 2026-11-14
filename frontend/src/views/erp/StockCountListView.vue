<template>
  <div class="page-card">
    <div class="page-header">
      <h2>盘点单</h2>
      <el-button type="primary" @click="createVisible = true">新建盘点单</el-button>
    </div>

    <el-table :data="rows" stripe v-loading="loading" empty-text="暂无盘点单">
      <el-table-column prop="billNo" label="系统单号" min-width="170" />
      <el-table-column label="开单日期" width="130">
        <template #default="{ row }">{{ row.billDate }}</template>
      </el-table-column>
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
          <el-button link type="primary" @click="openDetail(row.id)">查看</el-button>
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

    <el-dialog v-model="createVisible" title="新建盘点单" width="480px" :close-on-click-modal="false">
      <el-form label-width="90px">
        <el-form-item label="开单日期" required>
          <el-date-picker v-model="createForm.billDate" type="date" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item label="盘点仓库" required>
          <el-select v-model="createForm.warehouseId" placeholder="请选择仓库" class="full">
            <el-option
              v-for="warehouse in warehouses"
              :key="warehouse.id"
              :label="`${warehouse.code} - ${warehouse.name}`"
              :value="warehouse.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="createForm.remark" type="textarea" :rows="2" />
        </el-form-item>
      </el-form>
      <el-alert
        type="info"
        :closable="false"
        title="系统将按开单日期，自动带出该仓库当日的账面库存明细。"
      />
      <template #footer>
        <el-button @click="createVisible = false">取消</el-button>
        <el-button type="primary" :loading="creating" @click="handleCreate">生成盘点明细</el-button>
      </template>
    </el-dialog>

    <el-dialog
      v-model="detailVisible"
      :title="activeCount ? `盘点单 ${activeCount.billNo}` : '盘点单明细'"
      width="min(1200px, 96vw)"
      :close-on-click-modal="false"
    >
      <template v-if="activeCount">
        <div class="summary">
          <span>开单日期：{{ activeCount.billDate }}</span>
          <span>仓库：{{ warehouseName(activeCount.warehouseId) }}</span>
          <span>状态：{{ activeCount.status === 'confirmed' ? '已确认' : '草稿' }}</span>
        </div>
        <el-alert
          type="info"
          :closable="false"
          title="账面数量取自开单日期当日及之前的库存流水；盘盈/盘亏 = 实盘数量 - 账面数量。"
          class="mb"
        />
        <el-table :data="activeCount.items" border size="small" max-height="480">
          <el-table-column prop="productId" label="商品 ID" width="130" />
          <el-table-column prop="colorCode" label="色号" min-width="110" />
          <el-table-column prop="batchNo" label="批次" min-width="100" />
          <el-table-column prop="grade" label="等级" width="90" />
          <el-table-column prop="binName" label="货位" width="100" />
          <el-table-column prop="bookQty" label="账面数量" width="120" />
          <el-table-column label="实盘数量" width="150">
            <template #default="{ row }">
              <el-input-number
                v-if="activeCount.status === 'draft'"
                v-model="row.actualQty"
                :min="0"
                :precision="4"
                :step="1"
                controls-position="right"
                size="small"
              />
              <span v-else>{{ row.actualQty }}</span>
            </template>
          </el-table-column>
          <el-table-column label="盘盈/盘亏" width="130">
            <template #default="{ row }">
              <span :class="difference(row) > 0 ? 'gain' : difference(row) < 0 ? 'loss' : ''">
                {{ row.actualQty === null ? '待盘点' : difference(row).toFixed(4) }}
              </span>
            </template>
          </el-table-column>
        </el-table>
        <el-empty v-if="activeCount.items.length === 0" description="该日期没有账面库存流水" />
      </template>
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
        <template v-if="activeCount?.status === 'draft'">
          <el-button :loading="saving" :disabled="activeCount.items.length === 0" @click="saveActuals">保存实盘数量</el-button>
          <el-button type="primary" :loading="confirming" :disabled="activeCount.items.length === 0" @click="handleConfirm">确认盘点并调整库存</el-button>
        </template>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  confirmStockCount,
  createStockCount,
  getStockCount,
  listStockCounts,
  listWarehouses,
  updateStockCount,
} from '@/api/erp'
import type { StockCountView, WarehouseView } from '@/types/erp'

const loading = ref(false)
const creating = ref(false)
const saving = ref(false)
const confirming = ref(false)
const createVisible = ref(false)
const detailVisible = ref(false)
const rows = ref<StockCountView[]>([])
const warehouses = ref<WarehouseView[]>([])
const activeCount = ref<StockCountView | null>(null)
const page = ref(1)
const size = ref(10)
const total = ref(0)

function today() {
  const date = new Date()
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
}

const createForm = reactive({
  warehouseId: '' as string | number,
  billDate: today(),
  remark: '',
})

function warehouseName(id: string | number) {
  return warehouses.value.find((item) => String(item.id) === String(id))?.name || String(id)
}

function difference(row: StockCountView['items'][number]) {
  return (row.actualQty ?? row.bookQty) - row.bookQty
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

async function loadWarehouses() {
  warehouses.value = await listWarehouses({ status: 1 })
}

function handleSizeChange() {
  page.value = 1
  void loadList()
}

async function handleCreate() {
  if (!createForm.warehouseId) {
    ElMessage.warning('请选择盘点仓库')
    return
  }
  creating.value = true
  try {
    const count = await createStockCount({
      warehouseId: createForm.warehouseId,
      billDate: createForm.billDate,
      remark: createForm.remark.trim() || undefined,
    })
    createVisible.value = false
    createForm.remark = ''
    await loadList()
    await openDetail(count.id)
    ElMessage.success(`已生成 ${count.items.length} 行盘点明细`)
  } finally {
    creating.value = false
  }
}

async function openDetail(id: string | number) {
  activeCount.value = await getStockCount(id)
  detailVisible.value = true
}

async function saveActuals() {
  if (!activeCount.value) return
  if (activeCount.value.items.some((item) => item.actualQty === null || item.actualQty === undefined)) {
    ElMessage.warning('请填写全部商品的实盘数量')
    return
  }
  saving.value = true
  try {
    activeCount.value = await updateStockCount(activeCount.value.id, {
      items: activeCount.value.items.map((item) => ({
        itemId: item.id,
        actualQty: item.actualQty as number,
      })),
    })
    await loadList()
    ElMessage.success('实盘数量已保存')
  } finally {
    saving.value = false
  }
}

async function handleConfirm() {
  if (!activeCount.value) return
  if (activeCount.value.items.length === 0) {
    ElMessage.warning('没有盘点明细，无法确认')
    return
  }
  if (activeCount.value.items.some((item) => item.actualQty === null || item.actualQty === undefined)) {
    ElMessage.warning('请填写全部商品的实盘数量')
    return
  }
  await ElMessageBox.confirm(
    '确认后系统将按实盘数量与账面数量的差额调整库存，并生成盘点盈亏流水。此操作不可撤销。',
    '确认盘点',
    { type: 'warning', confirmButtonText: '确认调整', cancelButtonText: '再检查一下' },
  )
  confirming.value = true
  try {
    await updateStockCount(activeCount.value.id, {
      items: activeCount.value.items.map((item) => ({
        itemId: item.id,
        actualQty: item.actualQty as number,
      })),
    })
    activeCount.value = await confirmStockCount(activeCount.value.id)
    await loadList()
    ElMessage.success('盘点确认完成，库存已调整')
  } finally {
    confirming.value = false
  }
}

onMounted(async () => {
  await Promise.all([loadWarehouses(), loadList()])
})
</script>

<style scoped>
.pager {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}

.full {
  width: 100%;
}

.summary {
  display: flex;
  flex-wrap: wrap;
  gap: 24px;
  margin-bottom: 12px;
  color: var(--el-text-color-regular);
}

.mb {
  margin-bottom: 12px;
}

.gain {
  color: var(--el-color-success);
}

.loss {
  color: var(--el-color-danger);
}
</style>
