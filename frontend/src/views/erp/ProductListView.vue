<template>
  <div class="page-card">
    <div class="page-header">
      <h2>商品档案</h2>
      <el-button type="primary" @click="dialogVisible = true">新建商品</el-button>
    </div>
    <el-form :inline="true" class="query" @submit.prevent="handleSearch">
      <el-form-item label="关键词">
        <el-input v-model="keyword" placeholder="商品编码 / 助记码 / 商品名称 / 品牌名称" clearable />
      </el-form-item>
      <el-form-item><el-button type="primary" :loading="loading" native-type="submit">查询</el-button></el-form-item>
    </el-form>
    <el-table :data="rows" stripe border v-loading="loading" empty-text="暂无商品" style="width: 100%">
      <el-table-column prop="sku" label="*商品编码" min-width="130" />
      <el-table-column prop="mnemonicCode" label="助记码" min-width="110" />
      <el-table-column prop="name" label="*商品名称" min-width="150" />
      <el-table-column prop="brandName" label="*品牌名称" min-width="130" />
      <el-table-column prop="category" label="商品类别" min-width="110" />
      <el-table-column prop="spec" label="商品规格" min-width="120" />
      <el-table-column prop="unit" label="*单位" min-width="80" />
      <el-table-column prop="auxiliaryUnit" label="辅助单位" min-width="90" />
      <el-table-column prop="auxiliarySpec" label="辅助规格" min-width="110" />
      <el-table-column prop="packagingUnit" label="包装单位" min-width="90" />
      <el-table-column prop="packagingSpec" label="包装规格" min-width="110" />
      <el-table-column prop="unitWeight" label="单重" min-width="90" />
      <el-table-column prop="minimumOrderQty" label="最低起订量" min-width="110" />
      <el-table-column prop="minStock" label="最小库存" min-width="100" />
      <el-table-column prop="maxStock" label="最大库存" min-width="100" />
      <el-table-column label="小色号库存" min-width="110"><template #default="{ row }">{{ row.smallColorStock ? '是' : '否' }}</template></el-table-column>
      <el-table-column prop="stockAgeThreshold" label="库龄阈值" min-width="100" />
      <el-table-column prop="obsoleteDays" label="呆滞天数" min-width="100" />
      <el-table-column prop="shelfLifeDays" label="保质天数" min-width="100" />
      <el-table-column prop="productAttribute" label="*商品属性" min-width="110" />
      <el-table-column label="虚拟商品" min-width="90"><template #default="{ row }">{{ row.virtualProduct ? '是' : '否' }}</template></el-table-column>
      <el-table-column label="包安装" min-width="80"><template #default="{ row }">{{ row.installationRequired ? '是' : '否' }}</template></el-table-column>
      <el-table-column label="云店可查" min-width="90"><template #default="{ row }">{{ row.cloudStoreVisible ? '是' : '否' }}</template></el-table-column>
      <el-table-column prop="productionProcess" label="生产工艺" min-width="140" />
      <el-table-column prop="productionOrigin" label="生产产地" min-width="120" />
      <el-table-column prop="usageLocation" label="使用位置" min-width="120" />
      <el-table-column prop="vrUrl" label="VR地址" min-width="160" />
      <el-table-column prop="remark" label="备注" min-width="160" />
    </el-table>
    <div class="pager">
      <el-pagination v-model:current-page="page" v-model:page-size="size" layout="total, prev, pager, next" :total="total" @current-change="loadList" @size-change="handleSizeChange" />
    </div>
    <el-dialog v-model="dialogVisible" title="新建商品" width="900px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="110px">
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="*商品编码" prop="sku"><el-input v-model="form.sku" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="助记码" prop="mnemonicCode"><el-input v-model="form.mnemonicCode" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="*商品名称" prop="name"><el-input v-model="form.name" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="*品牌名称" prop="brandName"><el-input v-model="form.brandName" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="商品类别" prop="category"><el-input v-model="form.category" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="商品规格" prop="spec"><el-input v-model="form.spec" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="*单位" prop="unit"><el-input v-model="form.unit" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="辅助单位" prop="auxiliaryUnit"><el-input v-model="form.auxiliaryUnit" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="辅助规格" prop="auxiliarySpec"><el-input v-model="form.auxiliarySpec" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="包装单位" prop="packagingUnit"><el-input v-model="form.packagingUnit" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="包装规格" prop="packagingSpec"><el-input v-model="form.packagingSpec" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="单重" prop="unitWeight"><el-input-number v-model="form.unitWeight" :min="0" :precision="4" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="最低起订量" prop="minimumOrderQty"><el-input-number v-model="form.minimumOrderQty" :min="0" :precision="4" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="最小库存" prop="minStock"><el-input-number v-model="form.minStock" :min="0" :precision="4" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="最大库存" prop="maxStock"><el-input-number v-model="form.maxStock" :min="0" :precision="4" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="小色号库存" prop="smallColorStock"><el-switch v-model="form.smallColorStock" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="库龄阈值" prop="stockAgeThreshold"><el-input-number v-model="form.stockAgeThreshold" :min="0" :precision="0" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="呆滞天数" prop="obsoleteDays"><el-input-number v-model="form.obsoleteDays" :min="0" :precision="0" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="保质天数" prop="shelfLifeDays"><el-input-number v-model="form.shelfLifeDays" :min="0" :precision="0" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="*商品属性" prop="productAttribute"><el-select v-model="form.productAttribute"><el-option label="普通商品" value="normal" /><el-option label="服务商品" value="service" /><el-option label="组合商品" value="bundle" /></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="虚拟商品" prop="virtualProduct"><el-switch v-model="form.virtualProduct" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="包安装" prop="installationRequired"><el-switch v-model="form.installationRequired" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="云店可查" prop="cloudStoreVisible"><el-switch v-model="form.cloudStoreVisible" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="生产工艺" prop="productionProcess"><el-input v-model="form.productionProcess" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="生产产地" prop="productionOrigin"><el-input v-model="form.productionOrigin" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="使用位置" prop="usageLocation"><el-input v-model="form.usageLocation" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="VR地址" prop="vrUrl"><el-input v-model="form.vrUrl" /></el-form-item></el-col>
          <el-col :span="24"><el-form-item label="备注" prop="remark"><el-input v-model="form.remark" type="textarea" /></el-form-item></el-col>
        </el-row>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="handleCreate">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage } from 'element-plus'
