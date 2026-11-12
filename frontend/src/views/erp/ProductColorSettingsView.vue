<template>
  <div class="page-card">
    <div class="page-header"><h2>商品色号配置</h2></div>
    <el-alert type="info" :closable="false" title="NONE：不管理色号；DYNAMIC：单据中可动态新建/选择色号；BOUND：单据只能选择商品已绑定的色号。" />
    <el-form :inline="true" class="query">
      <el-form-item label="商品"><el-select v-model="productId" filterable remote clearable :remote-method="searchProducts" :loading="productLoading" placeholder="搜索商品编码/名称" style="width: 360px" @change="loadConfig">
        <el-option v-for="item in products" :key="item.id" :label="`${item.sku} - ${item.name}`" :value="String(item.id)" />
      </el-select></el-form-item>
    </el-form>
    <template v-if="productId">
      <el-divider content-position="left">库存管理模式</el-divider>
      <el-form label-width="150px" style="max-width: 560px">
        <el-form-item label="色号管理方式">
          <el-radio-group v-model="mode.colorManagementMode">
            <el-radio value="NONE">无色号</el-radio><el-radio value="DYNAMIC">动态色号</el-radio><el-radio value="BOUND">档案绑定色号</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="启用批次管理"><el-switch v-model="mode.batchManagementEnabled" /></el-form-item>
        <el-form-item label="启用等级管理"><el-switch v-model="mode.gradeManagementEnabled" /></el-form-item>
        <el-form-item><el-button type="primary" :loading="saving" @click="saveMode">保存配置</el-button></el-form-item>
      </el-form>
      <template v-if="mode.colorManagementMode === 'BOUND'">
        <el-divider content-position="left">商品绑定色号</el-divider>
        <el-form :inline="true">
          <el-form-item label="选择色号"><el-select v-model="selectedColorId" filterable placeholder="请选择色号" style="width: 320px">
            <el-option v-for="item in availableColors" :key="item.id" :label="`${item.code} - ${item.name}`" :value="String(item.id)" />
          </el-select></el-form-item>
          <el-form-item><el-button type="primary" :disabled="!selectedColorId" @click="bindColor">绑定</el-button></el-form-item>
        </el-form>
        <el-table :data="boundColors" stripe>
          <el-table-column prop="code" label="内部色号" width="150" /><el-table-column prop="name" label="名称" />
          <el-table-column prop="brandName" label="品牌" /><el-table-column label="操作" width="100">
            <template #default="{ row }"><el-button link type="danger" @click="unbindColor(row.id)">解绑</el-button></template>
          </el-table-column>
        </el-table>
      </template>
    </template>
  </div>
</template>
<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { bindProductColor, getProductColorMode, listColors, listProductColors, listProducts, unbindProductColor, updateProductColorMode } from '@/api/erp'
import type { ColorView, ProductColorMode, ProductView } from '@/types/erp'
const productId = ref('')
const products = ref<ProductView[]>([])
const productLoading = ref(false)
const saving = ref(false)
const selectedColorId = ref('')
const allColors = ref<ColorView[]>([])
const boundColors = ref<ColorView[]>([])
const mode = reactive<Omit<ProductColorMode, 'productId'>>({ colorManagementMode: 'NONE', batchManagementEnabled: false, gradeManagementEnabled: false })
const availableColors = computed(() => allColors.value.filter(c => !boundColors.value.some(b => b.id === c.id)))
async function searchProducts(keyword: string) {
  productLoading.value = true
  try { products.value = (await listProducts({ keyword, page: 1, size: 30 })).records }
  finally { productLoading.value = false }
}
async function loadConfig() {
  if (!productId.value) return
  const config = await getProductColorMode(productId.value)
  Object.assign(mode, { colorManagementMode: config.colorManagementMode, batchManagementEnabled: config.batchManagementEnabled, gradeManagementEnabled: config.gradeManagementEnabled })
  await loadBindings()
}
async function loadBindings() {
  if (!productId.value) return
  boundColors.value = await listProductColors(productId.value)
}
async function saveMode() {
  saving.value = true
  try {
    await updateProductColorMode(productId.value, { ...mode })
    ElMessage.success('商品库存管理配置已保存')
    if (mode.colorManagementMode !== 'BOUND') boundColors.value = []
  } finally { saving.value = false }
}
async function bindColor() {
  if (!selectedColorId.value) return
  await bindProductColor(productId.value, selectedColorId.value)
  selectedColorId.value = ''
  await loadBindings()
  ElMessage.success('色号已绑定')
}
async function unbindColor(id: string | number) {
  await ElMessageBox.confirm('解绑只影响后续单据选择，不删除色号档案及历史记录。确定继续吗？', '解绑确认')
  await unbindProductColor(productId.value, id)
  await loadBindings()
  ElMessage.success('色号已解绑')
}
onMounted(async () => { await searchProducts(''); allColors.value = await listColors() })
</script>
<style scoped>.query { margin-top: 16px; }</style>
