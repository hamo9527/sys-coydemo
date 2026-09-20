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
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { inboundPurchase, listPurchases } from '@/api/erp'
import type { PurchaseView } from '@/types/erp'

const router = useRouter()
const loading = ref(false)
const page = ref(1)
const size = ref(10)
const total = ref(0)
const rows = ref<PurchaseView[]>([])

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

async function handleInbound(id: string) {
  await inboundPurchase(id)
  ElMessage.success('入库成功')
  await loadList()
}

onMounted(() => {
  void loadList()
})
</script>

<style scoped>
.pager {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}
</style>
