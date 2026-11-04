<template>
  <div class="page-card">
    <div class="page-header">
      <h2>商品档案</h2>
      <el-button type="primary" @click="dialogVisible = true">新建商品</el-button>
    </div>

    <el-form :inline="true" class="query" @submit.prevent="handleSearch">
      <el-form-item label="关键词">
        <el-input v-model="keyword" placeholder="商品编码 / 助记码 / 商品名称 / 品牌" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :loading="loading" native-type="submit">查询</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="rows" stripe v-loading="loading" empty-text="暂无商品">
      <el-table-column prop="sku" label="商品编码" fixed="left" min-width="130" />
      <el-table-column prop="mnemonicCode" label="助记码" min-width="110" />
      <el-table-column prop="name" label="商品名称" fixed="left" min-width="160" />
      <el-table-column prop="brandName" label="品牌名称" min-width="130" />
      <el-table-column prop="category" label="商品类别" min-width="120" />
      <el-table-column prop="spec" label="商品规格" min-width="120" />
      <el-table-column prop="unit" label="单位" width="90" />
      <el-table-column prop="auxiliaryUnit" label="辅助单位" min-width="100" />
      <el-table-column prop="auxiliarySpec" label="辅助规格" min-width="110" />
      <el-table-column prop="packageUnit" label="包装单位" min-width="100" />
      <el-table-column prop="packageSpec" label="包装规格" min-width="110" />
      <el-table-column prop="unitWeight" label="单重" width="100" />
      <el-table-column prop="minOrderQty" label="最低起订量" width="110" />
      <el-table-column prop="minStock" label="最小库存" width="100" />
      <el-table-column prop="maxStock" label="最大库存" width="100" />
      <el-table-column prop="smallColorStock" label="小色号库存" width="110" />
      <el-table-column prop="stockAgeThreshold" label="库龄阈值" width="100" />
      <el-table-column prop="slowMovingDays" label="呆滞天数" width="100" />
      <el-table-column prop="shelfLifeDays" label="保质天数" width="100" />
      <el-table-column prop="productAttribute" label="商品属性" min-width="110" />
      <el-table-column prop="virtualProduct" label="虚拟商品" width="100">
        <template #default="{ row }">{{ row.virtualProduct ? '是' : '否' }}</template>
      </el-table-column>
      <el-table-column prop="installationIncluded" label="包安装" width="90">
        <template #default="{ row }">{{ row.installationIncluded ? '是' : '否' }}</template>
      </el-table-column>
      <el-table-column prop="cloudStoreVisible" label="云店可查" width="100">
        <template #default="{ row }">{{ row.cloudStoreVisible ? '是' : '否' }}</template>
      </el-table-column>
      <el-table-column prop="productionProcess" label="生产工艺" min-width="120" />
      <el-table-column prop="productionOrigin" label="生产产地" min-width="120" />
      <el-table-column prop="usageLocation" label="使用位置" min-width="120" />
      <el-table-column prop="vrUrl" label="VR地址" min-width="180" />
      <el-table-column prop="remark" label="备注" min-width="160" />
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

    <el-dialog v-model="dialogVisible" title="新建商品" width="820px" top="5vh" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="112px">
        <el-row :gutter="16">
          <el-col v-for="field in textFields" :key="field.prop" :span="12">
            <el-form-item :label="field.label" :prop="field.prop">
              <el-input v-model="form[field.prop]" :placeholder="'请输入' + field.label" />
            </el-form-item>
          </el-col>
          <el-col v-for="field in numberFields" :key="field.prop" :span="12">
            <el-form-item :label="field.label" :prop="field.prop">
              <el-input-number v-model="form[field.prop]" :min="0" :precision="field.precision ?? 0" controls-position="right" />
            </el-form-item>
          </el-col>
          <el-col v-for="field in booleanFields" :key="field.prop" :span="8">
            <el-form-item :label="field.label" :prop="field.prop">
              <el-switch v-model="form[field.prop]" />
            </el-form-item>
          </el-col>
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
  sku: '',
  mnemonicCode: '',
  name: '',
  brandName: '',
  category: '',
  spec: '',
  unit: '',
  auxiliaryUnit: '',
  auxiliarySpec: '',
  packageUnit: '',
  packageSpec: '',
  unitWeight: undefined,
  minOrderQty: undefined,
  minStock: undefined,
  maxStock: undefined,
  smallColorStock: undefined,
  stockAgeThreshold: undefined,
  slowMovingDays: undefined,
  shelfLifeDays: undefined,
  productAttribute: '',
  virtualProduct: false,
  installationIncluded: false,
  cloudStoreVisible: false,
  productionProcess: '',
  productionOrigin: '',
  usageLocation: '',
  vrUrl: '',
  remark: '',
})

const textFields = [
  { prop: 'sku', label: '商品编码' },
  { prop: 'mnemonicCode', label: '助记码' },
  { prop: 'name', label: '商品名称' },
  { prop: 'brandName', label: '品牌名称' },
  { prop: 'category', label: '商品类别' },
  { prop: 'spec', label: '商品规格' },
  { prop: 'unit', label: '单位' },
  { prop: 'auxiliaryUnit', label: '辅助单位' },
  { prop: 'auxiliarySpec', label: '辅助规格' },
  { prop: 'packageUnit', label: '包装单位' },
  { prop: 'packageSpec', label: '包装规格' },
  { prop: 'productAttribute', label: '商品属性' },
  { prop: 'productionProcess', label: '生产工艺' },
  { prop: 'productionOrigin', label: '生产产地' },
  { prop: 'usageLocation', label: '使用位置' },
  { prop: 'vrUrl', label: 'VR地址' },
  { prop: 'remark', label: '备注' },
] as const

const numberFields = [
  { prop: 'unitWeight', label: '单重', precision: 4 },
  { prop: 'minOrderQty', label: '最低起订量', precision: 4 },
  { prop: 'minStock', label: '最小库存', precision: 4 },
  { prop: 'maxStock', label: '最大库存', precision: 4 },
  { prop: 'smallColorStock', label: '小色号库存', precision: 4 },
  { prop: 'stockAgeThreshold', label: '库龄阈值' },
  { prop: 'slowMovingDays', label: '呆滞天数' },
  { prop: 'shelfLifeDays', label: '保质天数' },
] as const

const booleanFields = [
  { prop: 'virtualProduct', label: '虚拟商品' },
  { prop: 'installationIncluded', label: '包安装' },
  { prop: 'cloudStoreVisible', label: '云店可查' },
] as const

const rules: FormRules<CreateProductRequest> = {
  sku: [{ required: true, message: '请输入商品编码', trigger: 'blur' }],
  name: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
  brandName: [{ required: true, message: '请输入品牌名称', trigger: 'blur' }],
  unit: [{ required: true, message: '请输入单位', trigger: 'blur' }],
  productAttribute: [{ required: true, message: '请输入商品属性', trigger: 'blur' }],
}

async function loadList() {
  loading.value = true
  try {
    const result = await listProducts({
      keyword: keyword.value || undefined,
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
.query {
  margin-bottom: 8px;
}

.pager {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}
</style>