import { createProduct, listProducts } from '@/api/erp'
import type { CreateProductRequest, ProductView } from '@/types/erp'

const loading = ref(false)
const saving = ref(false)
const dialogVisible = ref(false)
const keyword = ref('')
const page = ref(1)
const size = ref(10)
const total = ref(0)
const rows = ref<ProductView[]>([])
const formRef = ref<FormInstance>()

const form = reactive<CreateProductRequest>({
  sku: '', mnemonicCode: '', name: '', brandName: '', category: '', spec: '', unit: '',
  auxiliaryUnit: '', auxiliarySpec: '', packagingUnit: '', packagingSpec: '',
  unitWeight: undefined, minimumOrderQty: undefined, minStock: undefined, maxStock: undefined,
  smallColorStock: false, stockAgeThreshold: undefined, obsoleteDays: undefined, shelfLifeDays: undefined,
  productAttribute: 'normal', virtualProduct: false, installationRequired: false, cloudStoreVisible: false,
  productionProcess: '', productionOrigin: '', usageLocation: '', vrUrl: '', remark: '',
  purchasePrice: 0, salePrice: 0,
})

const rules: FormRules<CreateProductRequest> = {
  sku: [{ required: true, message: '请输入商品编码', trigger: 'blur' }],
  name: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
  brandName: [{ required: true, message: '请输入品牌名称', trigger: 'blur' }],
  unit: [{ required: true, message: '请输入单位', trigger: 'blur' }],
  productAttribute: [{ required: true, message: '请选择商品属性', trigger: 'change' }],
}

async function loadList() {
  loading.value = true
  try {
    const result = await listProducts({ keyword: keyword.value || undefined, page: page.value, size: size.value })
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

async function handleCreate() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return
  saving.value = true
  try {
    await createProduct({ ...form })
    dialogVisible.value = false
    ElMessage.success('商品已创建')
    await loadList()
  } finally {
    saving.value = false
  }
}

onMounted(() => {
  void loadList()
})
</script>

<style scoped>
.query { margin-bottom: 8px; }
.pager { display: flex; justify-content: flex-end; margin-top: 16px; }
</style>
