<template>
  <div class="page-card">
    <div class="page-header">
      <h2>库存台账</h2>
    </div>

    <el-form :inline="true" class="query" @submit.prevent="handleSearch">
      <el-form-item label="仓库 ID">
        <el-input v-model="warehouseId" clearable placeholder="可选" />
      </el-form-item>
      <el-form-item label="商品 ID">
        <el-input v-model="productId" clearable placeholder="可选" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :loading="loading" native-type="submit">查询</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="rows" stripe v-loading="loading" empty-text="暂无库存，可通过采购入库生成">
      <el-table-column prop="id" label="ID" width="160" />
      <el-table-column prop="warehouseId" label="仓库 ID" width="140" />
      <el-table-column prop="productId" label="商品 ID" width="160" />
      <el-table-column prop="quantity" label="可用数量" width="120" />
      <el-table-column prop="lockedQty" label="锁定数量" width="120" />
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
import { listStocks } from '@/api/erp'
import type { StockView } from '@/types/erp'

const loading = ref(false)
const warehouseId = ref('')
const productId = ref('')
const page = ref(1)
const size = ref(10)
const total = ref(0)
const rows = ref<StockView[]>([])

async function loadList() {
  loading.value = true
  try {
    const result = await listStocks({
      warehouseId: warehouseId.value.trim() || undefined,
      productId: productId.value.trim() || undefined,
      page: page.value,
      size: size.value,
    })
    rows.value = result.records
    total.value = result.total
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  page.value = 1
  void loadList()
}

function handleSizeChange() {
  page.value = 1
  void loadList()
}

onMounted(() => {
  void loadList()
})
</script>

<style scoped>
.query {
  margin-bottom: 8px;
}

.pager {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}
</style>
