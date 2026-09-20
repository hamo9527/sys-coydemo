<template>
  <div class="page-card">
    <div class="page-header">
      <h2>审批中心</h2>
      <el-button type="primary" @click="dialogVisible = true">新建审批</el-button>
    </div>

    <el-form :inline="true" class="query" @submit.prevent="handleLookup">
      <el-form-item label="审批 ID">
        <el-input v-model="queryId" placeholder="按 ID 查询" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :loading="loading" native-type="submit">查询</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="rows" stripe empty-text="暂无审批单">
      <el-table-column prop="id" label="ID" width="120" />
      <el-table-column prop="title" label="标题" min-width="160" />
      <el-table-column prop="bizType" label="业务类型" width="120" />
      <el-table-column prop="bizId" label="业务 ID" width="120" />
      <el-table-column prop="applicantId" label="申请人" width="100" />
      <el-table-column prop="status" label="状态" width="100" />
      <el-table-column label="操作" width="120" fixed="right">
        <template #default="{ row }">
          <el-button
            link
            type="primary"
            :disabled="row.status !== 'draft'"
            @click="handleSubmit(row.id)"
          >
            提交
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" title="新建审批" width="520px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="96px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" />
        </el-form-item>
        <el-form-item label="业务类型" prop="bizType">
          <el-select v-model="form.bizType" clearable placeholder="可选">
            <el-option label="采购" value="purchase" />
            <el-option label="费用" value="expense" />
            <el-option label="其他" value="other" />
          </el-select>
        </el-form-item>
        <el-form-item label="业务 ID" prop="bizId">
          <el-input v-model="form.bizId" clearable placeholder="可选" />
        </el-form-item>
        <el-form-item label="申请人 ID" prop="applicantId">
          <el-input v-model="form.applicantId" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="3" />
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
import { reactive, ref } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage } from 'element-plus'
import { createApproval, getApproval, submitApproval } from '@/api/oa'
import type { ApprovalView, CreateApprovalRequest } from '@/types/oa'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const loading = ref(false)
const saving = ref(false)
const dialogVisible = ref(false)
const queryId = ref('')
const rows = ref<ApprovalView[]>([])
const formRef = ref<FormInstance>()

const form = reactive<CreateApprovalRequest>({
  title: '',
  bizType: 'purchase',
  bizId: undefined,
  applicantId: String(userStore.profile?.id || ''),
  remark: '',
})

const rules: FormRules<CreateApprovalRequest> = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  applicantId: [{ required: true, message: '请输入申请人', trigger: 'blur' }],
}

function upsertRow(item: ApprovalView) {
  rows.value = [item, ...rows.value.filter((row) => row.id !== item.id)]
}

async function handleLookup() {
  const id = queryId.value.trim()
  if (!id) {
    ElMessage.warning('请输入有效审批 ID')
    return
  }
  loading.value = true
  try {
    upsertRow(await getApproval(id))
  } finally {
    loading.value = false
  }
}

async function handleCreate() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) {
    return
  }
  saving.value = true
  try {
    upsertRow(await createApproval({
      ...form,
      bizId: form.bizId ? String(form.bizId) : undefined,
      applicantId: String(form.applicantId),
    }))
    dialogVisible.value = false
    ElMessage.success('审批单已创建')
  } finally {
    saving.value = false
  }
}

async function handleSubmit(id: string) {
  const item = await submitApproval(id)
  upsertRow(item)
  ElMessage.success('已提交审批')
}
</script>

<style scoped>
.query {
  margin-bottom: 8px;
}
</style>
