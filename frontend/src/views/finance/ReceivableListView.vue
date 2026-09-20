<template>
  <div class="page-card">
    <div class="page-header">
      <h2>应收管理</h2>
      <el-button type="primary" @click="createVisible = true">登记应收</el-button>
    </div>

    <el-form :inline="true" class="query" @submit.prevent="handleLookup">
      <el-form-item label="应收 ID">
        <el-input v-model="queryId" placeholder="按 ID 查询" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :loading="loading" native-type="submit">查询</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="rows" stripe empty-text="暂无应收单">
      <el-table-column prop="id" label="ID" width="120" />
      <el-table-column prop="billNo" label="单据号" min-width="140" />
      <el-table-column prop="customerId" label="客户 ID" width="110" />
      <el-table-column prop="amount" label="应收金额" width="120" />
      <el-table-column prop="receivedAmount" label="已收" width="120" />
      <el-table-column prop="dueDate" label="到期日" width="120" />
      <el-table-column prop="status" label="状态" width="100" />
      <el-table-column label="操作" width="120" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="openPayment(row)">收款</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="createVisible" title="登记应收" width="520px" destroy-on-close>
      <el-form ref="createFormRef" :model="createForm" :rules="createRules" label-width="110px">
        <el-form-item label="单据号" prop="billNo">
          <el-input v-model="createForm.billNo" />
        </el-form-item>
        <el-form-item label="客户 ID" prop="customerId">
          <el-input v-model="createForm.customerId" placeholder="customerId" />
        </el-form-item>
        <el-form-item label="来源订单" prop="sourceOrderId">
          <el-input v-model="createForm.sourceOrderId" clearable placeholder="可选" />
        </el-form-item>
        <el-form-item label="金额" prop="amount">
          <el-input-number v-model="createForm.amount" :min="0.0001" :precision="4" />
        </el-form-item>
        <el-form-item label="到期日" prop="dueDate">
          <el-date-picker v-model="createForm.dueDate" type="date" value-format="YYYY-MM-DD" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="createVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="handleCreate">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="payVisible" title="收款核销" width="420px" destroy-on-close>
      <el-form ref="payFormRef" :model="payForm" :rules="payRules" label-width="90px">
        <el-form-item label="收款金额" prop="amount">
          <el-input-number v-model="payForm.amount" :min="0.0001" :precision="4" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="payVisible = false">取消</el-button>
        <el-button type="primary" :loading="paying" @click="handlePay">确认收款</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage } from 'element-plus'
import { createReceivable, getReceivable, receivePayment } from '@/api/finance'
import type {
  CreateReceivableRequest,
  ReceivableView,
  ReceivePaymentRequest,
} from '@/types/finance'

const loading = ref(false)
const saving = ref(false)
const paying = ref(false)
const createVisible = ref(false)
const payVisible = ref(false)
const queryId = ref('')
const currentId = ref<string | null>(null)
const rows = ref<ReceivableView[]>([])
const createFormRef = ref<FormInstance>()
const payFormRef = ref<FormInstance>()

const createForm = reactive<{
  billNo: string
  customerId: string
  sourceOrderId: string
  amount: number
  dueDate?: string
}>({
  billNo: '',
  customerId: '1',
  sourceOrderId: '',
  amount: 0,
  dueDate: undefined,
})

const payForm = reactive<ReceivePaymentRequest>({
  amount: 0,
})

const createRules: FormRules = {
  billNo: [{ required: true, message: '请输入单据号', trigger: 'blur' }],
  customerId: [{ required: true, message: '请输入客户', trigger: 'blur' }],
  amount: [{ required: true, message: '请输入金额', trigger: 'change' }],
}

const payRules: FormRules<ReceivePaymentRequest> = {
  amount: [{ required: true, message: '请输入收款金额', trigger: 'change' }],
}

function upsertRow(item: ReceivableView) {
  rows.value = [item, ...rows.value.filter((row) => row.id !== item.id)]
}

async function handleLookup() {
  const id = queryId.value.trim()
  if (!id) {
    ElMessage.warning('请输入有效应收 ID')
    return
  }
  loading.value = true
  try {
    upsertRow(await getReceivable(id))
  } finally {
    loading.value = false
  }
}

async function handleCreate() {
  const valid = await createFormRef.value?.validate().catch(() => false)
  if (!valid) {
    return
  }
  saving.value = true
  try {
    const payload: CreateReceivableRequest = {
      billNo: createForm.billNo.trim(),
      customerId: createForm.customerId.trim(),
      sourceOrderId: createForm.sourceOrderId.trim() || undefined,
      amount: createForm.amount,
      dueDate: createForm.dueDate,
    }
    upsertRow(await createReceivable(payload))
    createVisible.value = false
    ElMessage.success('应收已登记')
  } finally {
    saving.value = false
  }
}

function openPayment(row: ReceivableView | Record<string, unknown>) {
  const data = row as ReceivableView
  currentId.value = String(data.id)
  payForm.amount = Math.max(data.amount - data.receivedAmount, 0.0001)
  payVisible.value = true
}

async function handlePay() {
  if (!currentId.value) {
    return
  }
  const valid = await payFormRef.value?.validate().catch(() => false)
  if (!valid) {
    return
  }
  paying.value = true
  try {
    upsertRow(await receivePayment(currentId.value, { ...payForm }))
    payVisible.value = false
    ElMessage.success('收款成功')
  } finally {
    paying.value = false
  }
}
</script>

<style scoped>
.query {
  margin-bottom: 8px;
}
</style>
