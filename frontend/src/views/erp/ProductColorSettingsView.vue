<template>
  <div class="page-card">
    <div class="page-header"><h2>商品库存维度配置</h2></div>
    <el-alert
      type="info"
      :closable="false"
      title="色号不需要单独建档。启用动态色号后，可在入库、出库单据中自由输入，并从该商品历史使用记录中选择。"
    />
    <el-form :inline="true" class="query">
      <el-form-item label="商品">
        <el-select
          v-model="productId"
          filterable
          remote
          clearable
          :remote-method="searchProducts"
          :loading="productLoading"
          placeholder="搜索商品编码/名称"
          style="width: 360px"
          @change="loadConfig"
        >
          <el-option
            v-for="item in products"
            :key="item.id"
            :label="`${item.sku} - ${item.name}`"
            :value="String(item.id)"
          />
        </el-select>
      </el-form-item>
    </el-form>
    <template v-if="productId">
      <el-divider content-position="left">库存维度</el-divider>
      <el-form label-width="150px" style="max-width: 560px">
        <el-form-item label="色号管理方式">
          <el-radio-group v-model="mode.colorManagementMode">
            <el-radio value="NONE">不管理色号</el-radio>
            <el-radio value="DYNAMIC">单据动态录入</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="启用批次管理">
          <el-switch v-model="mode.batchManagementEnabled" />
        </el-form-item>
        <el-form-item label="启用等级管理">
          <el-switch v-model="mode.gradeManagementEnabled" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="saving" @click="saveMode">保存配置</el-button>
        </el-form-item>
      </el-form>
    </template>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { getProductColorMode, listProducts, updateProductColorMode } from '@/api/erp'
import type { ProductColorMode, ProductView } from '@/types/erp'

const productId = ref('')
const products = ref<ProductView[]>([])
const productLoading = ref(false)
const saving = ref(false)
const mode = reactive<Omit<ProductColorMode, 'productId'>>({
  colorManagementMode: 'NONE',
  batchManagementEnabled: false,
  gradeManagementEnabled: false,
})

async function searchProducts(keyword: string) {
  productLoading.value = true
  try {
    products.value = (await listProducts({ keyword, page: 1, size: 30 })).records
  } finally {
    productLoading.value = false
  }
}

async function loadConfig() {
  if (!productId.value) return
  const config = await getProductColorMode(productId.value)
  Object.assign(mode, {
    colorManagementMode: config.colorManagementMode,
    batchManagementEnabled: config.batchManagementEnabled,
    gradeManagementEnabled: config.gradeManagementEnabled,
  })
}

async function saveMode() {
  saving.value = true
  try {
    await updateProductColorMode(productId.value, { ...mode })
    ElMessage.success('商品库存维度配置已保存')
  } finally {
    saving.value = false
  }
}

onMounted(async () => {
  await searchProducts('')
})
</script>

<style scoped>
.query {
  margin-top: 16px;
}
</style>
