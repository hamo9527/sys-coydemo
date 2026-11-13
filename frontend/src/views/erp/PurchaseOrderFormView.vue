<template>
  <div class="doc">
    <div class="toolbar page-card">
      <div class="toolbar-left">
        <el-button @click="router.push('/erp/purchase')">返回列表</el-button>
        <el-button type="primary" :loading="saving" @click="handleSave">保存草稿</el-button>
      </div>
    </div>

    <div class="page-card form-card">
      <h2>采购单</h2>
      <el-alert
        type="info"
        :closable="false"
        show-icon
        title="对接 POST /api/erp/purchases（draft）。入库请在列表操作 POST .../inbounds。"
        class="mb"
      />
      <el-form ref="formRef" :model="form" :rules="rules" label-width="110px" class="dense-form">
        <el-row :gutter="16">
          <el-col :xs="24" :md="12" :lg="8">
            <el-form-item label="采购单号" prop="orderNo">
              <el-input v-model="form.orderNo" placeholder="唯一单号" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :md="12" :lg="8">
            <el-form-item label="供应商 ID" prop="supplierId">
              <el-input v-model="form.supplierId" placeholder="对应 erp_partner.id" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :md="12" :lg="8">
            <el-form-item label="仓库 ID" prop="warehouseId">
              <el-input v-model="form.warehouseId" placeholder="对应 erp_warehouse.id" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" clearable />
            </el-form-item>
          </el-col>
        </el-row>

        <div class="items-head">
          <h3>明细 items[]</h3>
          <el-button size="small" @click="addItem">加行</el-button>
        </div>
        <el-table :data="form.items" border size="small">
          <el-table-column label="商品 ID" min-width="160">
            <template #default="{ row }">
              <el-input v-model="row.productId" placeholder="productId" />
            </template>
          </el-table-column>
          <el-table-column label="数量 qty" width="140">
            <template #default="{ row }">
              <el-input-number v-model="row.qty" :min="0.0001" :precision="4" controls-position="right" />
            </template>
          </el-table-column>
          <el-table-column label="单价 price" width="140">
            <template #default="{ row }">
              <el-input-number v-model="row.price" :min="0" :precision="4" controls-position="right" />
            </template>
          </el-table-column>
          <el-table-column label="操作" width="90">
            <template #default="{ $index }">
              <el-button
                link
                type="danger"
                :disabled="form.items.length <= 1"
                @click="removeItem($index)"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage } from 'element-plus'
import { createPurchase } from '@/api/erp'
import type { CreatePurchaseItemRequest, CreatePurchaseRequest } from '@/types/erp'

const router = useRouter()
const formRef = ref<FormInstance>()
const saving = ref(false)

const form = reactive<{
  orderNo: string
  supplierId: string
  warehouseId: string
  remark: string
  items: Array<{ productId: string; qty: number; price: number }>
}>({
  orderNo: '',
  supplierId: '1',
  warehouseId: '1',
  remark: '',
  items: [{ productId: '', qty: 1, price: 0 }],
})

const rules: FormRules = {
  orderNo: [{ required: true, message: '请输入采购单号', trigger: 'blur' }],
  supplierId: [{ required: true, message: '请输入供应商 ID', trigger: 'blur' }],
  warehouseId: [{ required: true, message: '请输入仓库 ID', trigger: 'blur' }],
}

function addItem() {
  form.items.push({ productId: '', qty: 1, price: 0 })
}

function removeItem(index: number) {
  if (form.items.length <= 1) {
    return
  }
  form.items.splice(index, 1)
}

function buildPayload(): CreatePurchaseRequest {
  const items: CreatePurchaseItemRequest[] = form.items
    .filter((item) => String(item.productId).trim())
    .map((item) => ({
      productId: String(item.productId).trim(),
      qty: item.qty,
      price: item.price,
    }))
  return {
    orderNo: form.orderNo.trim(),
    supplierId: String(form.supplierId).trim(),
    warehouseId: String(form.warehouseId).trim(),
    remark: form.remark.trim() || undefined,
    items,
  }
}

async function handleSave() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) {
    return
  }
  const payload = buildPayload()
  if (!payload.items.length) {
    ElMessage.warning('请至少填写一行商品明细')
    return
  }
  saving.value = true
  try {
    const created = await createPurchase(payload)
    ElMessage.success(`采购草稿已创建（${created.orderNo}）`)
    await router.push('/erp/purchase')
  } finally {
    saving.value = false
  }
}

onMounted(() => {
  form.orderNo = `PO${Date.now()}`
})
</script>

<style scoped lang="scss">
.doc {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.toolbar {
  padding-top: 12px;
  padding-bottom: 12px;
}

.toolbar-left {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.form-card h2 {
  margin: 0 0 12px;
  font-size: 18px;
}

.mb {
  margin-bottom: 16px;
}

.items-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin: 8px 0 12px;
}

.items-head h3 {
  margin: 0;
  font-size: 15px;
}

.dense-form :deep(.el-form-item) {
  margin-bottom: 14px;
}
</style>
