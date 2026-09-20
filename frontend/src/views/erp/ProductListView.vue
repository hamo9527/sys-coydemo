<template>
  <div class="page-card">
    <div class="page-header">
      <h2>商品档案</h2>
      <el-button type="primary" @click="dialogVisible = true">新建商品</el-button>
    </div>

    <el-form :inline="true" class="query" @submit.prevent="handleSearch">
      <el-form-item label="关键词">
        <el-input v-model="keyword" placeholder="SKU / 名称" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :loading="loading" native-type="submit">查询</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="rows" stripe v-loading="loading" empty-text="暂无商品">
      <el-table-column prop="id" label="ID" width="160" />
      <el-table-column prop="sku" label="SKU" min-width="120" />
      <el-table-column prop="name" label="名称" min-width="140" />
      <el-table-column prop="unit" label="单位" width="80" />
      <el-table-column prop="purchasePrice" label="采购价" width="110" />
      <el-table-column prop="salePrice" label="销售价" width="110" />
      <el-table-column prop="status" label="状态" width="80" />
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

    <el-dialog v-model="dialogVisible" title="新建商品" width="520px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="96px">
        <el-form-item label="SKU" prop="sku">
          <el-input v-model="form.sku" />
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="分类" prop="category">
          <el-input v-model="form.category" />
        </el-form-item>
        <el-form-item label="单位" prop="unit">
          <el-input v-model="form.unit" placeholder="吨 / 袋 / 米" />
        </el-form-item>
        <el-form-item label="规格" prop="spec">
          <el-input v-model="form.spec" />
        </el-form-item>
        <el-form-item label="采购价" prop="purchasePrice">
          <el-input-number v-model="form.purchasePrice" :min="0" :precision="4" />
        </el-form-item>
        <el-form-item label="销售价" prop="salePrice">
          <el-input-number v-model="form.salePrice" :min="0" :precision="4" />
        </el-form-item>
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
  name: '',
  category: '',
  unit: '吨',
  spec: '',
  purchasePrice: 0,
  salePrice: 0,
})

const rules: FormRules<CreateProductRequest> = {
  sku: [{ required: true, message: '请输入 SKU', trigger: 'blur' }],
  name: [{ required: true, message: '请输入名称', trigger: 'blur' }],
  purchasePrice: [{ required: true, message: '请输入采购价', trigger: 'change' }],
  salePrice: [{ required: true, message: '请输入销售价', trigger: 'change' }],
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
  if (!valid) {
    return
  }
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
